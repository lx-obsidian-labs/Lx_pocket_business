package com.obsidianlabs.lxpocketbusiness.ui.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidianlabs.lxpocketbusiness.ads.AdIds
import com.obsidianlabs.lxpocketbusiness.data.model.Business
import com.obsidianlabs.lxpocketbusiness.data.model.formatMoneyMinor
import com.obsidianlabs.lxpocketbusiness.data.repository.ActivityItem
import com.obsidianlabs.lxpocketbusiness.data.repository.ActivityType
import com.obsidianlabs.lxpocketbusiness.data.repository.AttentionItem
import com.obsidianlabs.lxpocketbusiness.data.repository.AttentionSource
import com.obsidianlabs.lxpocketbusiness.data.repository.DashboardData
import com.obsidianlabs.lxpocketbusiness.data.repository.FinancialSummary
import com.obsidianlabs.lxpocketbusiness.data.repository.PeriodFilter
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAdBanner
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackground
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackgroundOlive
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackgroundSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxError
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyInGreen
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyOutRed
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceBorder
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceVariant
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxWarning
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun HomeScreen(
    business: Business? = null,
    data: DashboardData = DashboardData(FinancialSummary()),
    selectedPeriod: PeriodFilter = PeriodFilter.THIS_MONTH,
    onPeriodSelected: (PeriodFilter) -> Unit = {},
    onCustomRangeSelected: (String, String) -> Unit = { _, _ -> },
    onMoneyInClick: () -> Unit = {},
    onMoneyOutClick: () -> Unit = {},
    onRecordDebtClick: () -> Unit = {},
    onAttentionClick: (AttentionItem) -> Unit = {},
    onCompleteTask: (String) -> Unit = {},
    onSnoozeTask: (String) -> Unit = {},
    onActivityClick: (ActivityItem) -> Unit = {},
    onViewAllAttentionClick: () -> Unit = {},
    onViewAllActivityClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onRecordTransactionClick: () -> Unit = {},
    adsReady: Boolean = false
) {
    var periodExpanded by remember { mutableStateOf(false) }
    var showCustomDialog by rememberSaveable { mutableStateOf(false) }
    var customStart by rememberSaveable { mutableStateOf("") }
    var customEnd by rememberSaveable { mutableStateOf("") }
    var customError by rememberSaveable { mutableStateOf<String?>(null) }
    var attentionY by remember { mutableIntStateOf(0) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val tabular = TextStyle(fontFeatureSettings = "tnum")

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(LxBackgroundOlive, LxBackgroundSecondary, LxBackground)))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BusinessAvatar(business = business)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Your business workspace", color = LxTextSecondary, fontSize = 12.sp)
                    Text(
                        text = business?.name ?: "LX Pocket Business",
                        color = LxTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = {
                    scope.launch { scrollState.animateScrollTo(attentionY.coerceAtLeast(0)) }
                    onNotificationsClick()
                }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Needs attention", tint = LxTextPrimary)
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = LxTextPrimary)
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Overview",
                    color = LxTextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(LxCardBg)
                            .border(1.dp, LxSurfaceBorder, RoundedCornerShape(20.dp))
                            .clickable { periodExpanded = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(data.periodLabel, color = LxTextPrimary, fontSize = 13.sp)
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "Choose period",
                                tint = LxTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = periodExpanded,
                        onDismissRequest = { periodExpanded = false },
                        modifier = Modifier.background(LxCardBg)
                    ) {
                        PeriodFilter.entries.forEach { filter ->
                            DropdownMenuItem(
                                text = { Text(periodLabel(filter), color = LxTextPrimary) },
                                onClick = {
                                    periodExpanded = false
                                    if (filter == PeriodFilter.CUSTOM) showCustomDialog = true else onPeriodSelected(filter)
                                }
                            )
                        }
                    }
                }
            }
            Text(data.summary.formattedPeriodRange, color = LxTextSecondary, fontSize = 12.sp)

            Spacer(Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = LxCardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LxSurfaceBorder, RoundedCornerShape(16.dp))
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Net cash movement", color = LxTextSecondary, fontSize = 14.sp)
                    Text(
                        data.summary.formattedNetMovement,
                        color = if (data.summary.netMovementMinor < 0) LxError else LxTextPrimary,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        style = tabular
                    )
                    Text(
                        "Money received minus money spent and refunds. Not profit.",
                        color = LxTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MetricCell(
                            title = "Money in",
                            value = data.summary.formattedMoneyReceived,
                            color = LxMoneyInGreen,
                            tabular = tabular,
                            modifier = Modifier.weight(1f)
                        )
                        VerticalDivider(
                            color = LxSurfaceBorder,
                            thickness = 1.dp,
                            modifier = Modifier.height(48.dp)
                        )
                        MetricCell(
                            title = "Money out",
                            value = data.summary.formattedMoneySpent,
                            color = LxMoneyOutRed,
                            tabular = tabular,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (data.summary.refundMinor > 0) {
                        Spacer(Modifier.height(12.dp))
                        Text("Refunds: ${data.summary.formattedRefund}", color = LxWarning, fontSize = 12.sp, style = tabular)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = LxCardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onViewAllActivityClick)
                    .border(1.dp, LxSurfaceBorder, RoundedCornerShape(16.dp))
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LxSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = LxWarning)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Customers owe you", color = LxTextSecondary, fontSize = 13.sp)
                        Text("As of ${data.debtAsOfDate}", color = LxTextSecondary, fontSize = 11.sp)
                    }
                    Text(
                        data.summary.formattedDebtOwed,
                        color = LxLime,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = tabular
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            SectionTitle("Quick actions")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickAction(
                    title = "Money in",
                    icon = Icons.Default.ArrowDownward,
                    onClick = onMoneyInClick,
                    modifier = Modifier.weight(1f)
                )
                QuickAction(
                    title = "Money out",
                    icon = Icons.Default.ArrowUpward,
                    onClick = onMoneyOutClick,
                    modifier = Modifier.weight(1f)
                )
                QuickAction(
                    title = "Record debt",
                    icon = Icons.Default.Person,
                    onClick = onRecordDebtClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            SectionTitle(
                title = "Needs attention",
                actionLabel = "View all",
                onActionClick = onViewAllAttentionClick
            )
            Spacer(Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = LxCardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { attentionY = it.positionInParent().y.toInt() }
                    .border(1.dp, LxSurfaceBorder, RoundedCornerShape(16.dp))
            ) {
                if (data.attention.isEmpty()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = LxLime, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("You're all caught up", color = LxTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Nothing to follow up today.", color = LxTextSecondary, fontSize = 12.sp)
                        Text(
                            "Payment reminders and upcoming tasks will appear here.",
                            color = LxTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Column(Modifier.padding(8.dp)) {
                        data.attention.take(3).forEach { item ->
                            AttentionRow(
                                item = item,
                                onClick = { onAttentionClick(item) },
                                onComplete = if (item.source == AttentionSource.TASK) onCompleteTask else null,
                                onSnooze = if (item.source == AttentionSource.TASK) onSnoozeTask else null
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            SectionTitle(
                title = "Recent activity",
                actionLabel = "View all",
                onActionClick = onViewAllActivityClick
            )
            Spacer(Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = LxCardBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, LxSurfaceBorder, RoundedCornerShape(16.dp))
            ) {
                if (data.recentActivity.isEmpty()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = null, tint = LxTextSecondary, modifier = Modifier.size(32.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("No activity yet", color = LxTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Your activity will appear here.", color = LxTextSecondary, fontSize = 12.sp)
                        Text("Record your first sale or expense.", color = LxTextSecondary, fontSize = 12.sp)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onRecordTransactionClick,
                            colors = ButtonDefaults.buttonColors(containerColor = LxLime, contentColor = LxTextDark),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.heightIn(min = 48.dp)
                        ) {
                            Text("+ Record transaction", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Column(Modifier.padding(8.dp)) {
                        data.recentActivity.forEach { item ->
                            ActivityRow(item = item, onClick = { onActivityClick(item) }, tabular = tabular)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            LxAdBanner(enabled = adsReady, unitId = AdIds.banner)
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showCustomDialog) {
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { Text("Custom period", color = LxTextPrimary) },
            text = {
                Column {
                    OutlinedTextField(
                        value = customStart,
                        onValueChange = { customStart = it; customError = null },
                        label = { Text("Start date") },
                        placeholder = { Text("YYYY-MM-DD") },
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = customEnd,
                        onValueChange = { customEnd = it; customError = null },
                        label = { Text("End date") },
                        placeholder = { Text("YYYY-MM-DD") },
                        singleLine = true
                    )
                    customError?.let {
                        Spacer(Modifier.height(8.dp))
                        Text(it, color = LxError, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (isValidIsoDate(customStart) && isValidIsoDate(customEnd) && customStart <= customEnd) {
                        onCustomRangeSelected(customStart, customEnd)
                        onPeriodSelected(PeriodFilter.CUSTOM)
                        showCustomDialog = false
                    } else {
                        customError = "Use YYYY-MM-DD dates with the end date on or after the start date."
                    }
                }) { Text("Apply", color = LxLime) }
            },
            dismissButton = {
                TextButton(onClick = { showCustomDialog = false }) { Text("Cancel", color = LxTextSecondary) }
            },
            containerColor = LxCardBg
        )
    }
}

@Composable
private fun BusinessAvatar(business: Business?) {
    val logoPath = business?.logoPath
    val bitmap = remember(logoPath) {
        logoPath?.let { path -> runCatching { BitmapFactory.decodeFile(path) }.getOrNull() }
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Business logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .border(1.dp, LxSurfaceBorder, CircleShape)
        )
    } else {
        Box(
            Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(LxSurfaceVariant)
                .border(1.dp, LxSurfaceBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                business?.name?.initials().orEmpty().ifBlank { "LX" },
                color = LxLime,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MetricCell(
    title: String,
    value: String,
    color: Color,
    tabular: TextStyle,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(title, color = LxTextSecondary, fontSize = 12.sp)
        Text(value, color = color, fontSize = 20.sp, fontWeight = FontWeight.Bold, style = tabular)
    }
}

@Composable
private fun QuickAction(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = LxCardBg),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .heightIn(min = 104.dp)
            .clickable(onClick = onClick)
            .border(1.dp, LxSurfaceBorder, RoundedCornerShape(16.dp))
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(LxSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = LxLime, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(title, color = LxTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun AttentionRow(
    item: AttentionItem,
    onClick: () -> Unit,
    onComplete: ((String) -> Unit)?,
    onSnooze: ((String) -> Unit)?
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            if (item.isOverdue) Icons.Default.Warning else Icons.Default.Schedule,
            contentDescription = if (item.isOverdue) "Overdue" else "Due",
            tint = if (item.isOverdue) LxWarning else LxLime,
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.title, color = LxTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(item.subtitle, color = LxTextSecondary, fontSize = 12.sp)
            if (item.isOverdue) {
                Text("Overdue · ${item.dueDate}", color = LxWarning, fontSize = 11.sp)
            } else {
                Text("Due ${item.dueDate}", color = LxTextSecondary, fontSize = 11.sp)
            }
        }
        if (onComplete != null) {
            TextButton(onClick = { onComplete(item.sourceId) }) { Text("Done", color = LxLime, fontSize = 12.sp) }
        }
        if (onSnooze != null) {
            TextButton(onClick = { onSnooze(item.sourceId) }) { Text("Snooze", color = LxTextSecondary, fontSize = 12.sp) }
        }
    }
}

@Composable
private fun ActivityRow(item: ActivityItem, onClick: () -> Unit, tabular: TextStyle) {
    val icon = when (item.type) {
        ActivityType.MONEY_IN -> Icons.Default.ArrowDownward
        ActivityType.MONEY_OUT -> Icons.Default.ArrowUpward
        ActivityType.DEBT -> Icons.Default.Person
        ActivityType.DOCUMENT -> Icons.Default.Description
    }
    val color = when (item.type) {
        ActivityType.MONEY_IN -> LxMoneyInGreen
        ActivityType.MONEY_OUT -> LxMoneyOutRed
        ActivityType.DEBT -> LxWarning
        ActivityType.DOCUMENT -> LxTextSecondary
    }
    val label = when (item.type) {
        ActivityType.MONEY_IN -> "In"
        ActivityType.MONEY_OUT -> "Out"
        ActivityType.DEBT -> "Debt"
        ActivityType.DOCUMENT -> "Document"
    }
    val amount = when {
        item.type == ActivityType.MONEY_IN -> "+${formatMoneyMinor(item.amountMinor)}"
        item.type == ActivityType.MONEY_OUT -> "-${formatMoneyMinor(kotlin.math.abs(item.amountMinor))}"
        else -> formatMoneyMinor(item.amountMinor)
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(LxSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.title, color = LxTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text("${item.subtitle} · ${item.date}", color = LxTextSecondary, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(label, color = color, fontSize = 10.sp)
            Text(amount, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold, style = tabular)
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = LxTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        if (actionLabel != null) {
            TextButton(onClick = onActionClick) { Text(actionLabel, color = LxLime, fontSize = 13.sp) }
        }
    }
}

private fun periodLabel(filter: PeriodFilter): String = when (filter) {
    PeriodFilter.TODAY -> "Today"
    PeriodFilter.THIS_WEEK -> "This week"
    PeriodFilter.THIS_MONTH -> "This month"
    PeriodFilter.LAST_MONTH -> "Last month"
    PeriodFilter.CUSTOM -> "Custom period"
}

private fun String.initials(): String = trim()
    .split(Regex("\\s+"))
    .filter { it.isNotBlank() }
    .take(2)
    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
    .joinToString("")

private fun isValidIsoDate(value: String): Boolean = runCatching { LocalDate.parse(value) }.isSuccess
