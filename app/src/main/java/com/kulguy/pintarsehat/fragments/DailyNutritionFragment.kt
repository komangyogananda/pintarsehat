package com.kulguy.pintarsehat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.firebase.auth.FirebaseAuth
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.activities.FoodDetailsActivity
import com.kulguy.pintarsehat.activities.FullPageSearchActivity
import com.kulguy.pintarsehat.adapters.OnSearchResultListener
import com.kulguy.pintarsehat.adapters.SearchResultArrayListAdapter
import com.kulguy.pintarsehat.models.DailyNutritionModel
import com.kulguy.pintarsehat.models.SearchResultModel
import com.kulguy.pintarsehat.viewmodel.DailyNutritionViewModel
import kotlinx.android.synthetic.main.activity_full_page_search.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 */
class DailyNutritionFragment : Fragment(), OnSearchResultListener {

    private var currentDate = Calendar.getInstance()
    private val lookupMonth = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "Oktober", "November", "Desember")
    private lateinit var calendar: CalendarView
    private lateinit var displayDate: TextView
    private lateinit var dailyNutritionModel: DailyNutritionModel
    private lateinit var auth: FirebaseAuth
    private var currentDateString: String = ""
    private var currentDateKey: String = ""
    private lateinit var content: LinearLayout
    private lateinit var loading: RelativeLayout
    private lateinit var emptydata: LinearLayout
    private lateinit var dailyNutritionViewModel: DailyNutritionViewModel
    private lateinit var fetchResult: MutableLiveData<DailyNutritionModel>
    private lateinit var addNewFoods: LinearLayout
    private lateinit var foodList: RecyclerView
    private var searchListModel: ArrayList<SearchResultModel> = arrayListOf()
    private lateinit var summary_details_right: FlexboxLayout
    private lateinit var summary_details_left: FlexboxLayout
    private lateinit var food_details_akg: TextView
    private var totalSummary: MutableMap<String, Double> = mutableMapOf()

    private fun fetchDailyNutrition(){
        showLoading()
        dailyNutritionViewModel.getDailyNutrition(auth.currentUser!!.uid, currentDateKey)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden){
            currentDateString = "${currentDate.get(Calendar.DAY_OF_MONTH)} ${lookupMonth[currentDate.get(Calendar.MONTH)]} ${currentDate.get(Calendar.YEAR)}"
            currentDateKey = "${currentDate.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')} ${(currentDate.get(Calendar.MONTH) + 1).toString().padStart(2, '0')} ${currentDate.get(Calendar.YEAR).toString().padStart(4, '0')}"
            fetchDailyNutrition()
        }
    }



    private fun calculateSummary(){
        totalSummary.clear()
        searchListModel.forEach {searchModel ->
            val summary = searchModel.summary
            summary.forEach {entry ->
                var key = entry.key
                var value = entry.value
                value = value.replace(",", ".").removeSuffix("g").removeSuffix("kcal")
                var oldInt = 0.0
                totalSummary[key]?.let {
                    oldInt = it
                }
                var newInt = oldInt + value.toDouble()
                totalSummary[key] = newInt
            }
        }
    }

    private fun updateUI(){
        clear()
        searchListModel.clear()
        dailyNutritionModel.foods.forEach {
            searchListModel.add(SearchResultModel.invoke(it))
        }
        calculateSummary()
        var idx = 1
        totalSummary.forEach{
            var value: String = ""
            if (it.key == "Kalori"){
                value = it.value.roundToInt().toString()
            }else{
                value = it.value.roundToInt().toString() + " g"
            }
            val x = Pair<String, String>(it.key, value)
            addSummary(x, idx)
            idx += 1
        }
        foodList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchResultArrayListAdapter(
                searchListModel,
                this@DailyNutritionFragment
            )
        }
        content.visibility = View.VISIBLE
    }

    private fun addSummary(item: Pair<String, String>, index: Int){
        val layoutInflater = LayoutInflater.from(context)
        val child = layoutInflater.inflate(R.layout.item_food_details_summary, null, false)
        val title = child.findViewById<TextView>(R.id.summary_details_title)
        title.text = item.first
        val value = child.findViewById<TextView>(R.id.summary_details_value)
        value.text = item.second
        if (item.first == "Kalori") {
            value.text = value.text as String + " kcal"
            food_details_akg.text = ceil(item.second.toDouble().roundToInt() / 2000.0 * 100).roundToInt().toString() + " %"
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

    private fun showLoading(){
        content.visibility = View.GONE
        emptydata.visibility = View.GONE
        loading.visibility = View.VISIBLE
    }

    private fun dismissLoading(){
        loading.visibility = View.GONE
    }

    private fun showNoData(){
        loading.visibility = View.GONE
        content.visibility = View.GONE
        emptydata.visibility = View.VISIBLE
    }

    private fun clear(){
        summary_details_right.removeAllViewsInLayout()
        summary_details_left.removeAllViewsInLayout()
    }

    private fun mapViewComponent(view: View){
        calendar = view.findViewById(R.id.calendar)
        displayDate = view.findViewById(R.id.daily_nutrition_title)
        content = view.findViewById(R.id.daily_nutrition_content)
        content.visibility = View.GONE
        loading = view.findViewById(R.id.loading_content)
        emptydata = view.findViewById(R.id.food_empty)
        addNewFoods = view.findViewById(R.id.add_new_foods)
        foodList = view.findViewById(R.id.daily_nutrition_food_list)
        summary_details_left = view.findViewById(R.id.summary_details_left)
        summary_details_right = view.findViewById(R.id.summary_details_right)
        food_details_akg = view.findViewById(R.id.food_details_akg)
    }

    private fun initFragment(view: View){
        mapViewComponent(view)
        auth = FirebaseAuth.getInstance()
        dailyNutritionViewModel = ViewModelProviders.of(this).get(DailyNutritionViewModel::class.java)
        fetchResult =
            dailyNutritionViewModel.getDailyNutrition(auth.currentUser?.uid!!, currentDateKey)!!
        fetchResult?.observe(this, Observer {
            if (it != null){
                dailyNutritionModel = it
                dismissLoading()
                updateUI()
            }else{
                dismissLoading()
                showNoData()
            }
            dismissLoading()
        })
        addNewFoods.setOnClickListener {
            val intent = Intent(context, FullPageSearchActivity::class.java)
            startActivity(intent)
        }
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        activity?.getDrawable(R.drawable.divider_vertical)?.let { itemDecorator.setDrawable(it) }
        foodList.addItemDecoration(itemDecorator)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_nutrition, container, false)

        currentDateString = "${currentDate.get(Calendar.DAY_OF_MONTH)} ${lookupMonth[currentDate.get(Calendar.MONTH)]} ${currentDate.get(Calendar.YEAR)}"
        currentDateKey = "${currentDate.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')} ${(currentDate.get(Calendar.MONTH) + 1).toString().padStart(2, '0')} ${currentDate.get(Calendar.YEAR).toString().padStart(4, '0')}"
        initFragment(view)

        displayDate.text = currentDateString

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val verboseMonth = lookupMonth[month]
            currentDateString = "$dayOfMonth $verboseMonth $year"
            currentDateKey = "${dayOfMonth.toString().padStart(2, '0')} ${(month + 1).toString().padStart(2, '0')} ${year.toString().padStart(4, '0')}"
            displayDate.text = currentDateString
            fetchDailyNutrition()
        }
        calendar.maxDate = calendar.date

        return view
    }

    override fun onSearchResultClick(position: Int) {
        val intent = Intent(context, FoodDetailsActivity::class.java)
        intent.putExtra("refId", searchListModel[position].refId)
        intent.putExtra("activePortion", searchListModel[position].activePortion)
        startActivity(intent)
    }

}
