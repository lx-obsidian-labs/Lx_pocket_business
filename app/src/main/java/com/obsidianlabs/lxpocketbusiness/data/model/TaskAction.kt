package com.obsidianlabs.lxpocketbusiness.data.model

import java.util.UUID

enum class TaskType {
    DUE_DEBT,
    QUOTE_EXPIRY,
    PAYMENT_REMINDER
}

data class TaskAction(
    val id: String = UUID.randomUUID().toString(),
    val type: TaskType,
    val title: String,
    val subtitle: String,
    val dueDate: String,
    val customerId: String? = null,
    val linkedId: String? = null,
    val isCompleted: Boolean = false,
    val isSnoozed: Boolean = false
)
