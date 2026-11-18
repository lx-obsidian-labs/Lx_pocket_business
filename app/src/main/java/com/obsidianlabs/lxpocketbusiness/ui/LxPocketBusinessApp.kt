package com.obsidianlabs.lxpocketbusiness.ui

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import com.obsidianlabs.lxpocketbusiness.ads.AdIds
import com.obsidianlabs.lxpocketbusiness.ads.InterstitialAdManager
import com.obsidianlabs.lxpocketbusiness.ads.RewardedAdManager
import com.obsidianlabs.lxpocketbusiness.ads.rememberAdConsent
import com.obsidianlabs.lxpocketbusiness.data.model.Document
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentType
import com.obsidianlabs.lxpocketbusiness.data.model.VatStatus
import com.obsidianlabs.lxpocketbusiness.data.repository.AppRepository
import com.obsidianlabs.lxpocketbusiness.data.repository.PeriodFilter
import com.obsidianlabs.lxpocketbusiness.security.LocalLockManager
import com.obsidianlabs.lxpocketbusiness.service.PdfExportService
import com.obsidianlabs.lxpocketbusiness.ui.components.AppBottomBar
import com.obsidianlabs.lxpocketbusiness.ui.dialogs.AddCatalogueItemDialog
import com.obsidianlabs.lxpocketbusiness.ui.dialogs.AddCustomerDialog
import com.obsidianlabs.lxpocketbusiness.ui.dialogs.AddTransactionDialog
import com.obsidianlabs.lxpocketbusiness.ui.dialogs.SettleDebtDialog
import com.obsidianlabs.lxpocketbusiness.ui.dialogs.TransactionMode
import com.obsidianlabs.lxpocketbusiness.ui.screens.BusinessSetupScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.CustomersScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.DocumentEditorScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.DocumentsScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.FeatureCarouselScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.HomeScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.InfoScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.LockedScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.MoneyLedgerScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.MoreScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.ProfileScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.SettingsScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.SplashScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.TermsScreen
import com.obsidianlabs.lxpocketbusiness.ui.screens.WelcomeScreen
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackground
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxPocketBusinessTheme
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary

enum class OnboardingState {
    SPLASH,
    WELCOME,
    CAROUSEL,
    TERMS,
    SETUP,
    COMPLETED
}

private data class AppInfoScreen(val title: String, val paragraphs: List<String>)

@Composable
fun LxPocketBusinessApp(repository: AppRepository) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val isOnboardingCompleted by repository.isOnboardingCompleted.collectAsState()
    val isLoaded by repository.isLoaded.collectAsState()
    val storageError by repository.storageError.collectAsState()
    val startupStage by repository.startupStage.collectAsState()
    val savedStep by repository.onboardingStep.collectAsState()
    val business by repository.business.collectAsState()
    val customers by repository.customers.collectAsState()
    val cashEvents by repository.cashEvents.collectAsState()
    val debts by repository.debts.collectAsState()
    val documents by repository.documents.collectAsState()
    val tasks by repository.tasks.collectAsState()
    val appLockEnabled by repository.appLockEnabled.collectAsState()
    val isLocked by repository.isLocked.collectAsState()
    val remindersEnabled by repository.remindersEnabled.collectAsState()
    val adsEnabled by repository.adsEnabled.collectAsState()
    val isSupporter by repository.isSupporter.collectAsState()
    val deviceLockAvailable = remember { LocalLockManager(context).isDeviceCredentialAvailable() }

    var onboardingStep by rememberSaveable { mutableStateOf(OnboardingState.SPLASH) }
    var onboardingInitialized by rememberSaveable { mutableStateOf(false) }
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var selectedPeriod by rememberSaveable { mutableStateOf(PeriodFilter.THIS_MONTH) }
    var customStart by rememberSaveable { mutableStateOf<String?>(null) }
    var customEnd by rememberSaveable { mutableStateOf<String?>(null) }
    var isEditingProfile by rememberSaveable { mutableStateOf(false) }
    var isSettingsOpen by rememberSaveable { mutableStateOf(false) }
    var showTermsReadOnly by rememberSaveable { mutableStateOf(false) }
    var infoScreen by remember { mutableStateOf<AppInfoScreen?>(null) }

    var activeTransactionMode by remember { mutableStateOf<TransactionMode?>(null) }
    var showAddCustomerDialog by remember { mutableStateOf(false) }
    var showAddCatalogueItemDialog by remember { mutableStateOf(false) }
    var settlingDebtTarget by remember { mutableStateOf<com.obsidianlabs.lxpocketbusiness.data.model.Debt?>(null) }
    var isCreatingDocument by rememberSaveable { mutableStateOf(false) }
    var documentTypeForCreation by rememberSaveable { mutableStateOf(DocumentType.INVOICE) }
    var showSignOutDialog by remember { mutableStateOf(false) }
        var consentRefresh by remember { mutableIntStateOf(0) }
    var consentReset by remember { mutableIntStateOf(0) }

    val dashboardData
 = repository.getDashboardData(selectedPeriod, customStart, customEnd)
    val isAppReady = isOnboardingCompleted || onboardingStep == OnboardingState.COMPLETED
    val hasOverlay = isEditingProfile || isSettingsOpen || showTermsReadOnly || infoScreen != null || isCreatingDocument
    val adsAvailable = adsEnabled && isAppReady && !isLocked && !hasOverlay
    val canRequestAds = rememberAdConsent(
        enabled = adsAvailable,
        refreshKey = consentRefresh,
        resetKey = consentReset
    )

    LaunchedEffect(isLoaded, savedStep, isOnboardingCompleted) {
        if (isLoaded && !onboardingInitialized) {
            onboardingStep = if (isOnboardingCompleted) {
                OnboardingState.COMPLETED
            } else {
                runCatching { OnboardingState.valueOf(savedStep) }.getOrDefault(OnboardingState.WELCOME)
            }
            onboardingInitialized = true
        }
    }

    DisposableEffect(lifecycle, repository) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_STOP && repository.appLockEnabled.value) {
                repository.signOut()
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    fun goToStep(step: OnboardingState) {
        onboardingStep = step
        if (!isOnboardingCompleted) repository.saveOnboardingProgress(step.name)
    }

    fun showInfo(title: String, vararg paragraphs: String) {
        infoScreen = AppInfoScreen(title, paragraphs.toList())
    }

    fun showAdAfterSuccess() {
        if (canRequestAds && context is Activity) {
            InterstitialAdManager.showIfReady(context) {}
        }
    }

    fun showRewardedSupport() {
        if (!canRequestAds) {
            Toast.makeText(context, "Enable ads to watch a supporter ad.", Toast.LENGTH_SHORT).show()
            return
        }
        val activity = context as? Activity ?: return
        val shown = RewardedAdManager.show(
            activity = activity,
            onReward = { repository.setSupporter(true) },
            onClosed = {}
        )
        if (!shown) {
            RewardedAdManager.load(activity)
            Toast.makeText(context, "A supporter ad is not ready yet. Try again shortly.", Toast.LENGTH_SHORT).show()
        }
    }

    BackHandler(enabled = hasOverlay || (isAppReady && !isLocked && selectedTab != 0)) {
        when {
            isCreatingDocument -> isCreatingDocument = false
            infoScreen != null -> infoScreen = null
            showTermsReadOnly -> showTermsReadOnly = false
            isSettingsOpen -> isSettingsOpen = false
            isEditingProfile -> isEditingProfile = false
            else -> selectedTab = 0
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = LxBackground,
        bottomBar = {
            if (isAppReady && !isLocked && !hasOverlay) {
                AppBottomBar(selectedTab, onTabSelected = { selectedTab = it })
            }
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                storageError != null -> {
                    Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(storageError.orEmpty(), color = LxTextSecondary)
                            Button(onClick = { repository.retryLoading() }, modifier = Modifier.padding(top = 16.dp)) {
                                Text("Retry")
                            }
                        }
                    }
                }
                !isLoaded -> {
                    SplashScreen(completedMilestones = startupStage, isReady = false, onSplashFinished = {})
                }
                isAppReady && isLocked -> {
                    LockedScreen(onUnlocked = repository::unlockApp)
                }
                !isAppReady -> {
                    when (onboardingStep) {
                        OnboardingState.SPLASH -> SplashScreen(
                            completedMilestones = 4,
                            isReady = true,
                            onSplashFinished = { goToStep(OnboardingState.WELCOME) }
                        )
                        OnboardingState.WELCOME -> WelcomeScreen(
                            onGetStartedClick = { goToStep(OnboardingState.CAROUSEL) }
                        )
                        OnboardingState.CAROUSEL -> FeatureCarouselScreen(
                            onBackClick = { goToStep(OnboardingState.WELCOME) },
                            onNextClick = { goToStep(OnboardingState.TERMS) },
                            onSkipClick = { goToStep(OnboardingState.TERMS) }
                        )
                        OnboardingState.TERMS -> TermsScreen(
                            onBackClick = { goToStep(OnboardingState.CAROUSEL) },
                            onContinueClick = {
                                repository.acceptTerms()
                                goToStep(OnboardingState.SETUP)
                            }
                        )
                        OnboardingState.SETUP -> BusinessSetupScreen(
                            onBackClick = { goToStep(OnboardingState.TERMS) },
                            onCreateBusinessClick = { name, sector ->
                                repository.saveBusinessProfile(name, sector)
                                goToStep(OnboardingState.COMPLETED)
                            }
                        )
                        OnboardingState.COMPLETED -> {}
                    }
                }
                isEditingProfile -> {
                    ProfileScreen(
                        business = business,
                        onBackClick = { isEditingProfile = false },
                        onSave = { updated ->
                            repository.updateFullBusinessProfile(updated)
                            isEditingProfile = false
                            Toast.makeText(context, "Business profile saved.", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                isSettingsOpen -> {
                    SettingsScreen(
                        business = business,
                        appLockEnabled = appLockEnabled,
                        deviceLockAvailable = deviceLockAvailable,
                        remindersEnabled = remindersEnabled,
                        adsEnabled = adsEnabled,
                        isSupporter = isSupporter,
                        onBackClick = { isSettingsOpen = false },
                        onOpenProfile = { isEditingProfile = true; isSettingsOpen = false },
                        onOpenCatalogue = { showAddCatalogueItemDialog = true },
                        onOpenReports = {
                            showInfo(
                                "Monthly reports",
                                "Detailed report generation is not implemented in this build.",
                                "Use Home and Money to review actual records for a chosen period. Net cash movement is not profit."
                            )
                        },
                        onOpenReminders = {
                            showInfo(
                                "Payment reminders",
                                "Reminders come from real due debts, open quotes and tasks in your workspace.",
                                "Notification prompts are only requested after notifications are enabled in Settings."
                            )
                        },
                        onOpenBackup = {
                            showInfo(
                                "Backup and data export",
                                "Encrypted local backup and structured export are not implemented in this build.",
                                "Records are stored in this app's private local workspace."
                            )
                        },
                        onOpenLegal = { showTermsReadOnly = true },
                        onToggleAppLock = repository::setAppLockEnabled,
                        onToggleReminders = repository::setRemindersEnabled,
                        onToggleAds = { enabled ->
                            repository.setAdsEnabled(enabled)
                            if (enabled) consentRefresh++
                        },
                        onResetAdConsent = {
                            repository.setAdsEnabled(true)
                            consentReset++
                        },
                        onOpenSupport = ::showRewardedSupport,
                        onSignOut = { showSignOutDialog = true }
                    )
                }
                showTermsReadOnly -> TermsScreen(readOnly = true, onBackClick = { showTermsReadOnly = false })
                infoScreen != null -> {
                    val info = infoScreen!!
                    InfoScreen(info.title, info.paragraphs, onBackClick = { infoScreen = null })
                }
                isCreatingDocument -> {
                    DocumentEditorScreen(
                        customers = customers,
                        allowVat = business?.vatStatus == VatStatus.VENDOR,
                        initialType = documentTypeForCreation,
                        onBackClick = { isCreatingDocument = false },
                        onSubmitDocument = { newDoc: Document ->
                            val issuedDoc = repository.createDocument(newDoc)
                            isCreatingDocument = false
                            business?.let { b ->
                                val pdf = PdfExportService.generateDocumentPdf(context, issuedDoc, b)
                                Toast.makeText(context, "Document PDF generated: ${pdf.name}", Toast.LENGTH_LONG).show()
                            }
                            showAdAfterSuccess()
                        }
                    )
                }
                else -> {
                    Box(Modifier.fillMaxSize()) {
                        when (selectedTab) {
                            0 -> HomeScreen(
                                business = business,
                                data = dashboardData,
                                selectedPeriod = selectedPeriod,
                                onPeriodSelected = { selectedPeriod = it },
                                onCustomRangeSelected = { start, end ->
                                    customStart = start
                                    customEnd = end
                                },
                                onMoneyInClick = { activeTransactionMode = TransactionMode.MONEY_IN },
                                onMoneyOutClick = { activeTransactionMode = TransactionMode.MONEY_OUT },
                                onRecordDebtClick = { activeTransactionMode = TransactionMode.SOMEONE_OWES_ME },
                                onAttentionClick = { item -> selectedTab = item.targetTab },
                                onCompleteTask = repository::completeTask,
                                onSnoozeTask = repository::snoozeTask,
                                onActivityClick = { item -> selectedTab = item.targetTab },
                                onViewAllAttentionClick = { selectedTab = 2 },
                                onViewAllActivityClick = { selectedTab = 2 },
                                onNotificationsClick = { selectedTab = 2 },
                                onSettingsClick = { isSettingsOpen = true },
                                onRecordTransactionClick = { activeTransactionMode = TransactionMode.MONEY_IN },
                                adsReady = canRequestAds
                            )
                            1 -> CustomersScreen(
                                customers = customers,
                                debts = debts,
                                onCustomerClick = {},
                                onAddCustomerClick = { showAddCustomerDialog = true },
                                onArchiveCustomer = { customer -> repository.archiveCustomer(customer.id) }
                            )
                            2 -> MoneyLedgerScreen(
                                summary = dashboardData.summary,
                                cashEvents = cashEvents,
                                debts = debts,
                                selectedPeriod = selectedPeriod,
                                onPeriodSelected = { selectedPeriod = it },
                                onRecordReceiptClick = { activeTransactionMode = TransactionMode.MONEY_IN },
                                onRecordExpenseClick = { activeTransactionMode = TransactionMode.MONEY_OUT },
                                onRecordDebtClick = { activeTransactionMode = TransactionMode.SOMEONE_OWES_ME },
                                onSettleDebtClick = { debt -> settlingDebtTarget = debt }
                            )
                            3 -> DocumentsScreen(
                                documents = documents,
                                isVatVendor = business?.vatStatus == VatStatus.VENDOR,
                                onNewDocumentClick = { type ->
                                    documentTypeForCreation = type
                                    isCreatingDocument = true
                                },
                                onDocumentClick = { doc ->
                                    business?.let { b ->
                                        val pdf = PdfExportService.generateDocumentPdf(context, doc, b)
                                        Toast.makeText(context, "Opening PDF: ${pdf.name}", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                adsReady = canRequestAds
                            )
                            4 -> MoreScreen(
                                business = business,
                                appLockEnabled = appLockEnabled,
                                remindersEnabled = remindersEnabled,
                                isSupporter = isSupporter,
                                onOpenProfileClick = { isEditingProfile = true },
                                onOpenSettingsClick = { isSettingsOpen = true },
                                onOpenCatalogueClick = { showAddCatalogueItemDialog = true },
                                onOpenReportsClick = {
                                    showInfo(
                                        "Monthly reports",
                                        "Detailed report generation is not implemented in this build.",
                                        "Use Home and Money to review actual records for a chosen period."
                                    )
                                },
                                onOpenRemindersClick = {
                                    showInfo(
                                        "Payment reminders",
                                        "Reminders come from real due debts, open quotes and tasks in your workspace."
                                    )
                                },
                                onOpenBackupClick = {
                                    showInfo(
                                        "Backup and data export",
                                        "Encrypted local backup and structured export are not implemented in this build."
                                    )
                                },
                                onOpenLegalClick = { showTermsReadOnly = true },
                                onOpenHelpClick = {
                                    showInfo(
                                        "Offline help",
                                        "Record money received or spent from Home or Money.",
                                        "Use Record debt for customer balances. A debt becomes cash only after a settlement is recorded.",
                                        "Create quotes and invoices from Documents. Templates are not SARS-certified."
                                    )
                                },
                                onOpenSupportClick = ::showRewardedSupport,
                                onSignOutClick = { showSignOutDialog = true }
                            )
                        }
                    }
                }
            }

            activeTransactionMode?.let { mode ->
                AddTransactionDialog(
                    initialMode = mode,
                    onDismiss = { activeTransactionMode = null },
                    onSubmitReceipt = { amount, name, notes ->
                        repository.recordReceipt(amount, name, notes)
                        Toast.makeText(context, "Receipt recorded successfully!", Toast.LENGTH_SHORT).show()
                        showAdAfterSuccess()
                    },
                    onSubmitExpense = { supplier, amount, category, notes ->
                        repository.recordExpense(supplier, amount, category, notes)
                        Toast.makeText(context, "Expense recorded successfully!", Toast.LENGTH_SHORT).show()
                        showAdAfterSuccess()
                    },
                    onSubmitDebt = { customerName, amount, dueDate, notes ->
                        val name = customerName.trim()
                        val customer = customers.firstOrNull { it.displayName.equals(name, ignoreCase = true) }
                            ?: com.obsidianlabs.lxpocketbusiness.data.model.Customer(displayName = name)
                                .also(repository::addCustomer)
                        repository.recordDebt(customer.id, customer.displayName, amount, dueDate, notes)
                        Toast.makeText(context, "Debt recorded successfully!", Toast.LENGTH_SHORT).show()
                        showAdAfterSuccess()
                    }
                )
            }

            if (showAddCustomerDialog) {
                AddCustomerDialog(
                    onDismiss = { showAddCustomerDialog = false },
                    onSubmitCustomer = { newCustomer ->
                        repository.addCustomer(newCustomer)
                        Toast.makeText(context, "Customer saved!", Toast.LENGTH_SHORT).show()
                        showAdAfterSuccess()
                    }
                )
            }

            if (showAddCatalogueItemDialog) {
                AddCatalogueItemDialog(
                    onDismiss = { showAddCatalogueItemDialog = false },
                    onSubmitItem = { item ->
                        repository.addCatalogueItem(item)
                        Toast.makeText(context, "Catalogue item added!", Toast.LENGTH_SHORT).show()
                        showAdAfterSuccess()
                    }
                )
            }

            settlingDebtTarget?.let { debt ->
                SettleDebtDialog(
                    debt = debt,
                    onDismiss = { settlingDebtTarget = null },
                    onSubmitSettlement = { debtId, amount, method ->
                        repository.settleDebt(debtId, amount, method)
                        Toast.makeText(context, "Settlement recorded!", Toast.LENGTH_SHORT).show()
                        showAdAfterSuccess()
                    }
                )
            }

            if (showSignOutDialog) {
                AlertDialog(
                    onDismissRequest = { showSignOutDialog = false },
                    title = { Text("Sign out?") },
                    text = {
                        Text(
                            "This locks LX Pocket Business and requires your Android screen lock to reopen. " +
                                "Records stay on this device."
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            repository.signOut()
                            showSignOutDialog = false
                        }) { Text("Sign out") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showSignOutDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@Preview(name = "Splash Screen", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewSplashScreen() {
    LxPocketBusinessTheme { SplashScreen() }
}

@Preview(name = "Welcome Screen", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewWelcomeScreen() {
    LxPocketBusinessTheme { WelcomeScreen() }
}

@Preview(name = "Carousel Screen", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewCarouselScreen() {
    LxPocketBusinessTheme { FeatureCarouselScreen() }
}

@Preview(name = "Terms Screen", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewTermsScreen() {
    LxPocketBusinessTheme { TermsScreen() }
}

@Preview(name = "Business Setup Screen", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewBusinessSetupScreen() {
    LxPocketBusinessTheme { BusinessSetupScreen() }
}

@Preview(name = "Home Screen", showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PreviewHomeScreen() {
    LxPocketBusinessTheme { HomeScreen() }
}
