package com.test.revolutcurrencyconverter.data

import com.squareup.moshi.Json

data class LatestRatesResponseObject(
    @field:Json(name = "base") val baseName: String,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "rates") val rates: List<RatesResponseObject>
)