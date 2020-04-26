package com.kulguy.pintarsehat.models

data class FoodModel(
    var defaultPortion: String= "",
    var title: String = "",
    var category: String= "",
    var portions: MutableMap<String, PortionModel> = mutableMapOf(),
    var refId: String = "",
    var clicked: Int = 0
)