package com.kulguy.pintarsehat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.models.SubNutrientModel
import com.kulguy.pintarsehat.viewholders.SubNutrientInfoViewHolder

class SubNutrientInfoArrayListAdapter(private val subNutrientList: ArrayList<SubNutrientModel>)
    : RecyclerView.Adapter<SubNutrientInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubNutrientInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SubNutrientInfoViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return subNutrientList.size
    }

    override fun onBindViewHolder(holder: SubNutrientInfoViewHolder, position: Int) {
        val subNutrient = subNutrientList[position]
        holder.bind(subNutrient)
    }

}