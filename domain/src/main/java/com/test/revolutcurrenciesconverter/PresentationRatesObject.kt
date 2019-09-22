package com.test.revolutcurrenciesconverter

sealed class PresentationRatesObject {
    data class Success(val baseName: String, val rates: Map<String, Float>) : PresentationRatesObject()
    object Loading : PresentationRatesObject()
    data class Error(val e: Throwable) : PresentationRatesObject()
}