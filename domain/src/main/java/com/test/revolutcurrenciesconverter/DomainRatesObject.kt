package com.test.revolutcurrenciesconverter

sealed class DomainRatesObject {
    data class Success(val baseName: String, val rates: Map<String, Float>) : DomainRatesObject()
    data class Error(val e: Throwable) : DomainRatesObject()
}