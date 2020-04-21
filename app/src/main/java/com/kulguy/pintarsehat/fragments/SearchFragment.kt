package com.kulguy.pintarsehat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.activities.FoodDetailsActivity
import com.kulguy.pintarsehat.activities.FullPageSearchActivity
import com.kulguy.pintarsehat.adapters.OnSearchResultListener
import com.kulguy.pintarsehat.adapters.SearchResultArrayListAdapter
import com.kulguy.pintarsehat.models.SearchResultModel
import com.kulguy.pintarsehat.viewmodel.TopSearchViewModel


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(),
    OnSearchResultListener {

    private var topSearch: ArrayList<SearchResultModel> = ArrayList<SearchResultModel>()
    private val loadingDialog: LoadingDialogFragment = LoadingDialogFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val searchButton = view.findViewById<Button>(R.id.search_fragment_search_bar)
        searchButton.setOnClickListener{
            val intent = Intent(activity, FullPageSearchActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun showLoadingDialog(){
        Log.w("Dialog", "FragmentInit")
        activity?.let {
            var fragmentManager: FragmentManager = it.supportFragmentManager
            var fragmentTransaction = fragmentManager.beginTransaction()
            val prev = fragmentManager.findFragmentByTag("dialog")
            if (prev != null){
                fragmentTransaction.remove(prev)
            }
            fragmentTransaction.addToBackStack(null)
        loadingDialog.show(fragmentTransaction, "dialog")
        }
    }

    private fun dismissLoadingDialog(){
        loadingDialog.dismiss()
        activity?.let {
            var fragmentManager: FragmentManager = it.supportFragmentManager
            var fragmentTransaction = fragmentManager.beginTransaction()
            val prev = fragmentManager.findFragmentByTag("dialog")
            if (prev != null){
                fragmentTransaction.remove(prev)
            }
        }
        Log.w("Dialog", "Dismissed")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchListView = view.findViewById<RecyclerView>(R.id.search_results)
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        activity?.getDrawable(R.drawable.divider_vertical)?.let { itemDecorator.setDrawable(it) }
        searchListView.addItemDecoration(itemDecorator)
        val topSearchViewModel = ViewModelProviders.of(this).get(TopSearchViewModel::class.java)
        val topSearchResult = topSearchViewModel.getTopSearches({
//            showLoadingDialog()
            true
        }, {
//            dismissLoadingDialog()
            true
        })
        topSearchResult?.observe(this, Observer<ArrayList<SearchResultModel>>{
            topSearch = it
            updateUI()
        })




    }

    private fun updateUI(){
        val searchListView = view?.findViewById<RecyclerView>(R.id.search_results)
        searchListView?.apply {
            layoutManager  = LinearLayoutManager(activity)
            adapter =
                SearchResultArrayListAdapter(
                    topSearch,
                    this@SearchFragment
                )
        }
    }

    override fun onSearchResultClick(position: Int) {
        Log.w("Recycle View", "Click " + position)
        val intent = Intent(activity, FoodDetailsActivity::class.java)
        startActivity(intent)
    }

}
