package com.kulguy.pintarsehat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.kulguy.pintarsehat.models.DailyNutritionModel

class DailyNutritionViewModel : ViewModel() {

    private var dailyNutrition: MutableLiveData<DailyNutritionModel>? = null
    private val db = FirebaseFirestore.getInstance()
    private val dailyNutritionFirebaseRef = db.collection("daily_nutritions")

    fun getDailyNutrition(userId: String, date: String): MutableLiveData<DailyNutritionModel>? {
        if (dailyNutrition == null){
            dailyNutrition = MutableLiveData()
        }
        fetchDailyNutrition(userId, date)
        return dailyNutrition
    }

    private fun fetchDailyNutrition(userId: String, date: String){
        Log.w("firestore", "userId $userId date $date")
        dailyNutritionFirebaseRef
            .whereEqualTo("userId", userId)
            .whereEqualTo("date", date)
            .limit(1)
            .get()
            .addOnSuccessListener {
                Log.w("firestore", "Suceess" + it.size())
                if (it.size() == 0){
                    dailyNutrition?.value = null
                }else{
                    val doc = it.documents[0]
                    dailyNutrition?.value = doc.toObject(DailyNutritionModel::class.java)
                }
            }
    }

}