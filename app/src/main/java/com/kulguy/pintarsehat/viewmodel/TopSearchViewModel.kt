package com.kulguy.pintarsehat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.kulguy.pintarsehat.models.FoodModel
import com.kulguy.pintarsehat.models.SearchResultModel

class TopSearchViewModel : ViewModel() {

    private var topSearches: MutableLiveData<ArrayList<SearchResultModel>>? = null
    private val db = FirebaseFirestore.getInstance()
    private val foodsRef = db.collection("foods")

    fun getTopSearches(): MutableLiveData<ArrayList<SearchResultModel>>? {
        if (topSearches == null){
            topSearches = MutableLiveData()
        }
        Log.w("firestore", "getTopSearcehs")
        fetchTopSearches()
        return topSearches
    }

    private fun fetchTopSearches(){
        Log.w("firestore", "fetchSearch" )
        val queryResult: ArrayList<SearchResultModel> = arrayListOf()
        foodsRef.limit(10).get().addOnSuccessListener {
            it.documents.forEach { docSnapshot ->
                docSnapshot.toObject(FoodModel::class.java)?.let { foodModel ->
                    foodModel.activePortion = foodModel.defaultPortion
                    SearchResultModel.invoke(
                        foodModel
                    )
                }?.let { searchResultModel -> queryResult.add(searchResultModel) }
                Log.w("firestore", "sucess")
            }
        }.addOnCompleteListener {
            if (!it.isSuccessful){
                Log.w("firestore", "failed")
            }else{
                topSearches?.value = queryResult
            }
        }
    }

}