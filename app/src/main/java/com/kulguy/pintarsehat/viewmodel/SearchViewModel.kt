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
    var client: Client = Client(, )
    var index: Index = client.getIndex("production_foods_search")
    var last_query: String? = ""

    public fun getSearch(query_string: String?): MutableLiveData<ArrayList<SearchResultModel>>? {
        if (searchResults == null){
            searchResults = MutableLiveData()
        }
        if (last_query != query_string){
            last_query = query_string
            fetchSearch(query_string)
        }
        return searchResults
    }

    fun fetchSearch(query_string: String?){
        val query = Query(query_string)
        Log.w("Fetch", "init")
        index.searchAsync(query, CompletionHandler { jsonObject, algoliaException ->
            if (jsonObject != null) {
                Log.w("Fetch", jsonObject.getString("hits"))
                val searchResultsParsed = Klaxon().parseArray<SearchResultModel>(jsonObject.getString("hits")) as ArrayList<SearchResultModel>
                searchResults?.value = searchResultsParsed
            }
//            searchResults?.value = searchResultsParsed
            Log.w("Fetch", algoliaException.toString())
        })
    }

}