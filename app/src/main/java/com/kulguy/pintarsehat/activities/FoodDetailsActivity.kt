package com.kulguy.pintarsehat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.adapters.NutrientInfoArrayListAdapter
import com.kulguy.pintarsehat.adapters.OtherPortionArrayListAdapter
import com.kulguy.pintarsehat.models.FoodModel
import com.kulguy.pintarsehat.models.NutrientModel
import com.kulguy.pintarsehat.viewmodel.FoodDetailsViewModel
import kotlinx.android.synthetic.main.activity_food_details.*
import kotlinx.android.synthetic.main.loading_page.*
import kotlin.math.ceil
import kotlin.math.roundToInt

class FoodDetailsActivity : AppCompatActivity(){

    private var foodModel: FoodModel? = null
    private var portionsModel: ArrayList<Pair<String, Boolean>> = arrayListOf()
    private var refId: String = ""
    private var activePortionIndex = 0
    private var activePortion = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_food_details)

        refId = intent.getStringExtra("refId") as String
        activePortion = intent.getStringExtra("activePortion") as String

        val food = ViewModelProviders.of(this).get(FoodDetailsViewModel::class.java)
        val fetchResult = food.getFood(refId)
        initLayout()
        showLoading()

        fetchResult?.observe(this, Observer {
            foodModel = it
            initialOtherPortions()
            updateUI()
            hideLoading()
        })
    }

    private fun showLoading(){
        food_details_content.visibility = View.GONE
        loading_page.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        loading_page.visibility = View.GONE
        food_details_content.visibility = View.VISIBLE
    }

    private fun initLayout(){
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val divider16 = this.getDrawable(R.drawable.divider_vertical_16)
        if (divider16 != null) {
            itemDecorator.setDrawable(divider16)
        }
        food_nutrition_details_recycler_view.addItemDecoration(itemDecorator)

        val divider8 = this.getDrawable(R.drawable.divider_vertical)
        if (divider8 != null) {
            itemDecorator.setDrawable(divider8)
        }
        other_portion_recycler_view.addItemDecoration(itemDecorator)
    }

    private fun initialOtherPortions(){
        portionsModel = arrayListOf()
        foodModel?.portions!!.keys.forEach{
            portionsModel.add(Pair(it, false))
        }
        portionsModel.forEachIndexed { index, portionTitle ->
            if (portionTitle.first == activePortion){
                activePortionIndex = index
            }
        }
        portionsModel[activePortionIndex] = portionsModel[activePortionIndex].copy(second = true)
    }

    private fun clear(){
        summary_details_right.removeAllViewsInLayout()
        summary_details_left.removeAllViewsInLayout()
    }

    private fun updateUI(){
        if (foodModel == null) return
        clear()
        food_details_titile.text = foodModel?.title
        val subtitle = foodModel?.category + ", " + activePortion
        food_details_subtitle.text = subtitle
        val portions = foodModel?.portions
        val portion = portions?.get(activePortion)
        var idx = 0
        for (item in portion?.summary!!){
            val x = item.toPair()
            idx ++
            Log.w("Pair", x.toString())
            addSummary(x as Pair<String, String>, idx)
        }
        updateNutrient(portion?.nutrient)
        updateOtherPortion()
    }

    private fun addSummary(item: Pair<String, String>, index: Int){
        val layoutInflater = LayoutInflater.from(this)
        val child = layoutInflater.inflate(R.layout.item_food_details_summary, null, false)
        val title = child.findViewById<TextView>(R.id.summary_details_title)
        title.text = item.first
        val value = child.findViewById<TextView>(R.id.summary_details_value)
        value.text = item.second
        if (item.first == "Kalori") {
            value.text = value.text as String + " kcal"
            food_details_akg.text = ceil(item.second.toInt() / 2000.0 * 100).roundToInt().toString() + " %"
        }
        val layoutParams: FlexboxLayout.LayoutParams = FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.MATCH_PARENT, FlexboxLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.flexGrow = 1F
        child.layoutParams = layoutParams
        if (index % 2 == 0){
            summary_details_right.addView(child)
        }else{
            summary_details_left.addView(child)
        }

    }

    private fun updateOtherPortion(){
        other_portion_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FoodDetailsActivity)
            adapter = OtherPortionArrayListAdapter(this@FoodDetailsActivity, portionsModel)
        }
    }

    private fun updateNutrient(nutrients: ArrayList<NutrientModel>){
        food_nutrition_details_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@FoodDetailsActivity)
            adapter = NutrientInfoArrayListAdapter(nutrients)
        }
    }

    fun onPortionClick(position: Int) {
        if (position != activePortionIndex) {
            portionsModel[activePortionIndex] =
                portionsModel[activePortionIndex].copy(second = false)
            portionsModel[position] = portionsModel[position].copy(second = true)
            activePortionIndex = position
            activePortion = portionsModel[position].first
            updateUI()
//            Toast.makeText(this,"Porsi telah berubah", Toast.LENGTH_LONG).show()
        }
    }
}
