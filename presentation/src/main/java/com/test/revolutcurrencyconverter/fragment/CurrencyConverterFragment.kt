package com.test.revolutcurrencyconverter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.test.revolutcurrenciesconverter.PresentationRatesObject
import com.test.revolutcurrencyconverter.R
import kotlinx.android.synthetic.main.fragment_currency_converter.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyConverterFragment : Fragment() {
    private val currencyConverterViewModel: CurrencyConverterViewModel by viewModel()

    private val adapter = RatesAdapter()

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
        recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        currencyConverterViewModel.loadRatesResult.observe(this, Observer {
            when (it) {
                PresentationRatesObject.Loading -> {
                    //todo show loading
                }
                is PresentationRatesObject.Error -> {
                    //todo show error
                }
                is PresentationRatesObject.Success -> {
                    adapter.setData(it.rates.toList())
                }
            }
        })

        currencyConverterViewModel.loadRates()
    }

    companion object {
        val TAG = CurrencyConverterFragment::class.java.simpleName

        fun newInstance() = CurrencyConverterFragment()
    }
}