package com.kulguy.pintarsehat.viewholders

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.activities.FoodDetailsActivity

class OtherPortionViewHolder
    : RecyclerView.ViewHolder {

    var text: TextView? = null
    var background: RelativeLayout? = null
    private var mActivity: FoodDetailsActivity? = null

    constructor(activity: FoodDetailsActivity, inflater: LayoutInflater, parent: ViewGroup)
        :super(inflater.inflate(R.layout.item_other_portion, parent, false)) {
        mActivity = activity
    }

    init {
        text = itemView.findViewById(R.id.other_portion_text)
        background = itemView.findViewById(R.id.other_portion_layout_background)
    }

    fun bind(portionName: String, active: Boolean){
        text?.text = portionName
        if (active){
            mActivity?.getColor(R.color.white)?.let { text?.setTextColor(it) }
            background?.background = mActivity?.getDrawable(R.drawable.rounded_blue_frame)
        }

        itemView.setOnClickListener {
            Log.w("OnPortionClick", adapterPosition.toString())
            mActivity?.onPortionClick(adapterPosition)
        }
    }

}