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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.obsidianlabs.lxpocketbusiness.data.model.CatalogueItem
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceBorder
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary

@Composable
fun AddCatalogueItemDialog(
    onDismiss: () -> Unit,
    onSubmitItem: (CatalogueItem) -> Unit
) {
    var nameText by remember { mutableStateOf("") }
    var priceText by remember { mutableStateOf("") }
    var unitText by remember { mutableStateOf("Item") }
    var skuText by remember { mutableStateOf("") }
    var isTaxable by remember { mutableStateOf(true) }

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
                    text = "Add Item to Catalogue",
                    color = LxTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Item Name *
                Text(text = "Item / Service Name *", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    placeholder = { Text("e.g. Electrical Repair Service", color = LxTextMuted) },
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

                // Price (ZAR)
                Text(text = "Unit Price (ZAR) *", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
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

                // Unit
                Text(text = "Unit of Measure", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = unitText,
                    onValueChange = { unitText = it },
                    placeholder = { Text("e.g. Hour, Box, Service, Meter", color = LxTextMuted) },
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

                // Taxable checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isTaxable,
                        onCheckedChange = { isTaxable = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = LxLime,
                            uncheckedColor = LxTextSecondary,
                            checkmarkColor = LxTextDark
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Standard Taxable Item (15% VAT)",
                        color = LxTextPrimary,
                        fontSize = 13.sp
                    )
                }

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

                    val parsedPrice = parseMoneyMinor(priceText)
                    val isEnabled = nameText.isNotBlank() && parsedPrice != null

                    Button(
                        onClick = {
                            val priceMinor = parsedPrice ?: return@Button
                            val item = CatalogueItem(
                                name = nameText,
                                sku = skuText,
                                unit = unitText,
                                unitPriceMinor = priceMinor,
                                isTaxable = isTaxable
                            )
                            onSubmitItem(item)
                            onDismiss()
                        },
                        enabled = isEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LxLime,
                            contentColor = LxTextDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add to Catalogue", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
