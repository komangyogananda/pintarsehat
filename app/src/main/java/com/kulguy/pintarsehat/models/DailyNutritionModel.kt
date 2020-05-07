package com.kulguy.pintarsehat.models

data class DailyNutritionModel (
    var foods: ArrayList<FoodModel> = arrayListOf(),
    var userId: String = "",
    var date: String = "",
    var refId: String = ""
)