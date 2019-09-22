package com.test.revolutcurrenciesconverter

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class LoadCurrenciesUseCase(private val repository: Repository) {

    fun execute(baseName: String, baseAmount: Float) = liveData(context = Dispatchers.IO) {
        emit(PresentationRatesObject.Loading)
        when (val latestDomainRatesObject = repository.getLatestRates(baseName)) {
            is DomainRatesObject.Success -> {

                val rates = mutableListOf(RatesResponseObject(baseName, baseAmount, 0))

                var counter = 1
                rates.addAll(latestDomainRatesObject.rates.map {
                    RatesResponseObject(it.key, it.value * baseAmount, counter++)
                })

                emit(PresentationRatesObject.Success(latestDomainRatesObject.baseName, rates))
            }

            is DomainRatesObject.Error -> emit(PresentationRatesObject.Error(latestDomainRatesObject.e))
        }
    }

    data class RatesResponseObject(
        val currency: String,
        var amount: Float,
        var position: Int
    )
}