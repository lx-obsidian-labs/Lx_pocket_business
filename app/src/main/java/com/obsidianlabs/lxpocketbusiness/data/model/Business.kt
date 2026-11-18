package com.obsidianlabs.lxpocketbusiness.data.model

import java.util.UUID

enum class VatStatus {
    UNCONFIRMED,
    NON_VENDOR,
    VENDOR
}

data class Business(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val legalName: String = "",
    val sector: String = "",
    val logoPath: String? = null,
    val registrationNumber: String = "",
    val vatStatus: VatStatus = VatStatus.UNCONFIRMED,
    val vatNumber: String = "",
    val billingAddress: String = "",
    val phone: String = "",
    val email: String = "",
    val currency: String = "ZAR",
    val defaultTerms: String = "Payment due within 14 days of invoice date.",
    val paymentInstructions: String = "",
    val nextInvoiceNumber: Int = 1,
    val nextQuoteNumber: Int = 1,
    val nextCreditNoteNumber: Int = 1,
    val invoicePrefix: String = "INV-",
    val quotePrefix: String = "QT-",
    val creditNotePrefix: String = "CN-"
)
