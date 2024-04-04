package com.towich.bankcurrency.data.repository

import com.towich.bankcurrency.data.api.RetrofitInstance
import com.towich.bankcurrency.data.api.model.CurrenciesObject
import retrofit2.Response

class Repository {
    suspend fun getCurrencies(): Response<CurrenciesObject<Double>> {
        return RetrofitInstance.api.getCurrencies()
    }
}