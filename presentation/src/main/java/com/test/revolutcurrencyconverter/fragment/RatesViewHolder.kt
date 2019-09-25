package com.test.revolutcurrencyconverter.fragment

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import kotlinx.android.synthetic.main.view_rates_item.view.*
import java.util.*

class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var textWatcher: TextWatcher? = null

    fun bind(
        ratesResponseObject: LoadCurrenciesUseCase.RatesResponseObject,
        onClickListener: ((baseName: String, amount: Float) -> Unit),
        onTextEditedListener: ((baseName: String, amount: Float) -> Unit)
    ) {
        itemView.setOnClickListener {
            itemView.editTextContainer.requestFocus()
            onClickListener(
                ratesResponseObject.currency,

                itemView.amountEditText.text.toString().let {
                    if (it.isBlank()) {
                        Float.NaN
                    } else {
                        it.toFloat()
                    }
                }
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
            private var timer = Timer()
            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        onTextEditedListener(
                            ratesResponseObject.currency,
                            if (s.isNullOrBlank()) {
                                Float.NaN
                            } else {
                                s.toString().toFloat()
                            }
                        )
                    }
                }, DELAY)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }
        itemView.amountEditText.addTextChangedListener(textWatcher)
    }

    fun bindText(amount: Float) {
        if (!itemView.amountEditText.isFocused) {
            itemView.amountEditText.setText(
                if (amount.isNaN()) {
                    ""
                } else {
                    String.format(
                        FORMATTING_PATTERN,
                        amount
                    )
                }
            )

            itemView.amountEditText.clearFocus()
        }
    }

    companion object {
        private const val FORMATTING_PATTERN = "%.2f"
        private const val DELAY = 500L
    }
}