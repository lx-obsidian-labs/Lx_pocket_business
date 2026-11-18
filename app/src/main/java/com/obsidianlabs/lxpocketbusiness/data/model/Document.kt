package com.obsidianlabs.lxpocketbusiness.data.model

import java.util.UUID
import java.math.BigDecimal
import java.math.RoundingMode

enum class DocumentType {
    QUOTE,
    INVOICE,
    CREDIT_NOTE
}

enum class DocumentStatus {
    DRAFT,
    ISSUED,
    SENT,
    ACCEPTED,
    REJECTED,
    CONVERTED
}

data class DocumentLine(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val quantity: Double = 1.0,
    val unitPriceMinor: Long, // Cents
    val isTaxable: Boolean = false
) {
    val netMinor: Long
        get() = BigDecimal.valueOf(quantity).multiply(BigDecimal.valueOf(unitPriceMinor))
            .setScale(0, RoundingMode.HALF_UP).longValueExact()

    val taxMinor: Long
        get() = if (isTaxable) BigDecimal.valueOf(netMinor).multiply(BigDecimal("0.15"))
            .setScale(0, RoundingMode.HALF_UP).longValueExact() else 0L

    val grossMinor: Long
        get() = netMinor + taxMinor
}

data class Document(
    val id: String = UUID.randomUUID().toString(),
    val type: DocumentType,
    val status: DocumentStatus = DocumentStatus.DRAFT,
    val number: String = "",
    val customerId: String,
    val customerName: String,
    val customerAddress: String = "",
    val customerPhone: String = "",
    val issueDate: String, // YYYY-MM-DD
    val dueDate: String? = null, // YYYY-MM-DD
    val notes: String = "",
    val lines: List<DocumentLine> = emptyList(),
    val sourceQuoteId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    val totalNetMinor: Long
        get() = lines.sumOf { it.netMinor }

    val totalTaxMinor: Long
        get() = lines.sumOf { it.taxMinor }

    val totalGrossMinor: Long
        get() = lines.sumOf { it.grossMinor }

    val formattedTotalGross: String
        get() = "R${String.format(java.util.Locale.US, "%.2f", totalGrossMinor / 100.0)}"
}
