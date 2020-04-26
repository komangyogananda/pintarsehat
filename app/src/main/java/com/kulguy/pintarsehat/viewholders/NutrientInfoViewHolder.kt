package com.kulguy.pintarsehat.viewholders

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.adapters.SubNutrientInfoArrayListAdapter
import com.kulguy.pintarsehat.models.NutrientModel

class NutrientInfoViewHolder(inflater: LayoutInflater, parent: ViewGroup)
    : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_food_nutrition_details_list, parent, false)) {

    private var title: TextView? = null
    private var value: TextView? = null
    private var subDetails: RecyclerView? = null

    init {
        title = itemView.findViewById(R.id.title_nutrition_info)
        value = itemView.findViewById(R.id.value_nutrition_info)
        subDetails = itemView.findViewById(R.id.food_nutrition_sub_details)
        val itemDecorator = DividerItemDecoration(itemView.context, DividerItemDecoration.VERTICAL)
        val divider = itemView.context.getDrawable(R.drawable.divider_vertical_4)
        if (divider != null) {
            itemDecorator.setDrawable(divider)
        }
        subDetails?.addItemDecoration(itemDecorator)
    }

    fun bind(nutrient: NutrientModel){
        title?.text = nutrient.title
        value?.text = nutrient.value
        if (nutrient.sub.size == 0){
            subDetails?.visibility = View.GONE
        }else{
            subDetails?.apply {
                layoutManager  = LinearLayoutManager(itemView.context)
                adapter =
                    SubNutrientInfoArrayListAdapter(
                        nutrient.sub
                    )
            }
        }
    }

}