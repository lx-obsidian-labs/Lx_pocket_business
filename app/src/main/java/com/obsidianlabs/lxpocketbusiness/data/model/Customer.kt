package com.obsidianlabs.lxpocketbusiness.data.model

import java.util.UUID

data class Customer(
    val id: String = UUID.randomUUID().toString(),
    val displayName: String,
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val vatNumber: String = "",
    val notes: String = "",
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
