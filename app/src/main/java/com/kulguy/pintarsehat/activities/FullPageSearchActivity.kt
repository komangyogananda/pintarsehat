package com.kulguy.pintarsehat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.adapters.OnSearchResultListener
import com.kulguy.pintarsehat.adapters.SearchResultArrayListAdapter
import com.kulguy.pintarsehat.models.SearchResultModel
import com.kulguy.pintarsehat.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_full_page_search.*

class FullPageSearchActivity : AppCompatActivity(),
    OnSearchResultListener {

    private var searchListModel: ArrayList<SearchResultModel> = ArrayList<SearchResultModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setSupportActionBar(search_toolbar)

        setContentView(R.layout.activity_full_page_search)

        initDummyData()
        updateUI()
        val itemDecorator: DividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        this.getDrawable(R.drawable.divider_vertical)?.let { itemDecorator.setDrawable(it) }
        full_page_search_results.addItemDecoration(itemDecorator)

        initSearchToolbar()
    }

    private fun initSearchToolbar(){
        search_toolbar_full_page_activity.requestFocus()
        search_toolbar_full_page_activity.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchViewModel = ViewModelProviders.of(this@FullPageSearchActivity).get(SearchViewModel::class.java)
                val searchResults = searchViewModel.getSearch(query)
                Log.w(this.toString(), query)
                searchResults?.observe(this@FullPageSearchActivity, object: Observer<ArrayList<SearchResultModel>> {
                    override fun onChanged(t: ArrayList<SearchResultModel>?) {
                        if (t != null) {
                            searchListModel = t
                            updateUI()
                        }
                    }

                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun updateUI(){
        full_page_search_results.layoutManager = LinearLayoutManager(this)
        full_page_search_results.adapter =
            SearchResultArrayListAdapter(
                searchListModel,
                this
            )
    }

    private fun query(query_string: String){

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
