package com.test.revolutcurrenciesconverter

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class LoadCurrenciesUseCase(private val repository: Repository) {

    fun execute(baseName: String, baseAmount: Float) = liveData(context = Dispatchers.IO) {
        emit(PresentationRatesObject.Loading)
        when (val latestDomainRatesObject = repository.getLatestRates(baseName)) {
            is DomainRatesObject.Success -> {

                val rates = mutableListOf(Pair(baseName, baseAmount))

                rates.addAll(latestDomainRatesObject.rates.map {
                    Pair(it.key, it.value * baseAmount)
                })

                emit(PresentationRatesObject.Success(latestDomainRatesObject.baseName, rates))
            }

            is DomainRatesObject.Error -> emit(PresentationRatesObject.Error(latestDomainRatesObject.e))
        }
    }
}