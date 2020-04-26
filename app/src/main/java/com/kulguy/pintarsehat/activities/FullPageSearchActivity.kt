package com.kulguy.pintarsehat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.adapters.OnSearchResultListener
import com.kulguy.pintarsehat.adapters.SearchResultArrayListAdapter
import com.kulguy.pintarsehat.fragments.LoadingDialogFragment
import com.kulguy.pintarsehat.models.SearchResultModel
import com.kulguy.pintarsehat.viewmodel.SearchViewModel
import com.kulguy.pintarsehat.viewmodel.TopSearchViewModel
import kotlinx.android.synthetic.main.activity_full_page_search.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.loading_search_result_shimmer.*

class FullPageSearchActivity : AppCompatActivity(),
    OnSearchResultListener {

    private var searchListModel: ArrayList<SearchResultModel> = ArrayList()
    private var querySearchIsChanged: Boolean = false
    private var firstTime: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(search_toolbar)

        setContentView(R.layout.activity_full_page_search)
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        this.getDrawable(R.drawable.divider_vertical)?.let { itemDecorator.setDrawable(it) }
        full_page_search_results.addItemDecoration(itemDecorator)
//        val bundle = intent.getBundleExtra("topSearch")
//        topSearch = bundle.getSerializable("data") as ArrayList<SearchResultModel>
//        searchListModel = topSearch
        search_results_loading_shimmer.visibility = View.GONE
        full_page_search_results.visibility = View.GONE
        search_empty.visibility = View.GONE
        search_results_counter.visibility = View.GONE

        getTopSearch()
        initSearchToolbar()
    }

    private fun showLoading(){
        search_results_loading_shimmer.visibility = View.VISIBLE
    }

    private fun dismissLoading(){
        search_results_loading_shimmer.visibility = View.GONE
    }

    private fun getTopSearch(){
        val topSearchViewModel = ViewModelProviders.of(this).get(TopSearchViewModel::class.java)
        val topSearchResult = topSearchViewModel.getTopSearches()
        showLoading()
        topSearchResult?.observe(this, Observer<ArrayList<SearchResultModel>>{
            searchListModel = it
            updateUI()
            dismissLoading()
        })
    }

    private fun initSearchToolbar(){
        search_toolbar_full_page_activity.requestFocus()
        search_toolbar_full_page_activity.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(queryString: String?): Boolean {
                search_toolbar_full_page_activity.clearFocus();
                if (queryString != null && querySearchIsChanged){
                    firstTime = false
                    showLoading()
                    query(queryString)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                querySearchIsChanged = true
                return true
            }

        })
    }

    private fun updateUI(){
        if (firstTime){
            search_empty.visibility = View.GONE
            full_page_search_results.visibility = View.VISIBLE
            search_results_counter.visibility = View.VISIBLE
            search_results_counter.text = "Top Foods"
            full_page_search_results.adapter =
                SearchResultArrayListAdapter(
                    searchListModel,
                    this
                )
            full_page_search_results.layoutManager = LinearLayoutManager(this)
        }else{
            if (searchListModel.size == 0){
                search_empty.visibility = View.VISIBLE
                search_results_counter.visibility = View.GONE
                full_page_search_results.visibility = View.GONE
                search_results_loading_shimmer.visibility = View.GONE
            }else{
                search_results_counter.visibility = View.VISIBLE
                full_page_search_results.visibility = View.VISIBLE
                search_empty.visibility = View.GONE
                search_results_loading_shimmer.visibility = View.GONE
                search_results_counter.text = searchListModel.size.toString() + " search results: "
                full_page_search_results.adapter =
                    SearchResultArrayListAdapter(
                        searchListModel,
                        this
                    )
                full_page_search_results.layoutManager = LinearLayoutManager(this)
            }
        }
    }

    private fun query(queryString: String){
        Log.w("Dialog", queryString)
        val searchViewModel = ViewModelProviders.of(this@FullPageSearchActivity).get(SearchViewModel::class.java)
        val searchResults = searchViewModel.getSearch(queryString)
        Log.w(this.toString(), queryString)
        searchResults?.observe(this@FullPageSearchActivity,
            Observer<ArrayList<SearchResultModel>> { t ->
                if (t != null) {
                    searchListModel = t
                    updateUI()
//                    dismissLoading()
                }
            })
    }

    override fun onSearchResultClick(position: Int) {
        Log.w("Recycle View", "Click " + position)
        val intent = Intent(this, FoodDetailsActivity::class.java)
        intent.putExtra("refId", searchListModel[position].refId)
        intent.putExtra("activePortion", searchListModel[position].defaultPortion)
        startActivity(intent)
    }
}
