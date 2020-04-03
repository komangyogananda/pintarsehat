package com.kulguy.pintarsehat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_food_details.*

class FoodDetailsActivity : AppCompatActivity() {

    var food: Food? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_details)

        initData()
        updateUI(food)
    }

    private fun initData(){
        val lemakSub: MutableMap<String, String> = mutableMapOf()
        lemakSub["Lemak Jenuh Baik"] = "10g"
        lemakSub["Lemak Jenuh Tidak Baik"] = "10g"
        val karboSub: MutableMap<String, String> = mutableMapOf()
        lemakSub["Gula"] = "90g"

        val lemak: Nutrient = Nutrient("Lemak", "100g", lemakSub)
        val energi: Nutrient = Nutrient("Energi", "200kj")
        val karbohidrat: Nutrient = Nutrient("Karbohidrat", "100g", karboSub)

        val summary: ArrayList<Pair<String, String>> = ArrayList()
        summary.add(Pair("Kalori", "100kj"))
        summary.add(Pair("Karbohidrat", "100g"))
        summary.add(Pair("Lemak", "32g"))
        summary.add(Pair("Protein", "93g"))

        val listOfNutrient = ArrayList<Nutrient>()
        listOfNutrient.add(energi)
        listOfNutrient.add(karbohidrat)
        listOfNutrient.add(lemak)

        val oneGram: Portion = Portion("1 gram", summary, listOfNutrient)
        val oneMangkok: Portion = Portion("1 Mangkok", summary, listOfNutrient)

        val listOfPortion: MutableMap<String, Portion> = mutableMapOf()
        listOfPortion["1 gram"] = oneGram
        listOfPortion["1 Mangkok"] = oneMangkok

        food = Food("Daging Sapi", "Daging", "1 gram", listOfPortion)
    }

    private fun updateUI(food: Food?){
        if (food == null) return
        toolbar_title.text = food.title
        val subtitle = food.category + " " + this.resources.getString(R.string.bullet_divider) + " " + food.default_portion
        toolbar_subtitle.text = subtitle
        val portions = food.portions
        val portion = portions[food.default_portion]
        for (item in portion?.summary!!){
            addSummary(item)
        }
    }

    private fun addSummary(item: Pair<String, String>){
        val child = layoutInflater.inflate(R.layout.item_food_details_summary, item_food_details_summary)
        child.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}
