package com.kulguy.pintarsehat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.button.MaterialButton
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.adapters.NutrientInfoArrayListAdapter
import com.kulguy.pintarsehat.models.FoodModel
import com.kulguy.pintarsehat.models.NutrientModel
import com.kulguy.pintarsehat.models.PortionModel
import com.kulguy.pintarsehat.models.SubNutrientModel
import kotlinx.android.synthetic.main.activity_food_details.*
import kotlinx.android.synthetic.main.activity_food_details.toolbar
import kotlinx.android.synthetic.main.activity_food_details.toolbar_title
import kotlinx.android.synthetic.main.activity_food_details.view.*

class FoodDetailsActivity : AppCompatActivity() {

    var foodModel: FoodModel? = null
    var active_portion: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setSupportActionBar(toolbar)
        setContentView(R.layout.activity_food_details)

        initData()
        updateUI(foodModel)
    }

    private fun initData(){
        val lemakSub: ArrayList<SubNutrientModel> = arrayListOf()
        lemakSub.add(SubNutrientModel("Lemak Jenuh Baik", "10g"))
        lemakSub.add(SubNutrientModel("Lemak Jenuh TIdak Baik", "10g"))
        val karboSub: ArrayList<SubNutrientModel> = arrayListOf()
        karboSub.add(SubNutrientModel("Gula", "90g"))
        karboSub.add(SubNutrientModel("Gula Merah", "100g"))

        val lemak: NutrientModel =
            NutrientModel(
                "Lemak",
                "100g",
                lemakSub
            )
        val energi: NutrientModel =
            NutrientModel("Energi", "200kj")
        val karbohidrat: NutrientModel =
            NutrientModel(
                "Karbohidrat",
                "100g",
                karboSub
            )

        val summary: ArrayList<Pair<String, String>> = ArrayList()
        summary.add(Pair("Kalori", "100kj"))
        summary.add(Pair("Karbohidrat", "100g"))
        summary.add(Pair("Lemak", "32g"))
        summary.add(Pair("Protein", "93g"))

        val listOfNutrient = ArrayList<NutrientModel>()
        listOfNutrient.add(energi)
        listOfNutrient.add(karbohidrat)
        listOfNutrient.add(lemak)

        val oneGram: PortionModel =
            PortionModel(
                "1 gram",
                summary,
                listOfNutrient
            )
        val oneMangkok: PortionModel =
            PortionModel(
                "1 Mangkok",
                summary,
                listOfNutrient
            )

        val listOfPortion: MutableMap<String, PortionModel> = mutableMapOf()
        listOfPortion["1 gram"] = oneGram
        listOfPortion["1 Mangkok"] = oneMangkok

        foodModel = FoodModel(
            "Daging Sapi",
            "Daging",
            "1 gram",
            listOfPortion
        )
        active_portion = foodModel!!.default_portion
    }

    private fun updateUI(foodModel: FoodModel?){
        if (foodModel == null) return
        toolbar_title.text = foodModel.title
        val subtitle = foodModel.category + " " + this.resources.getString(R.string.bullet_divider) + " " + foodModel.default_portion
        toolbar_subtitle.text = subtitle
        val portions = foodModel.portions
        val portion = portions[foodModel.default_portion]
        for (item in portion?.summary!!){
            addSummary(item)
        }
        addNutrient(portion.nutrients)
        addOtherPortion(portions)
    }

    private fun addSummary(item: Pair<String, String>){
        val layoutInflater = LayoutInflater.from(this)
        val child = layoutInflater.inflate(R.layout.item_food_details_summary, null, false)
        val title = child.findViewById<TextView>(R.id.summary_details_title)
        title.text = item.first
        val value = child.findViewById<TextView>(R.id.summary_details_value)
        value.text = item.second
        val layoutParams: FlexboxLayout.LayoutParams = FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.flexGrow = 1F
        child.layoutParams = layoutParams
        item_food_details_summary.addView(child)
    }

    private fun addNutrient(nutrients: ArrayList<NutrientModel>){
        val itemDecorator: DividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val divider = this.getDrawable(R.drawable.divider_vertical_6)
        if (divider != null) {
            itemDecorator.setDrawable(divider)
        }
        food_nutrition_details_recycler_view.addItemDecoration(itemDecorator)
        food_nutrition_details_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FoodDetailsActivity)
            adapter = NutrientInfoArrayListAdapter(nutrients)
        }
    }

    private fun addOtherPortion(portions: MutableMap<String, PortionModel>){
        val keys = portions.keys
        var selectedIndex: MaterialButton? = null
        keys.forEachIndexed(){ idx, key ->
            val portion = portions[key]
//            val themeWrapper = ContextThemeWrapper(this, R.style.materi)
            val portionButton = MaterialButton(this, null, R.attr.materialButtonOutlinedStyle )
            portionButton.text = portion?.title
            portionButton.setTextColor(this.resources.getColor(R.color.bluePrimary))
            portionButton.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            if (portion != null) {
                if (portion.title == active_portion){
                    Log.w("anjay", portion.title)
                    selectedIndex = portionButton
                }
            }
            portions_button_group.addView(portionButton)
        }
        portions_button_group.isSelectionRequired = true
        portions_button_group.isSingleSelection = true
        portions_button_group.clearChecked()
        selectedIndex?.isChecked = true
    }
}
