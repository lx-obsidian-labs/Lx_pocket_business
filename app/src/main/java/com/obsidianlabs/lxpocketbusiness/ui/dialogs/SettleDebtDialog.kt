package com.obsidianlabs.lxpocketbusiness.ui.dialogs

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.obsidianlabs.lxpocketbusiness.data.model.Debt
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceBorder
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary

@Composable
fun SettleDebtDialog(
    debt: Debt,
    onDismiss: () -> Unit,
    onSubmitSettlement: (debtId: String, amountMinor: Long, method: String) -> Unit
) {
    var amountText by remember { mutableStateOf((debt.remainingMinor / 100.0).toString()) }
    var methodText by remember { mutableStateOf("Cash") }

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
                    text = "Settle Debt",
                    color = LxTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Customer: ${debt.customerName}",
                    color = LxTextSecondary,
                    fontSize = 14.sp
                )
                Text(
                    text = "Total Remaining: ${debt.formattedRemaining}",
                    color = LxLime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Settlement Amount
                Text(text = "Payment Amount Received (ZAR)", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
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

                // Payment Method
                Text(text = "Payment Method", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = methodText,
                    onValueChange = { methodText = it },
                    placeholder = { Text("e.g. Cash, EFT, Instant Money", color = LxTextMuted) },
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

                Spacer(modifier = Modifier.height(20.dp))

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
                    val isEnabled = parsedAmount != null && parsedAmount <= debt.remainingMinor

                    Button(
                        onClick = {
                            val amountMinor = parsedAmount ?: return@Button
                            onSubmitSettlement(debt.id, amountMinor, methodText)
                            onDismiss()
                        },
                        enabled = isEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LxLime,
                            contentColor = LxTextDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Record Payment", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
