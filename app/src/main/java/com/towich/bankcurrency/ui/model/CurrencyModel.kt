package com.towich.bankcurrency.ui.model

data class CurrencyModel(
    val name: String,
    val charCode: String,
    val value: Double,
    val previous: Double
)
