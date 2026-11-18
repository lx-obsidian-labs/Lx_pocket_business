package com.obsidianlabs.lxpocketbusiness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.obsidianlabs.lxpocketbusiness.data.model.CashEvent
import com.obsidianlabs.lxpocketbusiness.data.model.CashEventType
import com.obsidianlabs.lxpocketbusiness.data.model.Debt
import com.obsidianlabs.lxpocketbusiness.data.model.formatMoneyMinor
import com.obsidianlabs.lxpocketbusiness.data.repository.FinancialSummary
import com.obsidianlabs.lxpocketbusiness.data.repository.PeriodFilter
import com.obsidianlabs.lxpocketbusiness.ui.components.LedgerEmptyArt
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAppBackground
import com.obsidianlabs.lxpocketbusiness.ui.components.LxCard
import com.obsidianlabs.lxpocketbusiness.ui.components.LxEmptyState
import com.obsidianlabs.lxpocketbusiness.ui.components.LxFilterChip
import com.obsidianlabs.lxpocketbusiness.ui.components.LxIconTile
import com.obsidianlabs.lxpocketbusiness.ui.components.LxScreenHeader
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSpacing
import com.obsidianlabs.lxpocketbusiness.ui.components.LxStatusPill
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyInGreen
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyOutRed
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxWarning
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private enum class LedgerFilter(val label: String) {
    ALL("All"),
    MONEY_IN("Money in"),
    MONEY_OUT("Money out"),
    DEBT("Debt"),
    TRANSFERS("Transfers")
}

private sealed interface LedgerEntry {
    data class Header(val label: String) : LedgerEntry
    data class Cash(val event: CashEvent) : LedgerEntry
    data class DebtEntry(val debt: Debt) : LedgerEntry
}

@Composable
fun MoneyLedgerScreen(
    summary: FinancialSummary,
    cashEvents: List<CashEvent>,
    debts: List<Debt>,
    selectedPeriod: PeriodFilter = PeriodFilter.THIS_MONTH,
    onPeriodSelected: (PeriodFilter) -> Unit = {},
    onRecordReceiptClick: () -> Unit,
    onRecordExpenseClick: () -> Unit,
    onRecordDebtClick: () -> Unit,
    onSettleDebtClick: (Debt) -> Unit
) {
    var filter by remember { mutableStateOf(LedgerFilter.ALL) }
    var periodExpanded by remember { mutableStateOf(false) }

    val periodEvents = cashEvents.filter { event ->
        event.civilDate >= summary.periodStart && event.civilDate < summary.periodEndExclusive
    }
    val activeDebts = debts.filter { it.remainingMinor > 0 && it.incurredDate <= summary.asOfDate }

    val entries = buildList {
        if (filter == LedgerFilter.ALL || filter == LedgerFilter.MONEY_IN || filter == LedgerFilter.MONEY_OUT || filter == LedgerFilter.TRANSFERS) {
            val visibleEvents = periodEvents.filter { event ->
                when (filter) {
                    LedgerFilter.MONEY_IN -> event.type == CashEventType.RECEIPT || event.type == CashEventType.SETTLEMENT
                    LedgerFilter.MONEY_OUT -> event.type == CashEventType.EXPENSE || event.type == CashEventType.REFUND
                    LedgerFilter.TRANSFERS -> event.type == CashEventType.OWNER_IN || event.type == CashEventType.OWNER_OUT
                    else -> true
                }
            }
            visibleEvents.groupBy { it.civilDate }.toSortedMap(compareByDescending { it }).forEach { (date, events) ->
                add(LedgerEntry.Header(displayDate(date)))
                events.forEach { add(LedgerEntry.Cash(it)) }
            }
        }
        if (filter == LedgerFilter.ALL || filter == LedgerFilter.DEBT) {
            if (activeDebts.isNotEmpty()) {
                add(LedgerEntry.Header("Active debts (as of ${displayDate(summary.asOfDate)})"))
                activeDebts.forEach { add(LedgerEntry.DebtEntry(it)) }
            }
        }
    }

    LxAppBackground {
        Column(Modifier.fillMaxSize().padding(horizontal = LxSpacing.lg, vertical = LxSpacing.lg)) {
            LxScreenHeader(
                title = "Money",
                subtitle = "Cash movements and what is still owed",
                actions = {
                    Box {
                        Row(
                            Modifier
                                .padding(LxSpacing.xs)
                                .clickable { periodExpanded = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(periodLabel(selectedPeriod), style = LxType.button, color = LxLime)
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Choose period", tint = LxLime)
                        }
                        DropdownMenu(
                            expanded = periodExpanded,
                            onDismissRequest = { periodExpanded = false },
                            modifier = Modifier.background(LxCardBg)
                        ) {
                            listOf(PeriodFilter.TODAY, PeriodFilter.THIS_WEEK, PeriodFilter.THIS_MONTH, PeriodFilter.LAST_MONTH).forEach { period ->
                                DropdownMenuItem(
                                    text = { Text(periodLabel(period), color = LxTextPrimary) },
                                    onClick = { onPeriodSelected(period); periodExpanded = false }
                                )
                            }
                        }
                    }
                }
            )
            Spacer(Modifier.height(LxSpacing.lg))

            LxCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(LxSpacing.lg)) {
                    Text(displayDateRange(summary.periodStart, summary.asOfDate), style = LxType.caption, color = LxTextSecondary)
                    Spacer(Modifier.height(LxSpacing.md))
                    Row {
                        Column(Modifier.weight(1f)) {
                            Text("Received", style = LxType.caption, color = LxTextSecondary)
                            Text(summary.formattedMoneyReceived, style = LxType.financialSmall, color = LxMoneyInGreen)
                        }
                        Column(Modifier.weight(1f)) {
                            Text("Spent", style = LxType.caption, color = LxTextSecondary)
                            Text(summary.formattedMoneySpent, style = LxType.financialSmall, color = LxMoneyOutRed)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Net movement", style = LxType.caption, color = LxTextSecondary)
                            Text(summary.formattedNetMovement, style = LxType.financialSmall, color = LxLime)
                        }
                    }
                }
            }
            Spacer(Modifier.height(LxSpacing.md))

            LxCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(LxSpacing.sm), horizontalArrangement = Arrangement.spacedBy(LxSpacing.xs)) {
                    LxQuickAction("Money in", Icons.Default.ArrowUpward, LxMoneyInGreen, onRecordReceiptClick, Modifier.weight(1f))
                    LxQuickAction("Money out", Icons.Default.ArrowDownward, LxMoneyOutRed, onRecordExpenseClick, Modifier.weight(1f))
                    LxQuickAction("Debt", Icons.Default.Person, LxWarning, onRecordDebtClick, Modifier.weight(1f))
                }
            }
            Spacer(Modifier.height(LxSpacing.lg))

            Text("Transaction history", style = LxType.sectionTitle, color = LxTextPrimary)
            Spacer(Modifier.height(LxSpacing.sm))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(LxSpacing.sm)) {
                items(LedgerFilter.entries) { item ->
                    LxFilterChip(item.label, filter == item) { filter = item }
                }
            }
            Spacer(Modifier.height(LxSpacing.md))

            if (entries.isEmpty()) {
                Box(Modifier.fillMaxSize()) {
                    if (cashEvents.isEmpty() && debts.isEmpty()) {
                        LxEmptyState(
                            illustration = { LedgerEmptyArt() },
                            title = "Start tracking your money.",
                            body = "Record money in, money out or a customer debt. Each entry stays on this device.",
                            actionLabel = "Record money in",
                            onActionClick = onRecordReceiptClick
                        )
                    } else {
                        LxEmptyState(
                            illustration = { LedgerEmptyArt() },
                            title = "Nothing here yet",
                            body = "Try another filter or choose a different period."
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(LxSpacing.xs),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(entries) { entry ->
                        when (entry) {
                            is LedgerEntry.Header -> {
                                Text(entry.label, style = LxType.caption, color = LxTextSecondary, modifier = Modifier.padding(top = LxSpacing.md, bottom = LxSpacing.xs))
                            }
                            is LedgerEntry.Cash -> CashEventRow(entry.event)
                            is LedgerEntry.DebtEntry -> DebtRow(entry.debt, onSettleDebtClick)
                        }
                    }
                    item { Spacer(Modifier.height(LxSpacing.xl)) }
                }
            }
        }
    }
}

@Composable
private fun LxQuickAction(
    label: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clickable(onClick = onClick)
            .padding(vertical = LxSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LxIconTile(icon, tint = color, size = 36.dp)
        Spacer(Modifier.height(LxSpacing.xs))
        Text(label, style = LxType.caption, color = LxTextPrimary, maxLines = 1)
    }
}

@Composable
private fun CashEventRow(event: CashEvent) {
    val isIn = event.type == CashEventType.RECEIPT || event.type == CashEventType.SETTLEMENT
    val isTransfer = event.type == CashEventType.OWNER_IN || event.type == CashEventType.OWNER_OUT
    val amountColor = when {
        isIn -> LxMoneyInGreen
        isTransfer -> LxTextSecondary
        else -> LxMoneyOutRed
    }
    LxCard(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(LxSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
            LxIconTile(
                icon = when (event.type) {
                    CashEventType.RECEIPT -> Icons.Default.ArrowUpward
                    CashEventType.SETTLEMENT -> Icons.Default.Person
                    CashEventType.OWNER_IN, CashEventType.OWNER_OUT -> Icons.Default.SwapHoriz
                    else -> Icons.Default.ArrowDownward
                },
                tint = amountColor,
                size = 38.dp
            )
            Spacer(Modifier.padding(horizontal = LxSpacing.md))
            Column(Modifier.weight(1f)) {
                Text(
                    event.customerName ?: event.category.ifBlank { event.type.label() },
                    style = LxType.cardTitle,
                    color = LxTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(LxSpacing.xs))
                LxStatusPill(event.type.label(), amountColor)
            }
            Text(
                text = "${if (isIn) "+" else if (amountColor == LxTextSecondary) "" else "-"}${formatMoneyMinor(event.amountMinor)}",
                style = LxType.financialSmall,
                color = amountColor
            )
        }
    }
}

@Composable
private fun DebtRow(debt: Debt, onSettle: (Debt) -> Unit) {
    LxCard(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(LxSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
            LxIconTile(Icons.Default.Person, tint = LxWarning, size = 38.dp)
            Spacer(Modifier.padding(horizontal = LxSpacing.md))
            Column(Modifier.weight(1f)) {
                Text(debt.customerName, style = LxType.cardTitle, color = LxTextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Incurred ${displayDate(debt.incurredDate)}", style = LxType.caption, color = LxTextSecondary)
                Spacer(Modifier.height(LxSpacing.xs))
                LxStatusPill("Balance outstanding", LxWarning)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(debt.formattedRemaining, style = LxType.financialSmall, color = LxLime)
                TextButton(onClick = { onSettle(debt) }) {
                    Text("Settle", style = LxType.button, color = LxLime)
                }
            }
        }
    }
}

private fun periodLabel(period: PeriodFilter): String = when (period) {
    PeriodFilter.TODAY -> "Today"
    PeriodFilter.THIS_WEEK -> "This week"
    PeriodFilter.THIS_MONTH -> "This month"
    PeriodFilter.LAST_MONTH -> "Last month"
    PeriodFilter.CUSTOM -> "Custom"
}

private fun CashEventType.label(): String = when (this) {
    CashEventType.RECEIPT -> "Received"
    CashEventType.SETTLEMENT -> "Debt payment"
    CashEventType.EXPENSE -> "Spent"
    CashEventType.REFUND -> "Refund"
    CashEventType.OWNER_IN -> "Owner transfer in"
    CashEventType.OWNER_OUT -> "Owner transfer out"
}

private fun displayDate(value: String): String {
    val date = runCatching { LocalDate.parse(value) }.getOrNull() ?: return value
    val today = LocalDate.now()
    return when (date) {
        today -> "Today"
        today.minusDays(1) -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))
    }
}

private fun displayDateRange(start: String, end: String): String {
    val startDate = runCatching { LocalDate.parse(start) }.getOrNull() ?: return "$start – $end"
    val endDate = runCatching { LocalDate.parse(end) }.getOrNull() ?: return "$start – $end"
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    return "${startDate.format(formatter)} – ${endDate.format(formatter)}"
}
