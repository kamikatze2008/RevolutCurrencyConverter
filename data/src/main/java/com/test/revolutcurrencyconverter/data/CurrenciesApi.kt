package com.test.revolutcurrencyconverter.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesApi {
    @GET("/latest")
    suspend fun getLatestRates(@Query("base") baseCurrency: String): Response<LatestRatesResponseObject>
}