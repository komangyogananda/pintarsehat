package com.kulguy.pintarsehat.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.adapters.OnSearchResultListener
import com.kulguy.pintarsehat.models.SearchResultModel

class SearchResultViewHolder :
    RecyclerView.ViewHolder, View.OnClickListener {
    private var title: TextView? = null
    private var category: TextView? = null
    private var calorie: Chip? = null
    private var carbo: Chip? = null
    private var fat: Chip? = null
    private var protein: Chip? = null
    private var mOnSearchResultListener: OnSearchResultListener? = null

    constructor(inflater: LayoutInflater, parent: ViewGroup, onSearchResultListener: OnSearchResultListener): super(inflater.inflate(
        R.layout.item_search_result_list, parent, false)) {
        itemView.setOnClickListener(this)
        mOnSearchResultListener = onSearchResultListener
    }


    init {
        title = itemView.findViewById(R.id.search_result_title)
        category = itemView.findViewById(R.id.search_result_category)
        calorie = itemView.findViewById(R.id.chip_calorie)
        carbo = itemView.findViewById(R.id.chip_carbo)
        fat = itemView.findViewById(R.id.chip_fat)
        protein = itemView.findViewById(R.id.chip_protein)
    }

    fun bind(searchResultModel: SearchResultModel) {
        title?.text = searchResultModel.title
        category?.text = searchResultModel.category + " " + itemView.context.resources.getString(
            R.string.bullet_divider
        ) + " " + searchResultModel.defaultPortion
        calorie?.text = "Kal: " + searchResultModel.summary["Kalori"]
        carbo?.text = "Karbo: " + searchResultModel.summary["Karbohidrat"]
        fat?.text = "Lemak: " + searchResultModel.summary["Lemak"]
        protein?.text = "Protein: " + searchResultModel.summary["Protein"]
    }

    override fun onClick(v: View?) {
        mOnSearchResultListener?.onSearchResultClick(adapterPosition)
    }
}