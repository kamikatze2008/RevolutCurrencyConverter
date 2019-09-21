package com.test.revolutcurrencyconverter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.test.revolutcurrencyconverter.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyConverterFragment : Fragment() {
    private val currencyConverterViewModel: CurrencyConverterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_converter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {


        currencyConverterViewModel.loadRates()
    }

    companion object {
        val TAG = CurrencyConverterFragment::class.java.simpleName

        fun newInstance() = CurrencyConverterFragment()
    }
}