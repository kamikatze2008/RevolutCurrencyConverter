package com.test.revolutcurrencyconverter.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_rates_item.view.*

class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(pair: Pair<String, Float>) {
        itemView.currencyTitle.text = pair.first
        itemView.currencyDescription.text = pair.first
        itemView.amountEditText.setText(pair.second.toString())
    }
}