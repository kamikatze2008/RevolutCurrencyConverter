package com.test.revolutcurrencyconverter.data

import retrofit2.Response

interface DataSource {
    suspend fun getLatestRates(base: String): Response<LatestRatesResponseObject>
}