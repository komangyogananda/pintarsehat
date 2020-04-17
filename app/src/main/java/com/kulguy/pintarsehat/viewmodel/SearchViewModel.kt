package com.kulguy.pintarsehat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.algolia.search.saas.Client
import com.algolia.search.saas.CompletionHandler
import com.algolia.search.saas.Index
import com.algolia.search.saas.Query
import com.beust.klaxon.Klaxon
import com.google.firebase.firestore.FirebaseFirestore
import com.kulguy.pintarsehat.models.SearchResultModel

class Hints {

}

class SearchViewModel : ViewModel() {

    var searchResults: MutableLiveData<ArrayList<SearchResultModel>>? = null
    val db = FirebaseFirestore.getInstance()
    var client: Client = Client("X85VLA89ED", "73ca9ac8f2e185b84a5bcede09e5e8eb")
    var index: Index = client.getIndex("production_foods_search")

    fun getSearch(query_string: String?, onStartSearch: () -> Boolean = { true }, onFinishedSearch: () -> Boolean = { true }): MutableLiveData<ArrayList<SearchResultModel>>? {
        if (searchResults == null){
            searchResults = MutableLiveData()
        }
        fetchSearch(query_string, onStartSearch, onFinishedSearch)
        return searchResults
    }

    private fun fetchSearch(query_string: String?, onStartSearch: () -> Boolean = { true }, onFinishedSearch: () -> Boolean = { true }){
        val query = Query(query_string)
        onStartSearch()
        index.searchAsync(query, CompletionHandler { jsonObject, algoliaException ->
            if (jsonObject != null) {
                Log.w("Fetch", jsonObject.getString("hits"))
                val searchResultsParsed = Klaxon().parseArray<SearchResultModel>(jsonObject.getString("hits")) as ArrayList<SearchResultModel>
                searchResults?.value = searchResultsParsed
            }
//            searchResults?.value = searchResultsParsed
            Log.w("Fetch", algoliaException.toString())
            onFinishedSearch()
        })
    }

}