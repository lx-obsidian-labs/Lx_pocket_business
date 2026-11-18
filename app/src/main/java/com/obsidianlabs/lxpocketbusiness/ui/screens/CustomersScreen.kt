package com.obsidianlabs.lxpocketbusiness.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.obsidianlabs.lxpocketbusiness.data.model.Customer
import com.obsidianlabs.lxpocketbusiness.data.model.Debt
import com.obsidianlabs.lxpocketbusiness.data.model.formatMoneyMinor
import com.obsidianlabs.lxpocketbusiness.ui.components.CustomersEmptyArt
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAppBackground
import com.obsidianlabs.lxpocketbusiness.ui.components.LxCard
import com.obsidianlabs.lxpocketbusiness.ui.components.LxEmptyState
import com.obsidianlabs.lxpocketbusiness.ui.components.LxFilterChip
import com.obsidianlabs.lxpocketbusiness.ui.components.LxIconTile
import com.obsidianlabs.lxpocketbusiness.ui.components.LxScreenHeader
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSearchField
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSpacing
import com.obsidianlabs.lxpocketbusiness.ui.components.LxStatusPill
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyInGreen
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxType
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxWarning
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private enum class CustomerFilter(val label: String) {
    ALL("All"),
    OUTSTANDING("Outstanding"),
    RECENT("Recent"),
    ARCHIVED("Archived")
}

@Composable
fun CustomersScreen(
    customers: List<Customer>,
    debts: List<Debt>,
    onCustomerClick: (Customer) -> Unit,
    onAddCustomerClick: () -> Unit,
    onArchiveCustomer: (Customer) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(CustomerFilter.ALL) }

    val active = customers.filter { !it.isArchived }
    val archived = customers.filter { it.isArchived }
    val owedByCustomer = debts.filter { !it.isSettled }.groupBy { it.customerId }
        .mapValues { (_, items) -> items.sumOf { it.remainingMinor } }
    val totalOwed = owedByCustomer.values.sum()
    val recentCutoff = LocalDate.now().minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant()

    val visible = customers.filter { customer ->
        val matchesQuery = query.isBlank() ||
            customer.displayName.contains(query, ignoreCase = true) ||
            customer.phone.contains(query, ignoreCase = true) ||
            customer.email.contains(query, ignoreCase = true)
        val matchesFilter = when (filter) {
            CustomerFilter.ALL -> !customer.isArchived
            CustomerFilter.OUTSTANDING -> !customer.isArchived && (owedByCustomer[customer.id] ?: 0L) > 0L
            CustomerFilter.RECENT -> !customer.isArchived && Instant.ofEpochMilli(customer.createdAt).isAfter(recentCutoff)
            CustomerFilter.ARCHIVED -> customer.isArchived
        }
        matchesQuery && matchesFilter
    }.sortedByDescending { it.createdAt }

    LxAppBackground {
        Column(Modifier.fillMaxSize().padding(horizontal = LxSpacing.lg, vertical = LxSpacing.lg)) {
            LxScreenHeader(
                title = "Customers",
                subtitle = "${active.size} active · ${archived.size} archived",
                actions = {
                    if (customers.isNotEmpty()) {
                        IconButton(onClick = onAddCustomerClick) {
                            Icon(Icons.Default.Add, contentDescription = "Add customer", tint = LxLime)
                        }
                    }
                }
            )
            Spacer(Modifier.height(LxSpacing.lg))

            LxCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(LxSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Outstanding balance", style = LxType.caption, color = LxTextSecondary)
                        Text(formatMoneyMinor(totalOwed), style = LxType.financialMedium, color = LxWarning)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("With balance", style = LxType.caption, color = LxTextSecondary)
                        Text(owedByCustomer.count { it.value > 0 }.toString(), style = LxType.financialSmall, color = LxTextPrimary)
                    }
                }
            }
            Spacer(Modifier.height(LxSpacing.md))

            LxSearchField(query, { query = it }, "Search by name, phone or email", leadingIcon = Icons.Default.Search)
            Spacer(Modifier.height(LxSpacing.md))

            LazyRow(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(LxSpacing.sm)) {
                items(CustomerFilter.entries) { item ->
                    LxFilterChip(item.label, filter == item) { filter = item }
                }
            }
            Spacer(Modifier.height(LxSpacing.md))

            if (visible.isEmpty()) {
                val isFreshEmpty = customers.isEmpty()
                Box(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                    if (isFreshEmpty) {
                        LxEmptyState(
                            illustration = { CustomersEmptyArt() },
                            title = "Your customers, organized.",
                            body = "Add your first customer to create invoices, track payments and manage outstanding balances.",
                            actionLabel = "Add customer",
                            onActionClick = onAddCustomerClick
                        )
                    } else {
                        LxEmptyState(
                            illustration = { CustomersEmptyArt() },
                            title = "No matching customers",
                            body = "Try another search term or choose a different filter."
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(LxSpacing.sm),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(visible) { customer ->
                        val owed = owedByCustomer[customer.id] ?: 0L
                        LxCard(Modifier.fillMaxWidth(), onClick = { onCustomerClick(customer) }) {
                            Row(Modifier.padding(LxSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                                LxIconTile(Icons.Default.Person, size = 42.dp)
                                Spacer(Modifier.padding(horizontal = LxSpacing.md))
                                Column(Modifier.weight(1f)) {
                                    Text(customer.displayName, style = LxType.cardTitle, color = LxTextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(
                                        listOf(customer.phone, customer.email).filter { it.isNotBlank() }.joinToString(" · ").ifBlank { "No contact details" },
                                        style = LxType.caption,
                                        color = LxTextSecondary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.height(LxSpacing.xs))
                                    LxStatusPill(
                                        text = when {
                                            customer.isArchived -> "Archived"
                                            owed > 0 -> "Balance outstanding"
                                            else -> "Up to date"
                                        },
                                        color = when {
                                            customer.isArchived -> LxTextMuted
                                            owed > 0 -> LxWarning
                                            else -> LxMoneyInGreen
                                        }
                                    )
                                }
                                if (owed > 0) {
                                    Text(formatMoneyMinor(owed), style = LxType.financialSmall, color = LxLime)
                                }
                            }
                            if (!customer.isArchived) {
                                Row(Modifier.padding(horizontal = LxSpacing.lg, vertical = LxSpacing.xs)) {
                                    TextButton(onClick = { onArchiveCustomer(customer) }) {
                                        Text("Archive", style = LxType.caption, color = LxTextSecondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
