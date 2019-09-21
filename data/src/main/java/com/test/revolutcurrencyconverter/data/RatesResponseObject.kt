package com.test.revolutcurrencyconverter.data

import com.squareup.moshi.Json

data class RatesResponseObject(
    @field:Json(name = "EUR") val eur: String,
    @field:Json(name = "AUD") val aud: String,
    @field:Json(name = "BGN") val bgn: String,
    @field:Json(name = "BLR") val blr: String,
    @field:Json(name = "CAD") val cad: String
)