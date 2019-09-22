package com.test.revolutcurrencyconverter.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import com.test.revolutcurrencyconverter.R
import kotlin.math.abs

class RatesAdapter(private val onItemClickListener: (baseName: String, amount: Float) -> Unit) :
    RecyclerView.Adapter<RatesViewHolder>() {
    private val data = mutableListOf<LoadCurrenciesUseCase.RatesResponseObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_rates_item, parent, false)
        return RatesViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(
            data[position], if (position == 0) {
                null
            } else {
                onItemClickListener
            }
        )
    }

    fun setData(
        newData: List<LoadCurrenciesUseCase.RatesResponseObject>,
        animated: Boolean = true
    ) {
        if (animated) {
            val diffUtilResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return true
                }

                override fun getOldListSize(): Int = data.size

                override fun getNewListSize(): Int = newData.size

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return data[oldItemPosition].currency == newData[newItemPosition].currency
                            && abs(data[newItemPosition].amount - newData[newItemPosition].amount) < 0.01
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
}