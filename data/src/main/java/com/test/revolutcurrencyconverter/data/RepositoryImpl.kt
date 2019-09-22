package com.test.revolutcurrencyconverter.data

import com.test.revolutcurrenciesconverter.DomainRatesObject
import com.test.revolutcurrenciesconverter.Repository

class RepositoryImpl(private val dataSource: DataSource) : Repository {
    override suspend fun getLatestRates(base: String): DomainRatesObject {
        return try {
            val response = dataSource.getLatestRates(base)
            if (response.isSuccessful) {
                val ratesObject = response.body()
                ratesObject?.let {
                    DomainRatesObject.Success(
                        it.baseName,
                        it.rates
                    )
                } ?: DomainRatesObject.Error(Exception("empty"))

            } else {
                DomainRatesObject.Error(Exception(response.errorBody().toString()))
            }


        } catch (e: Exception) {
            DomainRatesObject.Error(e)
        }
    }
}