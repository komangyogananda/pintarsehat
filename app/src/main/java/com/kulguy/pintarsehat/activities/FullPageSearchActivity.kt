package com.kulguy.pintarsehat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.adapters.OnSearchResultListener
import com.kulguy.pintarsehat.adapters.SearchResultArrayListAdapter
import com.kulguy.pintarsehat.models.SearchResultModel
import kotlinx.android.synthetic.main.activity_full_page_search.*

class FullPageSearchActivity : AppCompatActivity(),
    OnSearchResultListener {

    private var searchListModel: ArrayList<SearchResultModel> = ArrayList<SearchResultModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setSupportActionBar(search_toolbar)

        setContentView(R.layout.activity_full_page_search)

        initDummyData()
        full_page_search_results.layoutManager = LinearLayoutManager(this)
        full_page_search_results.adapter =
            SearchResultArrayListAdapter(
                searchListModel,
                this
            )
        val itemDecorator: DividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        this.getDrawable(R.drawable.divider_vertical)?.let { itemDecorator.setDrawable(it) }
        full_page_search_results.addItemDecoration(itemDecorator)
        search_toolbar_full_page_activity.requestFocus()
    }

    private fun initDummyData(){
        var summaryMap: MutableMap<String, String> = mutableMapOf()

        summaryMap["calorie"] = "12 kcal"
        summaryMap["carbo"] = "100g"
        summaryMap["protein"] = "1000g"
        summaryMap["fat"] = "130g"

        searchListModel.add(
            SearchResultModel(
                "Daging Ayam",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Babi",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Sapi",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Bakar",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Ayam",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Babi",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Sapi",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Bakar",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Ayam",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Babi",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Sapi",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
        searchListModel.add(
            SearchResultModel(
                "Daging Bakar",
                "Daging",
                "100 grams",
                summaryMap
            )
        )
    }

    override fun onSearchResultClick(position: Int) {
        Log.w("Recycle View", "Click" + position)
    }
}
