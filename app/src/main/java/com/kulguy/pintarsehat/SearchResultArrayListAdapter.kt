package com.kulguy.pintarsehat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchResultArrayListAdapter(private val searchList: ArrayList<SearchResult>)
    : RecyclerView.Adapter<SearchResultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchResultViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchResult: SearchResult = searchList[position]
        holder.bind(searchResult)
    }

    override fun getItemCount(): Int = searchList.size
}