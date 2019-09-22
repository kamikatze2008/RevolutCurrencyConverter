package com.test.revolutcurrencyconverter.fragment

import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.*
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import com.test.revolutcurrenciesconverter.PresentationRatesObject

class CurrencyConverterViewModel(private val currenciesUseCase: LoadCurrenciesUseCase) :
    ViewModel() {
    private val loadRatesTrigger = MutableLiveData<RatesRequestData>()
    private val loadRatesResult = loadRatesTrigger.switchMap {
        currenciesUseCase.execute(it.baseCurrency, it.baseAmount)
    }


    fun loadRates(baseCurrency: String = BASE_CURRENCY_STRING, baseAmount: Float = BASE_AMOUNT) {
        loadRatesTrigger.postValue(RatesRequestData(baseCurrency, baseAmount))
    }

    data class RatesRequestData(val baseCurrency: String, val baseAmount: Float)

    companion object {
        private const val BASE_CURRENCY_STRING = "EUR"
        private const val BASE_AMOUNT = 100.0F
    }
}