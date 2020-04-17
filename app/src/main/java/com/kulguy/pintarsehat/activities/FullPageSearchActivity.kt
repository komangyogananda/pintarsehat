package com.kulguy.pintarsehat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.kulguy.pintarsehat.dialog.LoadingDialog
import com.kulguy.pintarsehat.models.SearchResultModel
import com.kulguy.pintarsehat.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_full_page_search.*

class FullPageSearchActivity : AppCompatActivity(),
    OnSearchResultListener {

    private var searchListModel: ArrayList<SearchResultModel> = ArrayList<SearchResultModel>()
    private var loadingDialog: LoadingDialog = LoadingDialog(this)
    private var querySearchIsChanged: Boolean = false
    private var firstTime: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(search_toolbar)

        setContentView(R.layout.activity_full_page_search)
        updateUI()
        val itemDecorator: DividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        this.getDrawable(R.drawable.divider_vertical)?.let { itemDecorator.setDrawable(it) }
        full_page_search_results.addItemDecoration(itemDecorator)

        initSearchToolbar()
    }

    private fun initSearchToolbar(){
        search_toolbar_full_page_activity.requestFocus()
        search_toolbar_full_page_activity.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(queryString: String?): Boolean {
                search_toolbar_full_page_activity.clearFocus();
                if (queryString != null && querySearchIsChanged){
                    loadingDialog.showDialog()
                    firstTime = false
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
            search_empty.visibility = View.INVISIBLE
            search_results_counter.text = "Top Foods"
            full_page_search_results.adapter =
                SearchResultArrayListAdapter(
                    searchListModel,
                    this
                )
        }else{
            if (searchListModel.size == 0){
                search_empty.visibility = View.VISIBLE
                search_results_counter.visibility = View.INVISIBLE
                full_page_search_results.visibility = View.INVISIBLE
            }else{
                search_results_counter.visibility = View.VISIBLE
                full_page_search_results.visibility = View.VISIBLE
                search_empty.visibility = View.INVISIBLE
                full_page_search_results.layoutManager = LinearLayoutManager(this)
                search_results_counter.text = searchListModel.size.toString() + " search results: "
                full_page_search_results.adapter =
                    SearchResultArrayListAdapter(
                        searchListModel,
                        this
                    )
            }
        }
    }

    private fun query(queryString: String){
        Log.w("Dialog", queryString)
        val searchViewModel = ViewModelProviders.of(this@FullPageSearchActivity).get(SearchViewModel::class.java)
        val searchResults = searchViewModel.getSearch(queryString, {
            loadingDialog.showDialog()
            true
        }, {
            loadingDialog.dismissDialog()
            true
        })
        Log.w(this.toString(), queryString)
        searchResults?.observe(this@FullPageSearchActivity,
            Observer<ArrayList<SearchResultModel>> { t ->
                if (t != null) {
                    searchListModel = t
                    updateUI()
                }
            })
    }

    override fun onSearchResultClick(position: Int) {
        Log.w("Recycle View", "Click" + position)
    }
}
