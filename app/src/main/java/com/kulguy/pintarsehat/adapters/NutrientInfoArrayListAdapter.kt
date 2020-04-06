package com.kulguy.pintarsehat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.models.NutrientModel
import com.kulguy.pintarsehat.viewholders.NutrientInfoViewHolder


class NutrientInfoArrayListAdapter(private val nutrientList: ArrayList<NutrientModel>) :  RecyclerView.Adapter<NutrientInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutrientInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NutrientInfoViewHolder(
            inflater,
            parent
        )
    }

    override fun getItemCount(): Int {
        return nutrientList.size
    }

    override fun onBindViewHolder(holder: NutrientInfoViewHolder, position: Int) {
        val nutrient = nutrientList[position]
        holder.bind(nutrient)
    }
}