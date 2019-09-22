package com.test.revolutcurrencyconverter.fragment

import androidx.lifecycle.*
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase.RatesResponseObject
import com.test.revolutcurrenciesconverter.PresentationRatesObject

class CurrencyConverterViewModel(private val currenciesUseCase: LoadCurrenciesUseCase) :
    ViewModel() {
    private val loadRatesTrigger = MutableLiveData<RatesRequestData>()
    private val loadRatesResult = loadRatesTrigger.switchMap {
        currenciesUseCase.execute(it.baseCurrency, it.baseAmount)
    }

    private val ratePositionTrigger = MutableLiveData<String>()

    val ratesLiveData: LiveData<PresentationRatesObject> =
        MediatorLiveData<PresentationRatesObject>().apply {
            val result = mutableListOf<RatesResponseObject>()
            var baseName: String? = null

            fun update() {
                result[0].currency.also {
                    postValue(PresentationRatesObject.Success(it, result))
                }
            }

            addSource(ratePositionTrigger) { searchedCurrency ->
                val item = result.find { it.currency == searchedCurrency }
                if (item != null) {
                    val oldPosition = item.position
                    item.position = 0;
                    result.removeAt(oldPosition)
                    result.add(0, item)
                    for (i in 1 until oldPosition + 1) {
                        result[i].position++
                    }
                    update()
                }
            }

            addSource(loadRatesResult) { presentationRatesObject ->
                when (presentationRatesObject) {
                    is PresentationRatesObject.Success -> {
                        if (result.isEmpty()) {
                            result.addAll(presentationRatesObject.rates)
                        } else {
                            result.forEach { ratesResponseObject ->
                                val tempRate =
                                    presentationRatesObject.rates.find { ratesResponseObject.currency == it.currency }
                                ratesResponseObject.amount =
                                    tempRate?.amount ?: ratesResponseObject.amount
                            }
                        }

                        update()
                    }
                    else -> postValue(presentationRatesObject)
                }
            }
        }

    fun loadRates(baseCurrency: String = BASE_CURRENCY_STRING, baseAmount: Float = BASE_AMOUNT) {
        loadRatesTrigger.postValue(RatesRequestData(baseCurrency, baseAmount))
    }

    fun updatePositionAndLoadRates(baseName: String, baseAmount: Float) {
        ratePositionTrigger.postValue(baseName)
//        loadRates(baseName, baseAmount)
    }

    data class RatesRequestData(val baseCurrency: String, val baseAmount: Float)

    companion object {
        private const val BASE_CURRENCY_STRING = "EUR"
        private const val BASE_AMOUNT = 100.0F
    }
}