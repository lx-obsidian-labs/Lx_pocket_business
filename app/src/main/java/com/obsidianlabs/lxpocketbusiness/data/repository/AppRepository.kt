package com.obsidianlabs.lxpocketbusiness.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.gson.Gson
import com.obsidianlabs.lxpocketbusiness.data.db.RoomWorkspaceStore
import com.obsidianlabs.lxpocketbusiness.data.db.WorkspaceDatabase
import com.obsidianlabs.lxpocketbusiness.data.model.AppSnapshot
import com.obsidianlabs.lxpocketbusiness.data.model.Business
import com.obsidianlabs.lxpocketbusiness.data.model.CashEvent
import com.obsidianlabs.lxpocketbusiness.data.model.CashEventType
import com.obsidianlabs.lxpocketbusiness.data.model.CatalogueItem
import com.obsidianlabs.lxpocketbusiness.data.model.Customer
import com.obsidianlabs.lxpocketbusiness.data.model.Debt
import com.obsidianlabs.lxpocketbusiness.data.model.DebtOrigin
import com.obsidianlabs.lxpocketbusiness.data.model.Document
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentStatus
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentType
import com.obsidianlabs.lxpocketbusiness.data.model.LocalTerms
import com.obsidianlabs.lxpocketbusiness.data.model.TaskAction
import com.obsidianlabs.lxpocketbusiness.data.model.TaskType
import com.obsidianlabs.lxpocketbusiness.data.model.formatMoneyMinor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicBoolean

private val Context.dataStore by preferencesDataStore(name = "lx_pocket_settings")

enum class PeriodFilter {
    TODAY,
    THIS_WEEK,
    THIS_MONTH,
    LAST_MONTH,
    CUSTOM
}

enum class AttentionSource {
    TASK,
    DEBT,
    QUOTE
}

enum class ActivityType {
    MONEY_IN,
    MONEY_OUT,
    DEBT,
    DOCUMENT
}

data class AttentionItem(
    val id: String,
    val source: AttentionSource,
    val sourceId: String,
    val title: String,
    val subtitle: String,
    val dueDate: String,
    val isOverdue: Boolean,
    val targetTab: Int
)

data class ActivityItem(
    val id: String,
    val type: ActivityType,
    val title: String,
    val subtitle: String,
    val date: String,
    val amountMinor: Long,
    val targetTab: Int
)

data class DashboardData(
    val summary: FinancialSummary = FinancialSummary(),
    val periodLabel: String = "",
    val debtAsOfDate: String = "",
    val attention: List<AttentionItem> = emptyList(),
    val recentActivity: List<ActivityItem> = emptyList(),
    val hasActivity: Boolean = false,
    val isEmpty: Boolean = true
)

data class FinancialSummary(
    val moneyReceivedMinor: Long = 0L,
    val moneySpentMinor: Long = 0L,
    val netMovementMinor: Long = 0L,
    val refundMinor: Long = 0L,
    val ownerTransferMinor: Long = 0L,
    val debtOwedMinor: Long = 0L,
    val periodStart: String = "",
    val periodEndExclusive: String = "",
    val asOfDate: String = "",
    val periodLabel: String = ""
) {
    val formattedMoneyReceived: String get() = formatMoneyMinor(moneyReceivedMinor)
    val formattedMoneySpent: String get() = formatMoneyMinor(moneySpentMinor)
    val formattedNetMovement: String get() = formatMoneyMinor(netMovementMinor)
    val formattedRefund: String get() = formatMoneyMinor(refundMinor)
    val formattedDebtOwed: String get() = formatMoneyMinor(debtOwedMinor)
    val formattedPeriodRange: String get() = "$periodStart to $asOfDate"
}

private data class LedgerPeriod(val start: LocalDate, val endExclusive: LocalDate)

class AppRepository private constructor(private val context: Context) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val gson = Gson()
    private val store = RoomWorkspaceStore(
        Room.databaseBuilder(context.applicationContext, WorkspaceDatabase::class.java, "lx_workspace.db").build()
    )
    private val snapshots = Channel<AppSnapshot>(Channel.CONFLATED)
    private val writerStarted = AtomicBoolean(false)
    private val snapshotKey = stringPreferencesKey("app_snapshot_v1")
    private val onboardingKey = booleanPreferencesKey("onboarding_completed")
    private val businessNameKey = stringPreferencesKey("business_name")
    private val businessSectorKey = stringPreferencesKey("business_sector")
    private val businessLogoKey = stringPreferencesKey("business_logo")
    private val appLockKey = booleanPreferencesKey("app_lock_enabled")
    private val remindersKey = booleanPreferencesKey("reminders_enabled")
    private val supporterKey = booleanPreferencesKey("supporter_badge")
    private val adsEnabledKey = booleanPreferencesKey("ads_enabled")

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded = _isLoaded.asStateFlow()
    private val _storageError = MutableStateFlow<String?>(null)
    val storageError = _storageError.asStateFlow()
    private val _startupStage = MutableStateFlow(0)
    val startupStage = _startupStage.asStateFlow()
    private val _appLockEnabled = MutableStateFlow(false)
    val appLockEnabled = _appLockEnabled.asStateFlow()
    private val _isLocked = MutableStateFlow(false)
    val isLocked = _isLocked.asStateFlow()
    private val _remindersEnabled = MutableStateFlow(true)
    val remindersEnabled = _remindersEnabled.asStateFlow()
    private val _isSupporter = MutableStateFlow(false)
    val isSupporter = _isSupporter.asStateFlow()
    private val _adsEnabled = MutableStateFlow(true)
    val adsEnabled = _adsEnabled.asStateFlow()
    private val _onboardingStep = MutableStateFlow("WELCOME")
    val onboardingStep = _onboardingStep.asStateFlow()
    private val _termsAccepted = MutableStateFlow(false)
    val termsAccepted = _termsAccepted.asStateFlow()
    private var acceptedTermsVersion: String? = null
    private var acceptedTermsDigest: String? = null
    private var acceptedTermsAt: Long? = null

    private val _isOnboardingCompleted = MutableStateFlow(false)
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()
    private val _business = MutableStateFlow<Business?>(null)
    val business: StateFlow<Business?> = _business.asStateFlow()
    private val _customers = MutableStateFlow<List<Customer>>(emptyList())
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()
    private val _catalogueItems = MutableStateFlow<List<CatalogueItem>>(emptyList())
    val catalogueItems: StateFlow<List<CatalogueItem>> = _catalogueItems.asStateFlow()
    private val _cashEvents = MutableStateFlow<List<CashEvent>>(emptyList())
    val cashEvents: StateFlow<List<CashEvent>> = _cashEvents.asStateFlow()
    private val _debts = MutableStateFlow<List<Debt>>(emptyList())
    val debts: StateFlow<List<Debt>> = _debts.asStateFlow()
    private val _documents = MutableStateFlow<List<Document>>(emptyList())
    val documents: StateFlow<List<Document>> = _documents.asStateFlow()
    private val _tasks = MutableStateFlow<List<TaskAction>>(emptyList())
    val tasks: StateFlow<List<TaskAction>> = _tasks.asStateFlow()

    init {
        scope.launch { loadAndStartWriter() }
    }

    private suspend fun loadAndStartWriter() {
        try {
            val prefs = withTimeout(10_000) { context.dataStore.data.first() }
            _startupStage.value = 1
            val json = prefs[snapshotKey]
            _appLockEnabled.value = prefs[appLockKey] ?: false
            _remindersEnabled.value = prefs[remindersKey] ?: true
            _isSupporter.value = prefs[supporterKey] ?: false
            _adsEnabled.value = prefs[adsEnabledKey] ?: true
            _isLocked.value = _appLockEnabled.value
            val saved = withTimeout(10_000) { store.read() } ?: json?.let { gson.fromJson(it, AppSnapshot::class.java) }
            _startupStage.value = 2
            if (saved != null) {
                require(saved.version == 1) { "Unsupported saved data version" }
                _business.value = saved.business
                _customers.value = saved.customers
                _catalogueItems.value = saved.catalogueItems
                _cashEvents.value = saved.cashEvents
                _debts.value = saved.debts
                _documents.value = saved.documents
                _tasks.value = saved.tasks
                _onboardingStep.value = saved.onboardingStep
                acceptedTermsVersion = saved.acceptedTermsVersion
                acceptedTermsDigest = saved.acceptedTermsDigest
                acceptedTermsAt = saved.acceptedTermsAt
                _termsAccepted.value = acceptedTermsVersion == LocalTerms.VERSION && acceptedTermsDigest == LocalTerms.digest
                _isOnboardingCompleted.value = saved.onboardingCompleted && saved.business != null
            } else {
                val name = prefs[businessNameKey].orEmpty()
                if (name.isNotBlank()) {
                    _business.value = Business(
                        name = name,
                        sector = prefs[businessSectorKey].orEmpty(),
                        logoPath = prefs[businessLogoKey]
                    )
                }
                _isOnboardingCompleted.value = prefs[onboardingKey] == true && _business.value != null
            }
            _startupStage.value = 3
            store.write(currentSnapshot())
            _isLoaded.value = true
            _startupStage.value = 4
            _storageError.value = null
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            Log.e("AppRepository", "Unable to load saved records", error)
            _storageError.value = "Unable to load saved records. Check device storage and try again."
            return
        }
        if (writerStarted.compareAndSet(false, true)) {
            for (snapshot in snapshots) {
                try {
                    store.write(snapshot)
                } catch (error: Exception) {
                    if (error is CancellationException) throw error
                    Log.e("AppRepository", "Unable to save records", error)
                    _storageError.value = "Unable to save records. Check device storage and try again."
                }
            }
        }
    }

    fun retryLoading() {
        if (_isLoaded.value) return
        _storageError.value = null
        scope.launch { loadAndStartWriter() }
    }

    private fun persist() {
        check(_isLoaded.value) { "Saved records have not loaded yet" }
        snapshots.trySend(currentSnapshot())
    }

    private fun currentSnapshot() = AppSnapshot(
        onboardingCompleted = _isOnboardingCompleted.value,
        onboardingStep = _onboardingStep.value,
        acceptedTermsVersion = acceptedTermsVersion,
        acceptedTermsDigest = acceptedTermsDigest,
        acceptedTermsAt = acceptedTermsAt,
        business = _business.value,
        customers = _customers.value,
        catalogueItems = _catalogueItems.value,
        cashEvents = _cashEvents.value,
        debts = _debts.value,
        documents = _documents.value,
        tasks = _tasks.value
    )

    fun saveOnboardingProgress(step: String) {
        _onboardingStep.value = step
        persist()
    }

    fun acceptTerms() {
        acceptedTermsVersion = LocalTerms.VERSION
        acceptedTermsDigest = LocalTerms.digest
        acceptedTermsAt = System.currentTimeMillis()
        _termsAccepted.value = true
        persist()
    }

    fun setAppLockEnabled(enabled: Boolean) {
        _appLockEnabled.value = enabled
        scope.launch {
            context.dataStore.edit { prefs -> prefs[appLockKey] = enabled }
        }
    }

    fun setRemindersEnabled(enabled: Boolean) {
        _remindersEnabled.value = enabled
        scope.launch {
            context.dataStore.edit { prefs -> prefs[remindersKey] = enabled }
        }
    }

    fun setAdsEnabled(enabled: Boolean) {
        _adsEnabled.value = enabled
        scope.launch {
            context.dataStore.edit { prefs -> prefs[adsEnabledKey] = enabled }
        }
    }

    fun setSupporter(enabled: Boolean) {
        _isSupporter.value = enabled
        scope.launch {
            context.dataStore.edit { prefs -> prefs[supporterKey] = enabled }
        }
    }

    fun signOut() {
        _isLocked.value = true
    }

    fun unlockApp() {
        _isLocked.value = false
    }

    fun saveBusinessProfile(name: String, sector: String = "", logoPath: String? = null) {
        require(name.isNotBlank())
        val current = _business.value ?: Business(name = name)
        _business.value = current.copy(name = name, sector = sector, logoPath = logoPath)
        _isOnboardingCompleted.value = true
        _onboardingStep.value = "COMPLETED"
        persist()
    }

    fun updateFullBusinessProfile(updated: Business) {
        _business.value = updated
        persist()
    }

    fun addCustomer(customer: Customer) {
        _customers.value = _customers.value + customer
        persist()
    }

    fun updateCustomer(customer: Customer) {
        _customers.value = _customers.value.map { if (it.id == customer.id) customer else it }
        persist()
    }

    fun archiveCustomer(customerId: String) {
        _customers.value = _customers.value.map { if (it.id == customerId) it.copy(isArchived = true) else it }
        persist()
    }

    fun addCatalogueItem(item: CatalogueItem) {
        _catalogueItems.value = _catalogueItems.value + item
        persist()
    }

    fun updateCatalogueItem(item: CatalogueItem) {
        _catalogueItems.value = _catalogueItems.value.map { if (it.id == item.id) item else it }
        persist()
    }

    fun recordReceipt(
        amountMinor: Long,
        customerName: String? = null,
        notes: String = "",
        dateStr: String = LocalDate.now().toString()
    ) {
        require(amountMinor > 0)
        val event = CashEvent(
            type = CashEventType.RECEIPT,
            amountMinor = amountMinor,
            civilDate = dateStr,
            customerName = customerName,
            notes = notes
        )
        _cashEvents.value = listOf(event) + _cashEvents.value
        persist()
    }

    fun recordExpense(
        supplier: String,
        amountMinor: Long,
        category: String = "General",
        notes: String = "",
        dateStr: String = LocalDate.now().toString()
    ) {
        require(amountMinor > 0)
        val event = CashEvent(
            type = CashEventType.EXPENSE,
            amountMinor = amountMinor,
            civilDate = dateStr,
            category = category,
            customerName = supplier,
            notes = notes
        )
        _cashEvents.value = listOf(event) + _cashEvents.value
        persist()
    }

    fun recordDebt(
        customerId: String,
        customerName: String,
        amountMinor: Long,
        dueDate: String? = null,
        notes: String = "",
        dateStr: String = LocalDate.now().toString(),
        origin: DebtOrigin = DebtOrigin.MANUAL,
        invoiceId: String? = null,
        save: Boolean = true
    ): Debt {
        require(amountMinor > 0)
        val debt = Debt(
            customerId = customerId,
            customerName = customerName,
            origin = origin,
            invoiceId = invoiceId,
            principalMinor = amountMinor,
            incurredDate = dateStr,
            dueDate = dueDate,
            notes = notes
        )
        _debts.value = listOf(debt) + _debts.value
        if (!dueDate.isNullOrEmpty()) {
            _tasks.value = listOf(
                TaskAction(
                    type = TaskType.DUE_DEBT,
                    title = "Payment due: $customerName",
                    subtitle = "${formatMoneyMinor(amountMinor)} due on $dueDate",
                    dueDate = dueDate,
                    customerId = customerId,
                    linkedId = debt.id
                )
            ) + _tasks.value
        }
        if (save) persist()
        return debt
    }

    fun settleDebt(
        debtId: String,
        settlementAmountMinor: Long,
        paymentMethod: String = "Cash",
        dateStr: String = LocalDate.now().toString()
    ) {
        val target = _debts.value.firstOrNull { it.id == debtId } ?: return
        if (settlementAmountMinor <= 0 || settlementAmountMinor > target.remainingMinor) return
        _debts.value = _debts.value.map { debt ->
            if (debt.id == debtId) debt.copy(settledMinor = debt.settledMinor + settlementAmountMinor) else debt
        }
        _cashEvents.value = listOf(
            CashEvent(
                type = CashEventType.SETTLEMENT,
                amountMinor = settlementAmountMinor,
                civilDate = dateStr,
                paymentMethod = paymentMethod,
                customerId = target.customerId,
                debtId = target.id,
                customerName = target.customerName,
                notes = "Debt settlement payment"
            )
        ) + _cashEvents.value
        if (settlementAmountMinor == target.remainingMinor) {
            _tasks.value = _tasks.value.map { if (it.linkedId == debtId) it.copy(isCompleted = true) else it }
        }
        persist()
    }

    fun createDocument(doc: Document): Document {
        val business = _business.value
        var finalDoc = doc
        if (doc.status == DocumentStatus.ISSUED && doc.number.isEmpty() && business != null) {
            val number = when (doc.type) {
                DocumentType.QUOTE -> "${business.quotePrefix}${business.nextQuoteNumber}"
                DocumentType.INVOICE -> "${business.invoicePrefix}${business.nextInvoiceNumber}"
                DocumentType.CREDIT_NOTE -> "${business.creditNotePrefix}${business.nextCreditNoteNumber}"
            }
            finalDoc = doc.copy(number = number)
            _business.value = when (doc.type) {
                DocumentType.QUOTE -> business.copy(nextQuoteNumber = business.nextQuoteNumber + 1)
                DocumentType.INVOICE -> business.copy(nextInvoiceNumber = business.nextInvoiceNumber + 1)
                DocumentType.CREDIT_NOTE -> business.copy(nextCreditNoteNumber = business.nextCreditNoteNumber + 1)
            }
            if (doc.type == DocumentType.INVOICE) {
                recordDebt(
                    customerId = doc.customerId,
                    customerName = doc.customerName,
                    amountMinor = doc.totalGrossMinor,
                    dueDate = doc.dueDate,
                    notes = "Linked to Invoice $number",
                    dateStr = doc.issueDate,
                    origin = DebtOrigin.INVOICE,
                    invoiceId = finalDoc.id,
                    save = false
                )
            }
        }
        _documents.value = listOf(finalDoc) + _documents.value
        persist()
        return finalDoc
    }

    fun completeTask(taskId: String) {
        _tasks.value = _tasks.value.map { if (it.id == taskId) it.copy(isCompleted = true) else it }
        persist()
    }

    fun snoozeTask(taskId: String) {
        _tasks.value = _tasks.value.map { if (it.id == taskId) it.copy(isSnoozed = true) else it }
        persist()
    }

    private fun resolvePeriod(filter: PeriodFilter, customStart: String?, customEnd: String?): LedgerPeriod {
        val today = LocalDate.now()
        return when (filter) {
            PeriodFilter.TODAY -> LedgerPeriod(today, today.plusDays(1))
            PeriodFilter.THIS_WEEK -> {
                val start = today.with(DayOfWeek.MONDAY)
                LedgerPeriod(start, start.plusWeeks(1))
            }
            PeriodFilter.THIS_MONTH -> {
                val start = today.withDayOfMonth(1)
                LedgerPeriod(start, start.plusMonths(1))
            }
            PeriodFilter.LAST_MONTH -> {
                val start = today.withDayOfMonth(1).minusMonths(1)
                LedgerPeriod(start, today.withDayOfMonth(1))
            }
            PeriodFilter.CUSTOM -> {
                val parsedStart = customStart?.let { runCatching { LocalDate.parse(it) }.getOrNull() } ?: today
                val parsedEnd = customEnd?.let { runCatching { LocalDate.parse(it) }.getOrNull() } ?: today
                val start = if (parsedStart <= parsedEnd) parsedStart else parsedEnd
                LedgerPeriod(start, parsedEnd.plusDays(1))
            }
        }
    }

    fun getFinancialSummary(
        filter: PeriodFilter,
        customStart: String? = null,
        customEnd: String? = null
    ): FinancialSummary {
        val range = resolvePeriod(filter, customStart, customEnd)
        val start = range.start.toString()
        val end = range.endExclusive.toString()
        val today = LocalDate.now()
        val asOf = minOf(today, range.endExclusive.minusDays(1)).toString()
        val events = _cashEvents.value.filter { it.civilDate >= start && it.civilDate < end }
        val received = events.filter { it.type == CashEventType.RECEIPT || it.type == CashEventType.SETTLEMENT }
            .sumOf { it.amountMinor }
        val spent = events.filter { it.type == CashEventType.EXPENSE }.sumOf { it.amountMinor }
        val refunds = events.filter { it.type == CashEventType.REFUND }.sumOf { it.amountMinor }
        val ownerTransfers = events.filter { it.type == CashEventType.OWNER_IN || it.type == CashEventType.OWNER_OUT }
            .sumOf { if (it.type == CashEventType.OWNER_IN) it.amountMinor else -it.amountMinor }
        val outstanding = _debts.value.sumOf { debt ->
            if (debt.incurredDate > asOf) {
                0L
            } else {
                val settledByAsOf = _cashEvents.value.filter {
                    it.type == CashEventType.SETTLEMENT && it.debtId == debt.id && it.civilDate <= asOf
                }.sumOf { it.amountMinor }
                (debt.principalMinor - settledByAsOf).coerceAtLeast(0L)
            }
        }
        return FinancialSummary(
            moneyReceivedMinor = received,
            moneySpentMinor = spent,
            netMovementMinor = received - spent - refunds,
            refundMinor = refunds,
            ownerTransferMinor = ownerTransfers,
            debtOwedMinor = outstanding,
            periodStart = start,
            periodEndExclusive = end,
            asOfDate = asOf,
            periodLabel = periodLabel(filter)
        )
    }

    fun getDashboardData(
        filter: PeriodFilter,
        customStart: String? = null,
        customEnd: String? = null
    ): DashboardData {
        val summary = getFinancialSummary(filter, customStart, customEnd)
        val today = LocalDate.now().toString()
        val attention = buildAttention(today)
        val activity = buildRecentActivity()
        return DashboardData(
            summary = summary,
            periodLabel = summary.periodLabel,
            debtAsOfDate = summary.asOfDate,
            attention = attention,
            recentActivity = activity,
            hasActivity = _cashEvents.value.isNotEmpty() || _debts.value.isNotEmpty() || _documents.value.isNotEmpty(),
            isEmpty = _cashEvents.value.isEmpty() && _debts.value.isEmpty() && _documents.value.isEmpty()
        )
    }

    private fun periodLabel(filter: PeriodFilter): String = when (filter) {
        PeriodFilter.TODAY -> "Today"
        PeriodFilter.THIS_WEEK -> "This week"
        PeriodFilter.THIS_MONTH -> "This month"
        PeriodFilter.LAST_MONTH -> "Last month"
        PeriodFilter.CUSTOM -> "Custom period"
    }

    private fun buildAttention(today: String): List<AttentionItem> {
        val items = mutableListOf<AttentionItem>()
        val activeTasks = _tasks.value.filter { !it.isCompleted && !it.isSnoozed && it.dueDate <= today }
        activeTasks.forEach { task ->
            items += AttentionItem(
                id = task.id,
                source = AttentionSource.TASK,
                sourceId = task.id,
                title = task.title,
                subtitle = task.subtitle,
                dueDate = task.dueDate,
                isOverdue = task.dueDate < today,
                targetTab = if (task.type == TaskType.DUE_DEBT) 2 else 3
            )
        }
        val coveredDebtIds = activeTasks.mapNotNull { it.linkedId }.toSet()
        _debts.value.forEach { debt ->
            val due = debt.dueDate ?: return@forEach
            if (debt.remainingMinor > 0 && due <= today && debt.id !in coveredDebtIds) {
                items += AttentionItem(
                    id = "debt-${debt.id}",
                    source = AttentionSource.DEBT,
                    sourceId = debt.id,
                    title = "Payment overdue: ${debt.customerName}",
                    subtitle = "${formatMoneyMinor(debt.remainingMinor)} was due $due",
                    dueDate = due,
                    isOverdue = due < today,
                    targetTab = 2
                )
            }
        }
        _documents.value.forEach { doc ->
            val due = doc.dueDate ?: return@forEach
            val isQuote = doc.type == DocumentType.QUOTE
            val isOpen = doc.status == DocumentStatus.ISSUED || doc.status == DocumentStatus.SENT
            if (isQuote && isOpen && due <= today) {
                items += AttentionItem(
                    id = "quote-${doc.id}",
                    source = AttentionSource.QUOTE,
                    sourceId = doc.id,
                    title = "Follow up: quote ${doc.number.ifBlank { doc.customerName }}",
                    subtitle = "Quote due $due for ${doc.customerName}",
                    dueDate = due,
                    isOverdue = due < today,
                    targetTab = 3
                )
            }
        }
        return items.sortedBy { it.dueDate }
    }

    private fun buildRecentActivity(): List<ActivityItem> {
        val dated = mutableListOf<Pair<Long, ActivityItem>>()
        _cashEvents.value.forEach { event ->
            val item = when (event.type) {
                CashEventType.RECEIPT -> ActivityItem(
                    id = event.id, type = ActivityType.MONEY_IN, title = "Money in",
                    subtitle = event.customerName ?: event.category, date = event.civilDate,
                    amountMinor = event.amountMinor, targetTab = 2
                )
                CashEventType.SETTLEMENT -> ActivityItem(
                    id = event.id, type = ActivityType.MONEY_IN, title = "Debt payment",
                    subtitle = event.customerName.orEmpty(), date = event.civilDate,
                    amountMinor = event.amountMinor, targetTab = 2
                )
                CashEventType.EXPENSE -> ActivityItem(
                    id = event.id, type = ActivityType.MONEY_OUT, title = "Money out",
                    subtitle = event.customerName ?: event.category, date = event.civilDate,
                    amountMinor = -event.amountMinor, targetTab = 2
                )
                CashEventType.REFUND -> ActivityItem(
                    id = event.id, type = ActivityType.MONEY_OUT, title = "Refund",
                    subtitle = event.customerName.orEmpty(), date = event.civilDate,
                    amountMinor = -event.amountMinor, targetTab = 2
                )
                CashEventType.OWNER_IN, CashEventType.OWNER_OUT -> return@forEach
            }
            dated += sortKey(event.civilDate, event.createdAt) to item
        }
        _debts.value.forEach { debt ->
            dated += sortKey(debt.incurredDate, debt.createdAt) to ActivityItem(
                id = debt.id,
                type = ActivityType.DEBT,
                title = "Debt recorded",
                subtitle = debt.customerName,
                date = debt.incurredDate,
                amountMinor = debt.principalMinor,
                targetTab = 2
            )
        }
        _documents.value.forEach { doc ->
            dated += sortKey(doc.issueDate, doc.createdAt) to ActivityItem(
                id = doc.id,
                type = ActivityType.DOCUMENT,
                title = "Document ${doc.number.ifBlank { doc.type.name.lowercase() }}",
                subtitle = doc.customerName,
                date = doc.issueDate,
                amountMinor = doc.totalGrossMinor,
                targetTab = 3
            )
        }
        return dated.sortedByDescending { it.first }.take(5).map { it.second }
    }

    private fun sortKey(date: String, createdAt: Long): Long {
        val day = runCatching { LocalDate.parse(date).toEpochDay() }.getOrDefault(0L)
        return maxOf(createdAt, day * 86_400_000L)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null

        fun getInstance(context: Context): AppRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: AppRepository(context.applicationContext).also { INSTANCE = it }
        }
    }
}
