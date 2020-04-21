package com.kulguy.pintarsehat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.kulguy.pintarsehat.models.SearchResultModel

class TopSearchViewModel : ViewModel() {

    private var topSearches: MutableLiveData<ArrayList<SearchResultModel>>? = null
    private val db = FirebaseFirestore.getInstance()
    private val foodsRef = db.collection("foods")

    fun getTopSearches(query_string: () -> Boolean, onStartSearch: () -> Boolean = { true }, onFinishedSearch: () -> Boolean = { true }): MutableLiveData<ArrayList<SearchResultModel>>? {
        if (topSearches == null){
            topSearches = MutableLiveData()
        }
        fetchTopSearches(onStartSearch, onFinishedSearch)
        return topSearches
    }

    private fun fetchTopSearches(onStartSearch: () -> Boolean = { true }, onFinishedSearch: () -> Boolean = { true }){
        Log.w("firestore", "fetchSearch" )
        onStartSearch()
        val queryResult: ArrayList<SearchResultModel> = arrayListOf()
        foodsRef.limit(10).get().addOnSuccessListener {
            it.documents.forEach { docSnapshot ->
                Log.w("firestore", docSnapshot.data.toString())
                queryResult.add(SearchResultModel.invoke(docSnapshot))
            }
        }.addOnCompleteListener {
            onFinishedSearch();
            if (!it.isSuccessful){
                Log.w("firestore", "failed")
            }else{
                topSearches?.value = queryResult
            }
        }
    }

}