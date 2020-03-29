package com.kulguy.pintarsehat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_search, container, false)

        var searchList = ArrayList<SearchResult>()

        var summaryMap: MutableMap<String, String> = mutableMapOf()

        summaryMap["calorie"] = "12 kcal"
        summaryMap["carbo"] = "100g"
        summaryMap["protein"] = "1000g"
        summaryMap["fat"] = "130g"

        searchList.add(SearchResult("Daging Ayam", "Daging", "100 grams", summaryMap))
        searchList.add(SearchResult("Daging Babi", "Daging", "100 grams", summaryMap))
        searchList.add(SearchResult("Daging Sapi", "Daging", "100 grams", summaryMap))
        searchList.add(SearchResult("Daging Bakar", "Daging", "100 grams", summaryMap))


        val searchResultListAdapter: SearchResultListAdapter? = activity?.applicationContext?.let {
            SearchResultListAdapter(
                it, R.layout.search_result_list_item, searchList)
        }

        val searchResultsView = view.findViewById<ListView>(R.id.search_results)

        searchResultsView.adapter = searchResultListAdapter

        return view
    }

}
