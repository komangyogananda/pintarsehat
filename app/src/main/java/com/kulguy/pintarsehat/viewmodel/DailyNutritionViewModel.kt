package com.kulguy.pintarsehat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.kulguy.pintarsehat.models.DailyNutritionModel

class DailyNutritionViewModel : ViewModel() {

    private var dailyNutrition: MutableLiveData<ArrayList<DailyNutritionModel>>? = null
    private val db = FirebaseFirestore.getInstance()
    private val dailyNutritionFirebaseRef = db.collection("daily_nutritions")
    private var tempResult = arrayListOf<DailyNutritionModel>()

    fun getDailyNutrition(userId: String, date: ArrayList<String>): MutableLiveData<ArrayList<DailyNutritionModel>>? {
        if (dailyNutrition == null){
            dailyNutrition = MutableLiveData()
        }
        fetchDailyNutrition(userId, date)
        return dailyNutrition
    }

    private fun query(chunks: List<List<String>>, userId: String, idx: Int){
        dailyNutritionFirebaseRef
            .whereIn("date", chunks[idx])
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { query ->
                Log.w("firestore", "Suceess" + query.size())
                if (query.size() != 0){
                    query.documents.forEach { doc ->
                        val temp = doc.toObject(DailyNutritionModel::class.java)
                        temp?.let {dailyModel ->
                            tempResult.add(dailyModel)
                        }
                    }
                }
            }.addOnCompleteListener {
                if (idx != chunks.size - 1){
                    query(chunks, userId, idx + 1)
                }else{
                    Log.w("Kelar", idx.toString())
                    Log.w("Kelar", tempResult.size.toString())
                    Log.w("Kelar", tempResult.toString())
                    if (tempResult.size != 0){
                        dailyNutrition?.value = tempResult
                    }else{
                        dailyNutrition?.value = null
                    }
                }
            }
    }

    private fun fetchDailyNutrition(userId: String, date: ArrayList<String>){
        Log.w("firestore", "userId $userId date $date")
        val chunks = date.chunked(10)
        tempResult = arrayListOf()
        query(chunks, userId, 0)
    }

}