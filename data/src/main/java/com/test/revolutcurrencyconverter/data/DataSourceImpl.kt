package com.test.revolutcurrencyconverter.data

import retrofit2.Response

class DataSourceImpl(private val currenciesApi: CurrenciesApi) :
    DataSource {
    override suspend fun getLatestRates(base: String): Response<LatestRatesResponseObject> {
        return currenciesApi.getLatestRates(base)
    }
}