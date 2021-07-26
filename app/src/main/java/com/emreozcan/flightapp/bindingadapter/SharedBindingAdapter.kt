package com.emreozcan.flightapp.bindingadapter

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emreozcan.flightapp.adapters.AirportsFlightsRowAdapter
import com.emreozcan.flightapp.adapters.AirportsRowAdapter
import com.emreozcan.flightapp.adapters.FlightsRowAdapter
import com.emreozcan.flightapp.models.Airports
import com.emreozcan.flightapp.models.Flights
import com.emreozcan.flightapp.util.setupRecyclerView
import com.facebook.shimmer.ShimmerFrameLayout

class SharedBindingAdapter {

    companion object{

        @BindingAdapter("setAdapter", "listOfItems", requireAll = true)
        @JvmStatic
        fun <T, P : RecyclerView.ViewHolder?> setRecyclerView(
            recyclerView: RecyclerView,
            adapter: RecyclerView.Adapter<P>,
            list: List<T>?
        ) {

            list?.let {
                when (adapter) {
                    is AirportsFlightsRowAdapter -> {

                        recyclerView.setupRecyclerView(adapter
                        )

                        adapter.setData(it as List<Flights>).apply {
                            recyclerView.scheduleLayoutAnimation()
                        }
                    }
                    is AirportsRowAdapter -> {
                        recyclerView.setupRecyclerView(
                            adapter
                        )

                        adapter.setData(it as List<Airports>).apply {
                            recyclerView.scheduleLayoutAnimation()
                        }
                    }
                    is FlightsRowAdapter ->{
                        recyclerView.setupRecyclerView(adapter,
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        )

                        adapter.setData(it as List<Airports>).apply {
                            recyclerView.scheduleLayoutAnimation()
                        }
                    }
                }
            }
        }
        @BindingAdapter("visibilityList")
        @JvmStatic
        fun <T>viewVisibility(view: View,list: List<T>?){
            if (list.isNullOrEmpty()){
                when(view){
                    is RecyclerView ->{
                        view.isVisible = false
                    }is ShimmerFrameLayout ->{
                        view.isVisible = true
                        view.startShimmer()
                    }
                }
            }else{
                when(view){
                    is RecyclerView ->{
                        view.isVisible = true
                    }is ShimmerFrameLayout ->{
                    view.isVisible = false
                    view.hideShimmer()
                }
                }
            }
        }

    }
}