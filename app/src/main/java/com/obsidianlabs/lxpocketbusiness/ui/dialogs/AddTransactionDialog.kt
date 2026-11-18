package com.obsidianlabs.lxpocketbusiness.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.obsidianlabs.lxpocketbusiness.data.model.parseMoneyMinor
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackground
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceBorder
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary

enum class TransactionMode {
    MONEY_IN,
    MONEY_OUT,
    SOMEONE_OWES_ME
}

@Composable
fun AddTransactionDialog(
    initialMode: TransactionMode = TransactionMode.MONEY_IN,
    onDismiss: () -> Unit,
    onSubmitReceipt: (amountMinor: Long, customerName: String?, notes: String) -> Unit,
    onSubmitExpense: (supplier: String, amountMinor: Long, category: String, notes: String) -> Unit,
    onSubmitDebt: (customerName: String, amountMinor: Long, dueDate: String?, notes: String) -> Unit
) {
    var mode by remember { mutableStateOf(initialMode) }
    var amountText by remember { mutableStateOf("") }
    var nameText by remember { mutableStateOf("") }
    var categoryOrDueDateText by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }

    val title = when (mode) {
        TransactionMode.MONEY_IN -> "Record Money In"
        TransactionMode.MONEY_OUT -> "Record Money Out"
        TransactionMode.SOMEONE_OWES_ME -> "Record Debt (Someone Owes Me)"
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = LxCardBg),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, LxSurfaceBorder, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp)
            ) {
                Text(
                    text = title,
                    color = LxTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Amount Field (ZAR)
                Text(
                    text = "Amount (Rands) *",
                    color = LxTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    placeholder = { Text("0.00", color = LxTextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    prefix = { Text("R ", color = LxLime, fontWeight = FontWeight.Bold) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LxLime,
                        unfocusedBorderColor = LxSurfaceBorder,
                        focusedTextColor = LxTextPrimary,
                        unfocusedTextColor = LxTextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Customer / Supplier / Name Field
                val nameLabel = when (mode) {
                    TransactionMode.MONEY_IN -> "Customer Name (Optional)"
                    TransactionMode.MONEY_OUT -> "Supplier / Payee Name"
                    TransactionMode.SOMEONE_OWES_ME -> "Customer Name *"
                }
                Text(
                    text = nameLabel,
                    color = LxTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    placeholder = { Text("e.g. Sipho Nkosi", color = LxTextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LxLime,
                        unfocusedBorderColor = LxSurfaceBorder,
                        focusedTextColor = LxTextPrimary,
                        unfocusedTextColor = LxTextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mode specific field: Category or Due Date
                if (mode == TransactionMode.MONEY_OUT) {
                    Text(text = "Category (Optional)", color = LxTextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = categoryOrDueDateText,
                        onValueChange = { categoryOrDueDateText = it },
                        placeholder = { Text("e.g. Stock, Materials, Rent", color = LxTextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LxLime,
                            unfocusedBorderColor = LxSurfaceBorder,
                            focusedTextColor = LxTextPrimary,
                            unfocusedTextColor = LxTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                } else if (mode == TransactionMode.SOMEONE_OWES_ME) {
                    Text(text = "Due Date (YYYY-MM-DD)", color = LxTextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = categoryOrDueDateText,
                        onValueChange = { categoryOrDueDateText = it },
                        placeholder = { Text("e.g. 2026-03-15", color = LxTextMuted) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LxLime,
                            unfocusedBorderColor = LxSurfaceBorder,
                            focusedTextColor = LxTextPrimary,
                            unfocusedTextColor = LxTextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Notes Field
                Text(text = "Notes / Description", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = notesText,
                    onValueChange = { notesText = it },
                    placeholder = { Text("Add transaction details...", color = LxTextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = LxLime,
                        unfocusedBorderColor = LxSurfaceBorder,
                        focusedTextColor = LxTextPrimary,
                        unfocusedTextColor = LxTextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = LxTextSecondary)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    val parsedAmount = parseMoneyMinor(amountText)
                    val isEnabled = parsedAmount != null &&
                            (mode != TransactionMode.SOMEONE_OWES_ME || nameText.isNotBlank())

                    Button(
                        onClick = {
                            val amountMinor = parsedAmount ?: return@Button

                            when (mode) {
                                TransactionMode.MONEY_IN -> onSubmitReceipt(
                                    amountMinor,
                                    nameText.ifBlank { null },
                                    notesText
                                )
                                TransactionMode.MONEY_OUT -> onSubmitExpense(
                                    nameText.ifBlank { "Supplier" },
                                    amountMinor,
                                    categoryOrDueDateText.ifBlank { "General" },
                                    notesText
                                )
                                TransactionMode.SOMEONE_OWES_ME -> onSubmitDebt(
                                    nameText,
                                    amountMinor,
                                    categoryOrDueDateText.ifBlank { null },
                                    notesText
                                )
                            }
                            onDismiss()
                        },
                        enabled = isEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LxLime,
                            contentColor = LxTextDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Entry", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
