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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.obsidianlabs.lxpocketbusiness.ads.AdIds
import com.obsidianlabs.lxpocketbusiness.data.model.Document
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentStatus
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentType
import com.obsidianlabs.lxpocketbusiness.ui.components.DocumentsEmptyArt
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAdBanner
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAppBackground
import com.obsidianlabs.lxpocketbusiness.ui.components.LxCard
import com.obsidianlabs.lxpocketbusiness.ui.components.LxEmptyState
import com.obsidianlabs.lxpocketbusiness.ui.components.LxFilterChip
import com.obsidianlabs.lxpocketbusiness.ui.components.LxIconTile
import com.obsidianlabs.lxpocketbusiness.ui.components.LxScreenHeader
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSearchField
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSpacing
import com.obsidianlabs.lxpocketbusiness.ui.components.LxStatusPill
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxError
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyInGreen
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxWarning
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxType

private enum class DocumentFilter(val label: String) {
    ALL("All"),
    QUOTES("Quotes"),
    INVOICES("Invoices"),
    CREDIT_NOTES("Credit notes")
}

private enum class StatusFilter(val label: String) {
    ALL("All statuses"),
    OPEN("Open"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsScreen(
    documents: List<Document>,
    isVatVendor: Boolean = false,
    onNewDocumentClick: (DocumentType) -> Unit,
    onDocumentClick: (Document) -> Unit,
    adsReady: Boolean = false
) {
    var query by remember { mutableStateOf("") }
    var typeFilter by remember { mutableStateOf(DocumentFilter.ALL) }
    var statusFilter by remember { mutableStateOf(StatusFilter.ALL) }
    var showCreateSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val visible = documents.filter { doc ->
        val matchesType = when (typeFilter) {
            DocumentFilter.ALL -> true
            DocumentFilter.QUOTES -> doc.type == DocumentType.QUOTE
            DocumentFilter.INVOICES -> doc.type == DocumentType.INVOICE
            DocumentFilter.CREDIT_NOTES -> doc.type == DocumentType.CREDIT_NOTE
        }
        val matchesStatus = when (statusFilter) {
            StatusFilter.ALL -> true
            StatusFilter.OPEN -> doc.status == DocumentStatus.ISSUED || doc.status == DocumentStatus.SENT
            StatusFilter.ACCEPTED -> doc.status == DocumentStatus.ACCEPTED || doc.status == DocumentStatus.CONVERTED
            StatusFilter.REJECTED -> doc.status == DocumentStatus.REJECTED
        }
        val matchesQuery = query.isBlank() ||
            doc.number.contains(query, ignoreCase = true) ||
            doc.customerName.contains(query, ignoreCase = true) ||
            doc.lines.any { it.description.contains(query, ignoreCase = true) }
        matchesType && matchesStatus && matchesQuery
    }.sortedByDescending { it.createdAt }

    LxAppBackground {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize().padding(horizontal = LxSpacing.lg, vertical = LxSpacing.lg)) {
                LxScreenHeader(
                    title = "Documents",
                    subtitle = "${documents.count { it.type == DocumentType.QUOTE }} quotes · ${documents.count { it.type == DocumentType.INVOICE }} invoices"
                )
                Spacer(Modifier.height(LxSpacing.lg))

                LxSearchField(query, { query = it }, "Search number, customer or item", leadingIcon = Icons.Default.Search)
                Spacer(Modifier.height(LxSpacing.md))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(LxSpacing.sm)) {
                    items(DocumentFilter.entries) { item ->
                        LxFilterChip(item.label, typeFilter == item) { typeFilter = item }
                    }
                }
                Spacer(Modifier.height(LxSpacing.sm))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(LxSpacing.sm)) {
                    items(StatusFilter.entries) { item ->
                        LxFilterChip(item.label, statusFilter == item) { statusFilter = item }
                    }
                }
                Spacer(Modifier.height(LxSpacing.md))
                LxAdBanner(enabled = adsReady, unitId = AdIds.banner)
                Spacer(Modifier.height(LxSpacing.md))

                if (visible.isEmpty()) {
                    Box(Modifier.fillMaxSize()) {
                        if (documents.isEmpty()) {
                            LxEmptyState(
                                illustration = { DocumentsEmptyArt() },
                                title = "Professional documents start here.",
                                body = "Create quotes and invoices that reflect your real business details and actual records.",
                                actionLabel = "Create document",
                                onActionClick = { showCreateSheet = true }
                            )
                        } else {
                            LxEmptyState(
                                illustration = { DocumentsEmptyArt() },
                                title = "No matching documents",
                                body = "Try a different search, type or status filter."
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(LxSpacing.sm),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(visible) { doc ->
                            LxCard(Modifier.fillMaxWidth(), onClick = { onDocumentClick(doc) }) {
                                Row(Modifier.padding(LxSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                                    LxIconTile(Icons.Default.PictureAsPdf, size = 40.dp)
                                    Spacer(Modifier.padding(horizontal = LxSpacing.md))
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            documentTitle(doc, isVatVendor),
                                            style = LxType.cardTitle,
                                            color = LxTextPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            "${doc.customerName} · ${doc.number.ifBlank { "DRAFT" }} · ${doc.issueDate}",
                                            style = LxType.caption,
                                            color = LxTextSecondary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(Modifier.height(LxSpacing.xs))
                                        LxStatusPill(statusLabel(doc.status), statusColor(doc.status))
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(doc.formattedTotalGross, style = LxType.financialSmall, color = LxLime)
                                    }
                                }
                            }
                        }
                        item { Spacer(Modifier.height(LxSpacing.xxl)) }
                    }
                }
            }

            FloatingActionButton(
                onClick = { showCreateSheet = true },
                containerColor = LxLime,
                contentColor = LxTextDark,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(LxSpacing.xl)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create document")
            }

            if (showCreateSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showCreateSheet = false },
                    sheetState = sheetState,
                    containerColor = LxCardBg
                ) {
                    Column(Modifier.padding(horizontal = LxSpacing.lg, vertical = LxSpacing.md)) {
                        Text("Create document", style = LxType.screenTitle, color = LxTextPrimary)
                        Spacer(Modifier.height(LxSpacing.sm))
                        Text("Choose what you need to send.", style = LxType.caption, color = LxTextSecondary)
                        Spacer(Modifier.height(LxSpacing.lg))
                        CreateOption(Icons.Default.Description, "Quote", "Send an estimate before work starts") {
                            showCreateSheet = false
                            onNewDocumentClick(DocumentType.QUOTE)
                        }
                        CreateOption(Icons.Default.PictureAsPdf, if (isVatVendor) "Tax invoice" else "Invoice", "Bill a customer for work completed") {
                            showCreateSheet = false
                            onNewDocumentClick(DocumentType.INVOICE)
                        }
                        CreateOption(
                            icon = Icons.Default.Description,
                            title = "Credit note",
                            subtitle = "Linked corrections are not available in this build",
                            enabled = false
                        ) {}
                        Spacer(Modifier.height(LxSpacing.xl))
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(if (enabled) Color.Transparent else LxCardBg)
            .padding(vertical = LxSpacing.md)
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LxIconTile(icon, tint = if (enabled) LxLime else LxTextMuted, size = 40.dp)
        Spacer(Modifier.padding(horizontal = LxSpacing.md))
        Column(Modifier.weight(1f)) {
            Text(title, style = LxType.cardTitle, color = if (enabled) LxTextPrimary else LxTextMuted)
            Text(subtitle, style = LxType.caption, color = LxTextSecondary, maxLines = 2)
        }
    }
}

private fun documentTitle(document: Document, isVatVendor: Boolean): String = when (document.type) {
    DocumentType.QUOTE -> "Quote"
    DocumentType.INVOICE -> if (isVatVendor) "Tax invoice" else "Invoice"
    DocumentType.CREDIT_NOTE -> "Credit note"
}

private fun statusLabel(status: DocumentStatus): String = when (status) {
    DocumentStatus.DRAFT -> "Draft"
    DocumentStatus.ISSUED -> "Issued"
    DocumentStatus.SENT -> "Sent"
    DocumentStatus.ACCEPTED -> "Accepted"
    DocumentStatus.REJECTED -> "Rejected"
    DocumentStatus.CONVERTED -> "Converted"
}

private fun statusColor(status: DocumentStatus): Color = when (status) {
    DocumentStatus.DRAFT -> LxTextMuted
    DocumentStatus.ISSUED, DocumentStatus.SENT -> LxWarning
    DocumentStatus.ACCEPTED, DocumentStatus.CONVERTED -> LxMoneyInGreen
    DocumentStatus.REJECTED -> LxError
}
