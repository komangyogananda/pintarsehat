package com.kulguy.pintarsehat.models

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

class SearchResultModel(var title: String, var category: String, var defaultPortion: String, var summary: MutableMap<String, String>, var portions: Int = 0, var clicked: Int = 0) {
    companion object{
        operator fun invoke(documentSnapshot: DocumentSnapshot): SearchResultModel {
            val data = documentSnapshot.data
            val title: String = data?.get("title") as String
            val category: String = data?.get("category") as String
            val defaultPortion: String = data?.get("default_portion") as String
            val portions: HashMap<String, Any> = data?.get("portions") as HashMap<String, Any>
            val portion = portions?.get(defaultPortion) as HashMap<String, Any>
            val summaryFirebase = portion?.get("summary") as HashMap<String, String>
            val summary = summaryFirebase.toMutableMap()
            return SearchResultModel(title = title as String, category = category, defaultPortion = defaultPortion, summary = summary, portions = portions.size - 1)
        }
    }
}