package com.kulguy.pintarsehat

import android.content.Context
import android.content.res.Resources
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.android.material.chip.Chip

class SearchResultListAdapter(private var mContext: Context, private var resource: Int, private var searchResultList: List<SearchResult>)
    : ArrayAdapter<SearchResult>(mContext, resource, searchResultList) {

    public override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val view: View = layoutInflater.inflate(resource, null, false)

        val title: TextView = view.findViewById(R.id.search_result_title)
        val category: TextView = view.findViewById(R.id.search_result_category)
        val calorie: Chip = view.findViewById(R.id.chip_calorie)
        val carbo: Chip = view.findViewById(R.id.chip_carbo)
        val fat: Chip = view.findViewById(R.id.chip_fat)
        val protein: Chip = view.findViewById(R.id.chip_protein)

        val searchResult: SearchResult = searchResultList[position]

        title.text = searchResult.title
        category.text = searchResult.category + " " + mContext.resources.getString(R.string.bullet_divider) + " " + searchResult.portion
        calorie.text = "Kal: " + searchResult.summary["calorie"]
        carbo.text = "Karbo: " + searchResult.summary["carbo"]
        fat.text = "Lemak: " + searchResult.summary["fat"]
        protein.text = "Protein: " + searchResult.summary["protein"]

        return view
    }
}