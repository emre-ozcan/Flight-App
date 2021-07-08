package com.emreozcan.flightapp.adapters

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.flightapp.databinding.RowAirportDesignBinding
import com.emreozcan.flightapp.models.Airports
import com.emreozcan.flightapp.ui.fragments.airports.AirportsFragmentDirections
import com.emreozcan.flightapp.util.RecyclerDiffUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class AirportsRowAdapter : RecyclerView.Adapter<AirportsRowAdapter.MyViewHolder>(),PermissionListener {

    private var airportsList = emptyList<Airports>()
    private var position = 0
    private lateinit var view : View

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

        this.view = holder.itemView


        holder.binding.airportCall.setOnClickListener {
            this.position=position
            Dexter.withContext(it.context).withPermission(Manifest.permission.CALL_PHONE).withListener(this).check()
        }

        holder.binding.airportMap.setOnClickListener {
            val action = AirportsFragmentDirections.actionActionAirportsToMapsFragment(airportsList[position].latitudeLongitude,airportsList[position].airportName)
            Navigation.findNavController(it).navigate(action)
        }

        holder.binding.cardViewAirport.setOnClickListener {
            val action = AirportsFragmentDirections.actionActionAirportsToAirportFlightsFragment(airportsList[position].flightList.toTypedArray())
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

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

        val intent = Intent(Intent.ACTION_CALL,Uri.parse("tel:${airportsList[position].phoneNumber}"))
        view.context.startActivity(intent)

    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {

    }
}