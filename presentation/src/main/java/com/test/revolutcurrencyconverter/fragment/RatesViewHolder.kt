package com.test.revolutcurrencyconverter.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import kotlinx.android.synthetic.main.view_rates_item.view.*

class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var isTextChangedListenerActive = true

    fun bind(
        ratesResponseObject: LoadCurrenciesUseCase.RatesResponseObject,
        onClickListener: ((baseName: String, amount: Float) -> Unit)?,
        onTextEditedListener: ((baseName: String, amount: Float) -> Unit)
    ) {
        if (onClickListener != null) {
            itemView.setOnClickListener {
                itemView.editTextContainer.requestFocus()
                onClickListener(
                    ratesResponseObject.currency,
                    itemView.amountEditText.text.toString().toFloat()
                )
            }
        } else {
            itemView.setOnClickListener(null)
        }
        itemView.currencyTitle.text = ratesResponseObject.currency
        itemView.currencyDescription.text = ratesResponseObject.currency
        isTextChangedListenerActive = false
        itemView.amountEditText.setText(
            String.format(
                FORMATTING_PATTERN,
                ratesResponseObject.amount
            )
        )
        isTextChangedListenerActive = true

        itemView.amountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isTextChangedListenerActive) {
                    onTextEditedListener(
                        ratesResponseObject.currency,
                        if (s.isNullOrBlank()) {
                            Float.NaN
                        } else {
                            s.toString().toFloat()
                        }
                    )
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    companion object {
        private const val FORMATTING_PATTERN = "%.2f"
    }
}