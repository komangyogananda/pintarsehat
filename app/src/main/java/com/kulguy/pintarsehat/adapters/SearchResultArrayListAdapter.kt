package com.kulguy.pintarsehat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.models.SearchResultModel
import com.kulguy.pintarsehat.viewholders.SearchResultViewHolder

class SearchResultArrayListAdapter(private val searchListModel: ArrayList<SearchResultModel>, private val onSearchResultListener: OnSearchResultListener)
    : RecyclerView.Adapter<SearchResultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchResultViewHolder(
            inflater,
            parent,
            onSearchResultListener
        )
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchResultModel: SearchResultModel = searchListModel[position]
        holder.bind(searchResultModel)
    }

    override fun getItemCount(): Int = searchListModel.size
}

interface OnSearchResultListener {
    fun onSearchResultClick(position: Int);
}