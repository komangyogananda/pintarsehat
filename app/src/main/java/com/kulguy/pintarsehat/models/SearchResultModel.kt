package com.kulguy.pintarsehat.models

import java.io.Serializable

class SearchResultModel(
    var refId: String,
    var title: String,
    var category: String,
    var activePortion: String = "",
    var defaultPortion: String,
    var summary: MutableMap<String, String>,
    var portions: Int = 0,
    var clicked: Int = 0
): Serializable {
    companion object{

        operator fun invoke(foodModel: FoodModel): SearchResultModel{
            val portion = foodModel.portions[foodModel.activePortion]
            val summary = portion?.summary as MutableMap<String, String>
            return SearchResultModel(
                refId = foodModel.refId,
                title = foodModel.title,
                category = foodModel.category,
                defaultPortion = foodModel.defaultPortion,
                summary = summary,
                portions = foodModel.portions.size - 1,
                activePortion = foodModel.activePortion,
                clicked = foodModel.clicked
            )
        }
    }
}