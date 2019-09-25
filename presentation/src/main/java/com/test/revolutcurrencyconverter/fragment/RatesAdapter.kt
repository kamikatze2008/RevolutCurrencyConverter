package com.test.revolutcurrencyconverter.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import com.test.revolutcurrencyconverter.R
import kotlinx.android.synthetic.main.view_rates_item.view.*

class RatesAdapter(
    private val onItemClickListener: (baseName: String, amount: Float) -> Unit,
    private val onTextEditedListener: (baseName: String, amount: Float) -> Unit
) :
    RecyclerView.Adapter<RatesViewHolder>() {
    private val data = mutableListOf<LoadCurrenciesUseCase.RatesResponseObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_rates_item, parent, false)
                .apply {
                    amountEditText.filters = INPUT_FILTERS
                }
        return RatesViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(
            data[position], onItemClickListener,
            onTextEditedListener
        )
    }

    override fun onBindViewHolder(
        holder: RatesViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            holder.bindText(payloads[0] as Float)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    fun setData(
        newData: List<LoadCurrenciesUseCase.RatesResponseObject>,
        animated: Boolean = true
    ) {
        if (animated) {
            val diffUtilResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return data[oldItemPosition].currency == newData[newItemPosition].currency
                }

                override fun getOldListSize(): Int = data.size

                override fun getNewListSize(): Int = newData.size

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return data[oldItemPosition].currency == newData[newItemPosition].currency
                            && data[oldItemPosition].amount == newData[newItemPosition].amount
//                            && abs(data[oldItemPosition].amount - newData[newItemPosition].amount) < 0.01
                }

                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                    return newData[newItemPosition].amount
                }
            })
            data.clear()
            data.addAll(newData)
            diffUtilResult.dispatchUpdatesTo(this)
        } else {
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        }
    }

    companion object {
        private val INPUT_FILTERS = arrayOf(DecimalDigitsInputFilter())
    }
}