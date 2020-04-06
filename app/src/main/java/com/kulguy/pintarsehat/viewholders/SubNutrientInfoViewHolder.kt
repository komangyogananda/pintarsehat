package com.kulguy.pintarsehat.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.models.SubNutrientModel

class SubNutrientInfoViewHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_food_nutrition_sub_details_list, parent, false)) {

    private var title: TextView? = null
    private var value: TextView? = null

    init {
        title = itemView.findViewById(R.id.title_sub_nutrition_info)
        value = itemView.findViewById(R.id.value_sub_nutrition_info)
    }

    fun bind(subNutrient: SubNutrientModel){
        title?.text = subNutrient.title
        value?.text = subNutrient.value
    }

}