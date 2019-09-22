package com.test.revolutcurrenciesconverter

interface Repository {
    suspend fun getLatestRates(base: String): DomainRatesObject
}