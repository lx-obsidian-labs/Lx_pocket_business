package com.obsidianlabs.lxpocketbusiness.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidianlabs.lxpocketbusiness.data.model.Customer
import com.obsidianlabs.lxpocketbusiness.data.model.Document
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentLine
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentStatus
import com.obsidianlabs.lxpocketbusiness.data.model.DocumentType
import com.obsidianlabs.lxpocketbusiness.data.model.parseMoneyMinor
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackground
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceBorder
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DocumentEditorScreen(
    customers: List<Customer>,
    onBackClick: () -> Unit,
    onSubmitDocument: (Document) -> Unit,
    allowVat: Boolean = false,
    initialType: DocumentType = DocumentType.INVOICE
) {
    var docType by remember { mutableStateOf(initialType) }

    var selectedCustomer by remember { mutableStateOf<Customer?>(null) }
    var isCustomerDropdownExpanded by remember { mutableStateOf(false) }

    val lines = remember {
        mutableStateListOf<DocumentLine>()
    }

    var newItemDesc by remember { mutableStateOf("") }
    var newItemQty by remember { mutableStateOf("1") }
    var newItemPrice by remember { mutableStateOf("") }

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

    val totalNetMinor = lines.sumOf { it.netMinor }
    val totalTaxMinor = lines.sumOf { it.taxMinor }
    val totalGrossMinor = lines.sumOf { it.grossMinor }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackground)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LxTextPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Create Document", color = LxTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

            // Document Type Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(DocumentType.INVOICE, DocumentType.QUOTE).forEach { type ->
                    val isSelected = docType == type
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) LxLime else LxCardBg)
                            .border(1.dp, if (isSelected) LxLime else LxSurfaceBorder, RoundedCornerShape(12.dp))
                            .clickable { docType = type }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type.name,
                            color = if (isSelected) LxTextDark else LxTextSecondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Customer Selector
            Text(text = "Select Customer *", color = LxTextSecondary, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LxCardBg)
                        .border(1.dp, LxSurfaceBorder, RoundedCornerShape(12.dp))
                        .clickable { isCustomerDropdownExpanded = true }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = selectedCustomer?.displayName ?: "Select customer...",
                            color = if (selectedCustomer != null) LxTextPrimary else LxTextMuted,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = LxTextSecondary)
                    }
                }

                DropdownMenu(
                    expanded = isCustomerDropdownExpanded,
                    onDismissRequest = { isCustomerDropdownExpanded = false },
                    modifier = Modifier.background(LxCardBg).border(1.dp, LxSurfaceBorder)
                ) {
                    customers.filterNot { it.isArchived }.forEach { customer ->
                        DropdownMenuItem(
                            text = { Text(customer.displayName, color = LxTextPrimary) },
                            onClick = {
                                selectedCustomer = customer
                                isCustomerDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Line items header
            Text(text = "Line Items", color = LxTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            // Lines List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(lines) { index, line ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = LxCardBg),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, LxSurfaceBorder, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = line.description, color = LxTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "${line.quantity} x R${String.format(Locale.US, "%.2f", line.unitPriceMinor / 100.0)}",
                                    color = LxTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = "R${String.format(Locale.US, "%.2f", line.grossMinor / 100.0)}",
                                color = LxLime,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { lines.removeAt(index) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    }
                }

                // Inline add item card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF161E16)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, LxSurfaceBorder, RoundedCornerShape(12.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Add Line Item", color = LxLime, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = newItemDesc,
                                onValueChange = { newItemDesc = it },
                                placeholder = { Text("Description", color = LxTextMuted) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LxLime, unfocusedBorderColor = LxSurfaceBorder, focusedTextColor = LxTextPrimary, unfocusedTextColor = LxTextPrimary),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = newItemQty,
                                    onValueChange = { newItemQty = it },
                                    placeholder = { Text("Qty", color = LxTextMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LxLime, unfocusedBorderColor = LxSurfaceBorder, focusedTextColor = LxTextPrimary, unfocusedTextColor = LxTextPrimary),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = newItemPrice,
                                    onValueChange = { newItemPrice = it },
                                    placeholder = { Text("Price R", color = LxTextMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = LxLime, unfocusedBorderColor = LxSurfaceBorder, focusedTextColor = LxTextPrimary, unfocusedTextColor = LxTextPrimary),
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val priceMinor = parseMoneyMinor(newItemPrice) ?: return@Button
                                    val qtyVal = newItemQty.toDoubleOrNull() ?: return@Button
                                    if (newItemDesc.isNotBlank() && qtyVal.isFinite() && qtyVal > 0 &&
                                        qtyVal * priceMinor < Long.MAX_VALUE / 2.0) {
                                        lines.add(
                                            DocumentLine(
                                                description = newItemDesc,
                                                quantity = qtyVal,
                                                unitPriceMinor = priceMinor,
                                                isTaxable = allowVat
                                            )
                                        )
                                        newItemDesc = ""
                                        newItemPrice = ""
                                        newItemQty = "1"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = LxLime, contentColor = LxTextDark),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add Line")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Totals Summary Box
            Card(
                colors = CardDefaults.cardColors(containerColor = LxCardBg),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, LxSurfaceBorder, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Subtotal Net:", color = LxTextSecondary, fontSize = 13.sp)
                        Text(text = "R${String.format(Locale.US, "%.2f", totalNetMinor / 100.0)}", color = LxTextPrimary, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "VAT (15%):", color = LxTextSecondary, fontSize = 13.sp)
                        Text(text = "R${String.format(Locale.US, "%.2f", totalTaxMinor / 100.0)}", color = LxTextPrimary, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Grand Total:", color = LxTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(text = "R${String.format(Locale.US, "%.2f", totalGrossMinor / 100.0)}", color = LxLime, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Submit Button
            Button(
                onClick = {
                    val customer = selectedCustomer ?: return@Button
                    val doc = Document(
                        type = docType,
                        status = DocumentStatus.ISSUED,
                        customerId = customer.id,
                        customerName = customer.displayName,
                        customerPhone = customer.phone,
                        customerAddress = customer.address,
                        issueDate = todayStr,
                        lines = lines.toList()
                    )
                    onSubmitDocument(doc)
                },
                enabled = selectedCustomer != null && lines.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = LxLime, contentColor = LxTextDark),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(text = "Issue Document & Generate PDF", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
