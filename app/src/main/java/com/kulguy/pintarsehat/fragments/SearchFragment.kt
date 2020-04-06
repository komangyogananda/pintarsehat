package com.kulguy.pintarsehat.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kulguy.pintarsehat.R
import com.kulguy.pintarsehat.activities.FoodDetailsActivity
import com.kulguy.pintarsehat.activities.FullPageSearchActivity
import com.kulguy.pintarsehat.adapters.OnSearchResultListener
import com.kulguy.pintarsehat.adapters.SearchResultArrayListAdapter
import com.kulguy.pintarsehat.models.SearchResultModel


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment,
    OnSearchResultListener {

    private val searchListModel: ArrayList<SearchResultModel> = ArrayList<SearchResultModel>()

    constructor(){
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

    }


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchListView = view.findViewById<RecyclerView>(R.id.search_results)
        val itemDecorator: DividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        activity?.getDrawable(R.drawable.divider_vertical)?.let { itemDecorator.setDrawable(it) }
        searchListView.addItemDecoration(itemDecorator)
        searchListView.apply {
            layoutManager  = LinearLayoutManager(activity)
            adapter =
                SearchResultArrayListAdapter(
                    searchListModel,
                    this@SearchFragment
                )
        }

        Log.w("Lifecycle: onViewCreated Search Fragment", "halo")
    }

    override fun onSearchResultClick(position: Int) {
        Log.w("Recycle View", "Click " + position)
        val intent = Intent(activity, FoodDetailsActivity::class.java)
        startActivity(intent)
    }

}
