package com.obsidianlabs.lxpocketbusiness.data.model

data class AppSnapshot(
    val version: Int = 1,
    val onboardingCompleted: Boolean = false,
    val onboardingStep: String = "WELCOME",
    val acceptedTermsVersion: String? = null,
    val acceptedTermsDigest: String? = null,
    val acceptedTermsAt: Long? = null,
    val business: Business? = null,
    val customers: List<Customer> = emptyList(),
    val catalogueItems: List<CatalogueItem> = emptyList(),
    val cashEvents: List<CashEvent> = emptyList(),
    val debts: List<Debt> = emptyList(),
    val documents: List<Document> = emptyList(),
    val tasks: List<TaskAction> = emptyList()
)
