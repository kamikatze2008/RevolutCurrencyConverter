package com.test.revolutcurrencyconverter.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import kotlinx.android.synthetic.main.view_rates_item.view.*

class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textWatcher: TextWatcher? = null

    fun bind(
        ratesResponseObject: LoadCurrenciesUseCase.RatesResponseObject,
        onClickListener: ((baseName: String, amount: Float) -> Unit),
        onTextEditedListener: ((baseName: String, amount: Float) -> Unit)
    ) {
        itemView.setOnClickListener {
            itemView.editTextContainer.requestFocus()
            onClickListener(
                ratesResponseObject.currency,
                itemView.amountEditText.text.toString().toFloat()
            )
        }
        itemView.currencyTitle.text = ratesResponseObject.currency
        itemView.currencyDescription.text = ratesResponseObject.currency

        textWatcher?.also {
            itemView.amountEditText.removeTextChangedListener(it)
        }

        itemView.amountEditText.setText(
            if (ratesResponseObject.amount.isNaN()) {
                ""
            } else {
                String.format(
                    FORMATTING_PATTERN,
                    ratesResponseObject.amount
                )
            }
        )

        //todo move textwatcher to onCreateViewHolder
        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onTextEditedListener(
                    ratesResponseObject.currency,
                    if (s.isNullOrBlank()) {
                        Float.NaN
                    } else {
                        s.toString().toFloat()
                    }
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }
        itemView.amountEditText.addTextChangedListener(textWatcher)
    }

    companion object {
        private const val FORMATTING_PATTERN = "%.2f"
    }
}