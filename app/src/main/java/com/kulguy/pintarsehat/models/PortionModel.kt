package com.kulguy.pintarsehat.models

data class PortionModel(
    var title: String = "",
    var summary: MutableMap<String, Any> = mutableMapOf(),
    var nutrient: ArrayList<NutrientModel> = arrayListOf()
)