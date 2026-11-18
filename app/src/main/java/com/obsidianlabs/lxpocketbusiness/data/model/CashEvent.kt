package com.obsidianlabs.lxpocketbusiness.data.model

import java.util.UUID

enum class CashEventType {
    RECEIPT,
    EXPENSE,
    SETTLEMENT,
    OWNER_IN,
    OWNER_OUT,
    REFUND
}

data class CashEvent(
    val id: String = UUID.randomUUID().toString(),
    val type: CashEventType,
    val amountMinor: Long, // Integer cents, positive
    val civilDate: String, // ISO YYYY-MM-DD
    val paymentMethod: String = "Cash",
    val category: String = "General",
    val customerId: String? = null,
    val debtId: String? = null,
    val customerName: String? = null,
    val notes: String = "",
    val reversalOfId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    val formattedAmount: String
        get() = "R${String.format(java.util.Locale.US, "%.2f", amountMinor / 100.0)}"
}
