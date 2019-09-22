package com.test.revolutcurrencyconverter.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase

class CurrencyConverterViewModel(private val currenciesUseCase: LoadCurrenciesUseCase) : ViewModel() {
    private val loadRatesTrigger = MutableLiveData<RatesRequestData>()

    fun loadRates(baseCurrency: String = BASE_CURRENCY_STRING, baseAmount: Int = BASE_AMOUNT) {
        loadRatesTrigger.postValue(RatesRequestData(baseCurrency, baseAmount))
    }

    data class RatesRequestData(val baseCurrency: String, val baseAmount: Int)

    companion object {
        private const val BASE_CURRENCY_STRING = "EUR"
        private const val BASE_AMOUNT = 100
    }
}