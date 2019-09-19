package com.test.revolutcurrencyconverter.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.revolutcurrencyconverter.R

class RatesAdapter : RecyclerView.Adapter<RatesViewHolder>() {
    private val data = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.view_rates_item, parent, false)
        return RatesViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
//        holder.bind()
    }

    fun setData(newData: List<Any>, animated: Boolean = true) {
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
                    return true
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