package com.kulguy.pintarsehat.models

data class NutrientModel(
    var title: String = "",
    var sub: ArrayList<SubNutrientModel> = arrayListOf(),
    var value: String = ""
)