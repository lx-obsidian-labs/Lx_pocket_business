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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.obsidianlabs.lxpocketbusiness.data.model.Customer
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceBorder
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary

@Composable
fun AddCustomerDialog(
    onDismiss: () -> Unit,
    onSubmitCustomer: (Customer) -> Unit
) {
    var nameText by remember { mutableStateOf("") }
    var phoneText by remember { mutableStateOf("") }
    var emailText by remember { mutableStateOf("") }
    var addressText by remember { mutableStateOf("") }

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
                    text = "Add Customer",
                    color = LxTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Name *
                Text(text = "Customer Name *", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    placeholder = { Text("Enter full name or shop name", color = LxTextMuted) },
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

                // Phone
                Text(text = "Phone Number (Optional)", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = phoneText,
                    onValueChange = { phoneText = it },
                    placeholder = { Text("e.g. 082 123 4567", color = LxTextMuted) },
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

                // Email
                Text(text = "Email Address (Optional)", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = emailText,
                    onValueChange = { emailText = it },
                    placeholder = { Text("e.g. customer@example.com", color = LxTextMuted) },
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

                // Address
                Text(text = "Physical / Billing Address", color = LxTextSecondary, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = addressText,
                    onValueChange = { addressText = it },
                    placeholder = { Text("Address details...", color = LxTextMuted) },
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

                    Button(
                        onClick = {
                            val newCustomer = Customer(
                                displayName = nameText.trim(),
                                phone = phoneText,
                                email = emailText,
                                address = addressText
                            )
                            onSubmitCustomer(newCustomer)
                            onDismiss()
                        },
                        enabled = nameText.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LxLime,
                            contentColor = LxTextDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Customer", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
