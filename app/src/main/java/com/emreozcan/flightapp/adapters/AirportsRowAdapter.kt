package com.emreozcan.flightapp.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.flightapp.databinding.RowAirportDesignBinding
import com.emreozcan.flightapp.models.Airports
import com.emreozcan.flightapp.ui.fragments.airports.AirportsFragmentDirections
import com.emreozcan.flightapp.util.RecyclerDiffUtil


class AirportsRowAdapter : RecyclerView.Adapter<AirportsRowAdapter.MyViewHolder>() {

    private var airportsList = emptyList<Airports>()

    class MyViewHolder(val binding: RowAirportDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(airport: Airports) {
            binding.airport = airport
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowAirportDesignBinding.inflate(layoutInflater,parent,false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(airportsList[position])

        holder.binding.airportCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL,Uri.parse("tel:${airportsList[position].phoneNumber}"))
            it.context.startActivity(intent)
        }

        holder.binding.airportMap.setOnClickListener {
            val action = AirportsFragmentDirections.actionActionAirportsToMapsFragment(airportsList[position].latitudeLongitude,airportsList[position].airportName)
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