package com.emreozcan.flightapp.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.flightapp.databinding.RowCardFlightDesignBinding
import com.emreozcan.flightapp.models.Flights
import com.emreozcan.flightapp.util.RecyclerDiffUtil

class AirportsFlightsRowAdapter :
    RecyclerView.Adapter<AirportsFlightsRowAdapter.AirportFlightsViewHolder>() {

    private var flightList = emptyList<Flights>()

    class AirportFlightsViewHolder(val binding: RowCardFlightDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(flights: Flights) {
            binding.flight = flights
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AirportFlightsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowCardFlightDesignBinding.inflate(layoutInflater, parent, false)
                return AirportFlightsViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportFlightsViewHolder {
        return AirportFlightsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AirportFlightsViewHolder, position: Int) {
        holder.bind(flightList[position])
    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    fun setData(list: List<Flights>) {
        val recyclerDiffUtil = RecyclerDiffUtil(flightList, list)
        val diffResult = DiffUtil.calculateDiff(recyclerDiffUtil)
        flightList = list
        diffResult.dispatchUpdatesTo(this)
    }
}