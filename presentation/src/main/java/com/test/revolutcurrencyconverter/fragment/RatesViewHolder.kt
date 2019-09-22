package com.test.revolutcurrencyconverter.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import kotlinx.android.synthetic.main.view_rates_item.view.*

class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(
        ratesResponseObject: LoadCurrenciesUseCase.RatesResponseObject,
        onClickListener: ((baseName: String, amount: Float) -> Unit)?
    ) {
        if (onClickListener != null) {
            itemView.setOnClickListener {
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
        itemView.amountEditText.setText(
            String.format(
                FORMATTING_PATTERN,
                ratesResponseObject.amount
            )
        )
    }

    companion object {
        private const val FORMATTING_PATTERN = "%.2f"
    }
}