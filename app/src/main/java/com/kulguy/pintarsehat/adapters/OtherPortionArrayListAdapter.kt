package com.kulguy.pintarsehat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.activities.FoodDetailsActivity
import com.kulguy.pintarsehat.viewholders.OtherPortionViewHolder

class OtherPortionArrayListAdapter(private val activity: FoodDetailsActivity, private val portions: ArrayList<Pair<String, Boolean>>)
    : RecyclerView.Adapter<OtherPortionViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherPortionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return OtherPortionViewHolder(
            activity,
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: OtherPortionViewHolder, position: Int) {
        val portionModel: Pair<String, Boolean> = portions[position]
        holder.bind(portionModel.first, portionModel.second)
    }

    override fun getItemCount(): Int = portions.size
}