package com.towich.bankcurrency.data.api

import com.towich.bankcurrency.data.api.model.CurrenciesObject
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("daily_json.js")
    suspend fun getCurrencies(): Response<CurrenciesObject<Double>>
}