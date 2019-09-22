package com.test.revolutcurrencyconverter.fragment

import androidx.lifecycle.*
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase.RatesResponseObject
import com.test.revolutcurrenciesconverter.PresentationRatesObject
import java.util.*


class CurrencyConverterViewModel(private val currenciesUseCase: LoadCurrenciesUseCase) :
    ViewModel() {

    private val timerLiveData = MutableLiveData<Boolean>()
    private val preLoadRatesTrigger = MutableLiveData<RatesRequestData>()
    private val loadRatesTrigger = MediatorLiveData<RatesRequestData>().apply {
        var ratesRequestData: RatesRequestData? = null

        fun update() {
            ratesRequestData?.also {
                postValue(it)
            }
        }

        addSource(timerLiveData) {
            update()
        }

        addSource(preLoadRatesTrigger) {
            ratesRequestData = it

        }
    }
    private val loadRatesResult = loadRatesTrigger.switchMap {
        currenciesUseCase.execute(it.baseCurrency, it.baseAmount)
    }

    private val ratePositionTrigger = MutableLiveData<String>()

    val ratesLiveData: LiveData<PresentationRatesObject> =
        MediatorLiveData<PresentationRatesObject>().apply {
            val result = mutableListOf<RatesResponseObject>()
            var shouldBlockUpdates = false

            fun update() {
                result[0].currency.also {
                    postValue(PresentationRatesObject.Success(it, result))
                }
            }

            addSource(ratePositionTrigger) { searchedCurrency ->
                if (!shouldBlockUpdates) {
                    shouldBlockUpdates = true
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
                    shouldBlockUpdates = false
                }
            }

            addSource(loadRatesResult) { presentationRatesObject ->
                when (presentationRatesObject) {
                    is PresentationRatesObject.Success -> {
                        if (!shouldBlockUpdates) {
                            shouldBlockUpdates = true
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
                            shouldBlockUpdates = false
                        }
                    }
                    else -> postValue(presentationRatesObject)
                }
            }
        }

    fun loadRates(baseCurrency: String = BASE_CURRENCY_STRING, baseAmount: Float = BASE_AMOUNT) {
        preLoadRatesTrigger.postValue(RatesRequestData(baseCurrency, baseAmount))
    }

    fun updatePositionAndLoadRates(baseName: String, baseAmount: Float) {
        ratePositionTrigger.postValue(baseName)
        loadRates(baseName, baseAmount)
    }

    fun initTimer() {

        // Update the elapsed time every second.
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timerLiveData.postValue(true)
            }
        }, 0L, REQUEST_PERIOD)
    }

    data class RatesRequestData(val baseCurrency: String, val baseAmount: Float)

    companion object {
        private const val BASE_CURRENCY_STRING = "EUR"
        private const val BASE_AMOUNT = 100.0F

        private const val REQUEST_PERIOD = 1000L
    }
}