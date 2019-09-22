package com.test.revolutcurrenciesconverter

sealed class DomainRatesObject {
    data class Success(val baseName: String, val rates: Map<String, Float>) : DomainRatesObject()
    object Loading : DomainRatesObject()
    data class Error(val e: Throwable) : DomainRatesObject()
}