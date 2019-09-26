package com.test.revolutcurrencyconverter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.revolutcurrencyconverter.fragment.CurrencyConverterFragment

class MainActivity : AppCompatActivity() {

    var mainFragment: CurrencyConverterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFragment = if (savedInstanceState == null) {

            CurrencyConverterFragment.newInstance().apply {
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.container,
                        this,
                        CurrencyConverterFragment.TAG
                    )
                    .commitNow()
            }
        } else {
            supportFragmentManager.findFragmentById(R.id.container) as CurrencyConverterFragment?
        }
    }
}
