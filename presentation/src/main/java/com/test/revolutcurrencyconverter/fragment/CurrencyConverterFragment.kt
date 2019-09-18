package com.test.revolutcurrencyconverter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.revolutcurrencyconverter.R

class CurrencyConverterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_converter, container, false)
    }

    companion object {
        val TAG = CurrencyConverterFragment::class.java.simpleName

        fun newInstance() = CurrencyConverterFragment()
    }
}