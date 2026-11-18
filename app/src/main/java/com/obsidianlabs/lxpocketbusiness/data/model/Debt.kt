package com.obsidianlabs.lxpocketbusiness.data.model

import java.util.UUID

enum class DebtOrigin {
    MANUAL,
    INVOICE
}

data class Debt(
    val id: String = UUID.randomUUID().toString(),
    val customerId: String,
    val customerName: String,
    val origin: DebtOrigin = DebtOrigin.MANUAL,
    val invoiceId: String? = null,
    val principalMinor: Long, // Initial debt amount in cents
    val settledMinor: Long = 0L, // Amount settled in cents
    val incurredDate: String, // ISO YYYY-MM-DD
    val dueDate: String? = null, // ISO YYYY-MM-DD
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    val remainingMinor: Long
        get() = (principalMinor - settledMinor).coerceAtLeast(0L)

    val isSettled: Boolean
        get() = remainingMinor == 0L

    val formattedRemaining: String
        get() = "R${String.format(java.util.Locale.US, "%.2f", remainingMinor / 100.0)}"

    val formattedPrincipal: String
        get() = "R${String.format(java.util.Locale.US, "%.2f", principalMinor / 100.0)}"
}
