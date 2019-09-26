package com.test.revolutcurrencyconverter.fragment

import android.util.Log
import androidx.lifecycle.*
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase.RatesResponseObject
import com.test.revolutcurrenciesconverter.PresentationRatesObject
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.abs

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
            update()
        }
    }
    private val loadRatesResult = loadRatesTrigger.switchMap {
        currenciesUseCase.execute(it.baseCurrency, it.baseAmount)
    }

    private val ratePositionTrigger = MutableLiveData<String>()

    private val baseCurrencyLiveData = MutableLiveData<BaseCurrencyData>()

    val ratesLiveData: LiveData<PresentationRatesObject> =
        MediatorLiveData<PresentationRatesObject>().apply {
            var result: MutableList<RatesResponseObject> = mutableListOf()
            var shouldBlockUpdates = false

            fun update() {
                viewModelScope.launch {
                    result[0].currency.also {
                        val dataToPost = PresentationRatesObject.Success(
                            it,
                            mutableListOf<RatesResponseObject>().apply {
                                addAll(result)
                            })
                        postValue(dataToPost)
                        baseCurrencyLiveData.postValue(
                            BaseCurrencyData(
                                dataToPost.baseName,
                                dataToPost.rates
                            )
                        )
                    }
                }
            }

            addSource(ratePositionTrigger) { searchedCurrency ->
                viewModelScope.launch {
                    if (!shouldBlockUpdates) {
                        shouldBlockUpdates = true
                        val item = result.find { it.currency == searchedCurrency }
                        if (item != null) {
                            val oldPosition = item.position
                            item.position = 0
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
            }

            addSource(loadRatesResult) { presentationRatesObject ->
                when (presentationRatesObject) {
                    is PresentationRatesObject.Success -> {
                        if (!shouldBlockUpdates) {
                            shouldBlockUpdates = true
                            if (result.isEmpty()) {
                                result.addAll(presentationRatesObject.rates)
                            } else {
                                result = result.map { ratesResponseObject ->
                                    val tempRate =
                                        presentationRatesObject.rates.find { ratesResponseObject.currency == it.currency }

                                    RatesResponseObject(
                                        ratesResponseObject.currency,
                                        tempRate?.amount ?: ratesResponseObject.amount,
                                        ratesResponseObject.position
                                    )
                                }.toMutableList()
                            }

                            update()
                            shouldBlockUpdates = false
                        }
                    }
                    else -> postValue(presentationRatesObject)
                }
            }
        }

    init {
        initTimer()
        loadRates()
    }

    private fun loadRates(
        baseCurrency: String = BASE_CURRENCY_STRING,
        baseAmount: Float = BASE_AMOUNT
    ) {
        preLoadRatesTrigger.postValue(RatesRequestData(baseCurrency, baseAmount))
    }

    fun updatePositionAndLoadRates(baseName: String, baseAmount: Float) {
        ratePositionTrigger.postValue(baseName)
        loadRates(baseName, baseAmount)
    }

    private fun initTimer() {
        viewModelScope.launch {
            // Update the elapsed time every second.
            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    Log.e("ERROR", "${System.currentTimeMillis()}")
                    timerLiveData.postValue(true)
                }
            }, 0L, REQUEST_PERIOD)
        }
    }

    fun onTextEdited(baseName: String, amount: Float) {
        viewModelScope.launch {
            val baseCurrencyData = baseCurrencyLiveData.value
            if (baseCurrencyData != null) {
                if (baseCurrencyData.baseCurrency == baseName) {
                    preLoadRatesTrigger.postValue(RatesRequestData(baseName, amount))
                } else {
                    val oldCurrencyRate =
                        baseCurrencyData.latestRates.find { it.currency == baseName }
                    val baseCurrencyRate =
                        baseCurrencyData.latestRates.find { it.currency == baseCurrencyData.baseCurrency }
                    if (oldCurrencyRate != null && baseCurrencyRate != null) {
                        val baseAmount = abs(baseCurrencyRate.amount)
                        val oldAmount = if (oldCurrencyRate.amount == 0F) {
                            -1F
                        } else {
                            oldCurrencyRate.amount
                        }
                        preLoadRatesTrigger.postValue(
                            RatesRequestData(
                                baseCurrencyData.baseCurrency,
                                amount * baseAmount / oldAmount
                            )
                        )
                    }
                }
            }
        }
    }

    data class BaseCurrencyData(
        val baseCurrency: String,
        val latestRates: List<RatesResponseObject>
    )

    data class RatesRequestData(val baseCurrency: String, val baseAmount: Float)

    companion object {
        private const val BASE_CURRENCY_STRING = "EUR"
        private const val BASE_AMOUNT = 100.0F

        private const val REQUEST_PERIOD = 1000L
    }
}