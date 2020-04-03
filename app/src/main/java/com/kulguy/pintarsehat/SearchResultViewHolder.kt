package com.kulguy.pintarsehat

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

class SearchResultViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_search_result_list, parent, false)) {
    private var title: TextView? = null
    private var category: TextView? = null
    private var calorie: Chip? = null
    private var carbo: Chip? = null
    private var fat: Chip? = null
    private var protein: Chip? = null


    init {
        val title: TextView = itemView.findViewById(R.id.search_result_title)
        val category: TextView = itemView.findViewById(R.id.search_result_category)
        val calorie: Chip = itemView.findViewById(R.id.chip_calorie)
        val carbo: Chip = itemView.findViewById(R.id.chip_carbo)
        val fat: Chip = itemView.findViewById(R.id.chip_fat)
        val protein: Chip = itemView.findViewById(R.id.chip_protein)
    }

    fun bind(searchResult: SearchResult) {
        title?.text = searchResult.title
        category?.text = searchResult.category + " " + itemView.context.resources.getString(R.string.bullet_divider) + " " + searchResult.portion
        calorie?.text = "Kal: " + searchResult.summary["calorie"]
        carbo?.text = "Karbo: " + searchResult.summary["carbo"]
        fat?.text = "Lemak: " + searchResult.summary["fat"]
        protein?.text = "Protein: " + searchResult.summary["protein"]
    }
}