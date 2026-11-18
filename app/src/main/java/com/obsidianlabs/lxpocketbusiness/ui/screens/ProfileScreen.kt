package com.obsidianlabs.lxpocketbusiness.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.obsidianlabs.lxpocketbusiness.data.model.Business
import com.obsidianlabs.lxpocketbusiness.data.model.VatStatus
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAppBackground
import com.obsidianlabs.lxpocketbusiness.ui.components.LxBusinessAvatar
import com.obsidianlabs.lxpocketbusiness.ui.components.LxCard
import com.obsidianlabs.lxpocketbusiness.ui.components.LxPrimaryButton
import com.obsidianlabs.lxpocketbusiness.ui.components.LxScreenHeader
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSpacing
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxOutline
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxType
import java.io.File

@Composable
fun ProfileScreen(
    business: Business?,
    onBackClick: () -> Unit,
    onSave: (Business) -> Unit
) {
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf(business?.name.orEmpty()) }
    var legalName by rememberSaveable { mutableStateOf(business?.legalName.orEmpty()) }
    var sector by rememberSaveable { mutableStateOf(business?.sector.orEmpty()) }
    var registration by rememberSaveable { mutableStateOf(business?.registrationNumber.orEmpty()) }
    var vatStatus by rememberSaveable { mutableStateOf(business?.vatStatus ?: VatStatus.UNCONFIRMED) }
    var vatNumber by rememberSaveable { mutableStateOf(business?.vatNumber.orEmpty()) }
    var address by rememberSaveable { mutableStateOf(business?.billingAddress.orEmpty()) }
    var phone by rememberSaveable { mutableStateOf(business?.phone.orEmpty()) }
    var email by rememberSaveable { mutableStateOf(business?.email.orEmpty()) }
    var currency by rememberSaveable { mutableStateOf(business?.currency.ifNullOrBlank("ZAR")) }
    var terms by rememberSaveable { mutableStateOf(business?.defaultTerms.orEmpty()) }
    var paymentInstructions by rememberSaveable { mutableStateOf(business?.paymentInstructions.orEmpty()) }
    var invoicePrefix by rememberSaveable { mutableStateOf(business?.invoicePrefix.orEmpty()) }
    var quotePrefix by rememberSaveable { mutableStateOf(business?.quotePrefix.orEmpty()) }
    var creditPrefix by rememberSaveable { mutableStateOf(business?.creditNotePrefix.orEmpty()) }
    var nextInvoice by rememberSaveable { mutableStateOf((business?.nextInvoiceNumber ?: 1).toString()) }
    var nextQuote by rememberSaveable { mutableStateOf((business?.nextQuoteNumber ?: 1).toString()) }
    var nextCredit by rememberSaveable { mutableStateOf((business?.nextCreditNoteNumber ?: 1).toString()) }
    var logoPath by rememberSaveable { mutableStateOf(business?.logoPath) }
    var vatExpanded by remember { mutableStateOf(false) }
    var logoError by remember { mutableStateOf<String?>(null) }

    val logoPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            runCatching { copyLogo(context, uri) }
                .onSuccess { logoPath = it; logoError = null }
                .onFailure { logoError = "That image could not be used. Try another file." }
        }
    }

    LxAppBackground {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(LxSpacing.lg)) {
            LxScreenHeader(
                title = "Business profile",
                subtitle = "Details used on documents and reports",
                actions = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LxTextPrimary)
                    }
                }
            )
            Spacer(Modifier.height(LxSpacing.lg))

            LxCard(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(LxSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                    LxBusinessAvatar(business?.copy(name = name, logoPath = logoPath), 56.dp)
                    Spacer(Modifier.width(LxSpacing.md))
                    Column(Modifier.weight(1f)) {
                        Text(name.ifBlank { "Your business" }, style = LxType.cardTitle, color = LxTextPrimary, maxLines = 1)
                        Text(sector.ifBlank { "Add your business sector" }, style = LxType.caption, color = LxTextSecondary)
                    }
                }
                Row(Modifier.padding(horizontal = LxSpacing.lg, vertical = LxSpacing.sm)) {
                    IconButton(onClick = { logoPicker.launch("image/*") }) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Change logo", tint = LxLime)
                    }
                    Text("Change logo", style = LxType.caption, color = LxTextSecondary, modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { logoPath = null }) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Remove logo", tint = LxTextSecondary)
                    }
                }
                logoError?.let {
                    Text(it, style = LxType.caption, color = LxTextPrimary, modifier = Modifier.padding(horizontal = LxSpacing.lg, vertical = LxSpacing.sm))
                }
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxProfileSection("Identity") {
                LxField("Business name *", name, { name = it })
                LxField("Legal name", legalName, { legalName = it }, "Registered or trading legal name")
                LxField("Sector", sector, { sector = it }, "e.g. Retail, Services, Catering")
                LxField("Registration number", registration, { registration = it }, "Optional")
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxProfileSection("Contact and address") {
                LxField("Phone", phone, { phone = it }, keyboardType = KeyboardType.Phone)
                LxField("Email", email, { email = it }, keyboardType = KeyboardType.Email)
                LxField("Billing address", address, { address = it }, "Used on branded documents", maxLines = 3)
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxProfileSection("Tax and currency") {
                Box {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LxCardBg)
                            .border(1.dp, LxOutline, RoundedCornerShape(12.dp))
                            .clickable { vatExpanded = true }
                            .padding(horizontal = LxSpacing.lg),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                when (vatStatus) {
                                    VatStatus.VENDOR -> "VAT vendor"
                                    VatStatus.NON_VENDOR -> "Not a VAT vendor"
                                    VatStatus.UNCONFIRMED -> "VAT status unconfirmed"
                                },
                                style = LxType.body,
                                color = LxTextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = LxTextSecondary)
                        }
                    }
                    DropdownMenu(
                        expanded = vatExpanded,
                        onDismissRequest = { vatExpanded = false },
                        modifier = Modifier.background(LxCardBg)
                    ) {
                        VatStatus.entries.forEach { status ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        when (status) {
                                            VatStatus.VENDOR -> "VAT vendor"
                                            VatStatus.NON_VENDOR -> "Not a VAT vendor"
                                            VatStatus.UNCONFIRMED -> "VAT status unconfirmed"
                                        },
                                        color = LxTextPrimary
                                    )
                                },
                                onClick = { vatStatus = status; vatExpanded = false }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(LxSpacing.md))
                LxField("VAT number", vatNumber, { vatNumber = it }, "Only for confirmed VAT vendors")
                LxField("Currency", currency, { currency = it }, "ZAR")
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxProfileSection("Document setup") {
                LxField("Invoice prefix", invoicePrefix, { invoicePrefix = it })
                LxField("Quote prefix", quotePrefix, { quotePrefix = it })
                LxField("Credit note prefix", creditPrefix, { creditPrefix = it })
                LxField("Next invoice number", nextInvoice, { nextInvoice = it }, keyboardType = KeyboardType.Number)
                LxField("Next quote number", nextQuote, { nextQuote = it }, keyboardType = KeyboardType.Number)
                LxField("Next credit note number", nextCredit, { nextCredit = it }, keyboardType = KeyboardType.Number)
                LxField("Default payment terms", terms, { terms = it }, maxLines = 3)
                LxField("Payment instructions", paymentInstructions, { paymentInstructions = it }, "Bank details or payment reference guidance", maxLines = 3)
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxPrimaryButton(
                text = "Save profile",
                onClick = {
                    val base = business ?: Business(name = name.trim())
                    onSave(
                        base.copy(
                            name = name.trim(),
                            legalName = legalName.trim(),
                            sector = sector.trim(),
                            registrationNumber = registration.trim(),
                            vatStatus = vatStatus,
                            vatNumber = vatNumber.trim(),
                            billingAddress = address.trim(),
                            phone = phone.trim(),
                            email = email.trim(),
                            currency = currency.trim().ifBlank { "ZAR" },
                            defaultTerms = terms.trim(),
                            paymentInstructions = paymentInstructions.trim(),
                            invoicePrefix = invoicePrefix.trim().ifBlank { "INV-" },
                            quotePrefix = quotePrefix.trim().ifBlank { "QT-" },
                            creditNotePrefix = creditPrefix.trim().ifBlank { "CN-" },
                            nextInvoiceNumber = nextInvoice.toIntOrNull()?.coerceAtLeast(1) ?: 1,
                            nextQuoteNumber = nextQuote.toIntOrNull()?.coerceAtLeast(1) ?: 1,
                            nextCreditNoteNumber = nextCredit.toIntOrNull()?.coerceAtLeast(1) ?: 1,
                            logoPath = logoPath
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            )
            Spacer(Modifier.height(LxSpacing.xl))
        }
    }
}

@Composable
private fun LxProfileSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(title, style = LxType.sectionTitle, color = LxTextPrimary)
        Spacer(Modifier.height(LxSpacing.sm))
        LxCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(LxSpacing.lg), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(LxSpacing.md)) {
                content()
            }
        }
    }
}

@Composable
private fun LxField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = LxType.caption) },
        placeholder = placeholder?.let { { Text(it, color = LxTextMuted, style = LxType.body) } },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LxLime,
            unfocusedBorderColor = LxOutline,
            focusedTextColor = LxTextPrimary,
            unfocusedTextColor = LxTextPrimary,
            cursorColor = LxLime
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = maxLines == 1,
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = LxType.body,
        modifier = Modifier.fillMaxWidth()
    )
}

private fun String?.ifNullOrBlank(default: String): String = if (isNullOrBlank()) default else this

private fun copyLogo(context: Context, uri: Uri): String {
    val file = File(context.filesDir, "business_logo_${System.currentTimeMillis()}.img")
    context.contentResolver.openInputStream(uri).use { input ->
        requireNotNull(input)
        file.outputStream().use { output -> input.copyTo(output) }
    }
    return file.absolutePath
}
