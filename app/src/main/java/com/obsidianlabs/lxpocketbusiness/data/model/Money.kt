package com.obsidianlabs.lxpocketbusiness.data.model

import java.math.BigDecimal
import java.math.RoundingMode

fun parseMoneyMinor(text: String): Long? {
    val normalized = text.trim().replace(',', '.')
    if (normalized.length > 24 || !Regex("(?:\\d+(?:\\.\\d{0,2})?|\\.\\d{1,2})").matches(normalized)) return null
    return try {
        normalized.toBigDecimal().movePointRight(2).longValueExact().takeIf { it > 0 }
    } catch (_: ArithmeticException) {
        null
    } catch (_: NumberFormatException) {
        null
    }
}

fun formatMoneyMinor(amountMinor: Long): String {
    val value = BigDecimal.valueOf(amountMinor).movePointLeft(2).setScale(2, RoundingMode.UNNECESSARY)
    return "R${value.toPlainString()}"
}
