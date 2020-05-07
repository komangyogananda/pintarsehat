package com.kulguy.pintarsehat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.kulguy.pintarsehat.models.FoodModel
import com.kulguy.pintarsehat.models.SearchResultModel

class FoodDetailsViewModel : ViewModel() {

    private var food: MutableLiveData<FoodModel>? = null
    private val db = FirebaseFirestore.getInstance()
    private val foodsRef = db.collection("foods")

    fun getFood(refId: String, onStartSearch: () -> Boolean = {true}, onFinishedSearch: () -> Boolean = { true }): MutableLiveData<FoodModel>? {
        if (food == null){
            food = MutableLiveData()
        }
        Log.w("firestore", "getTopSearcehs")
        Log.w("firestore", refId)
        Log.w("firestore", onStartSearch.toString())
        fetchFood(refId, onStartSearch, onFinishedSearch)
        return food
    }

    private fun fetchFood(refId: String, onStartSearch: () -> Boolean, onFinishedSearch: () -> Boolean){
        onStartSearch()
        var result: FoodModel? = null
        foodsRef.document(refId).get().addOnSuccessListener {
            result = it.toObject(FoodModel::class.java)
            result?.activePortion = result?.defaultPortion!!
        }.addOnCompleteListener{
            onFinishedSearch()
            if (!it.isSuccessful){
                Log.w("firestore", "failed")
            }else{
                food?.value = result as FoodModel
            }
        }
    }

}