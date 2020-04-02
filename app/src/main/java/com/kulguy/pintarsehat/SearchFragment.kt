package com.kulguy.pintarsehat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment {

    private val searchList: ArrayList<SearchResult> = ArrayList<SearchResult>()

    constructor(){
        var summaryMap: MutableMap<String, String> = mutableMapOf()

        summaryMap["calorie"] = "12 kcal"
        summaryMap["carbo"] = "100g"
        summaryMap["protein"] = "1000g"
        summaryMap["fat"] = "130g"

        searchList.add(SearchResult("Daging Ayam", "Daging", "100 grams", summaryMap))
        searchList.add(SearchResult("Daging Babi", "Daging", "100 grams", summaryMap))
        searchList.add(SearchResult("Daging Sapi", "Daging", "100 grams", summaryMap))
        searchList.add(SearchResult("Daging Bakar", "Daging", "100 grams", summaryMap))

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
            adapter = SearchResultArrayListAdapter(searchList)
        }

        Log.w("Lifecycle: onViewCreated Search Fragment", "halo")
    }

}
