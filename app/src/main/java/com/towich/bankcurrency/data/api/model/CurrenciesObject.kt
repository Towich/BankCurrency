package com.towich.bankcurrency.data.api.model

data class CurrenciesObject<T>(
    val Date: String,
    val PreviousDate: String,
    val PreviousURL: String,
    val Timestamp: String,
    val Valute: Map<String, Currency<T>>
)