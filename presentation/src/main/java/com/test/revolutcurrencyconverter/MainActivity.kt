package com.test.revolutcurrencyconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.revolutcurrencyconverter.fragment.CurrencyConverterFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .add(CurrencyConverterFragment.newInstance(), CurrencyConverterFragment.TAG)
                .commitNow()
    }
}
