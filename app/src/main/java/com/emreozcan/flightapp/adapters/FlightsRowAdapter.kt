package com.emreozcan.flightapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.flightapp.adapters.FlightsRowAdapter.FlightsViewHolder
import com.emreozcan.flightapp.databinding.RowAirportDesignBinding
import com.emreozcan.flightapp.models.Airports
import com.emreozcan.flightapp.models.Flights
import com.emreozcan.flightapp.ui.fragments.flights.FlightsFragmentDirections
import com.emreozcan.flightapp.util.RecyclerDiffUtil

class FlightsRowAdapter: RecyclerView.Adapter<FlightsViewHolder>() {

    private var airportsList = emptyList<Airports>()


    class FlightsViewHolder(val binding : RowAirportDesignBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(airport: Airports) {
            binding.airport = airport
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): FlightsViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowAirportDesignBinding.inflate(layoutInflater,parent,false)
                return FlightsViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightsViewHolder {
        return FlightsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FlightsViewHolder, position: Int) {
        holder.bind(airportsList[position])
        holder.binding.cardViewAirport.setOnClickListener {
            val action = FlightsFragmentDirections.actionFlightsFragmentToAirportFlightsFragment(airportsList[position].flightList.toTypedArray())
            Navigation.findNavController(it).navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return airportsList.size
    }
    fun setData(list: List<Airports>){
        val recyclerDiffUtil = RecyclerDiffUtil(airportsList,list)
        val diffResult = DiffUtil.calculateDiff(recyclerDiffUtil)
        airportsList = list
        diffResult.dispatchUpdatesTo(this)
    }


}