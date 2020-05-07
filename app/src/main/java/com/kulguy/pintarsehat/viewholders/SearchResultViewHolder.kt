package com.kulguy.pintarsehat.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.adapters.OnSearchResultListener
import com.kulguy.pintarsehat.models.SearchResultModel

class SearchResultViewHolder :
    RecyclerView.ViewHolder, View.OnClickListener {
    private var title: TextView? = null
    private var category: TextView? = null
    private var subtitle: TextView? = null
    private var mOnSearchResultListener: OnSearchResultListener? = null

    constructor(inflater: LayoutInflater, parent: ViewGroup, onSearchResultListener: OnSearchResultListener): super(inflater.inflate(
        R.layout.item_search_result_list, parent, false)) {
        itemView.setOnClickListener(this)
        mOnSearchResultListener = onSearchResultListener
    }


    init {
        title = itemView.findViewById(R.id.search_result_title)
        category = itemView.findViewById(R.id.search_result_category)
        subtitle = itemView.findViewById(R.id.food_summary_subtitle)
    }

    fun bind(searchResultModel: SearchResultModel) {
        title?.text = searchResultModel.title
        var categoryText: String = searchResultModel.category + " " + itemView.context.resources.getString(
            R.string.bullet_divider
        ) + " " + searchResultModel.activePortion
        if (searchResultModel.portions > 0){
            categoryText += " " + itemView.context.resources.getString(
                R.string.bullet_divider
            ) + " " + searchResultModel.portions +" porsi lainnya"
        }
        category?.text = categoryText
        var subtitleText: String = "Kalori " + searchResultModel.summary["Kalori"] + " kcal"
        subtitleText += ", Karbohidrat " + searchResultModel.summary["Karbohidrat"]
        subtitleText += ", Protein " + searchResultModel.summary["Protein"]
        subtitleText += ", Lemak " + searchResultModel.summary["Lemak"]
        subtitle?.text = subtitleText

    }

    override fun onClick(v: View?) {
        mOnSearchResultListener?.onSearchResultClick(adapterPosition)
    }
}