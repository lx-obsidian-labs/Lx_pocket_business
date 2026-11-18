package com.obsidianlabs.lxpocketbusiness.data.model

import java.util.UUID

data class CatalogueItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val sku: String = "",
    val unit: String = "Item",
    val unitPriceMinor: Long, // Money in cents (e.g. R150.00 = 15000)
    val isTaxable: Boolean = true,
    val isArchived: Boolean = false
) {
    val formattedPrice: String
        get() = "R${String.format(java.util.Locale.US, "%.2f", unitPriceMinor / 100.0)}"
}
