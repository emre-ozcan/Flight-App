package com.emreozcan.flightapp.adapters

import android.media.TimedMetaData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.flightapp.databinding.RowCardFlightDesignBinding
import com.emreozcan.flightapp.models.Flights
import com.emreozcan.flightapp.util.RecyclerDiffUtil
import com.emreozcan.flightapp.util.TimeDifference
import com.emreozcan.flightapp.util.difference

class AirportsFlightsRowAdapter :
    RecyclerView.Adapter<AirportsFlightsRowAdapter.AirportFlightsViewHolder>() {

    private var flightList = emptyList<Flights>()

    class AirportFlightsViewHolder(val binding: RowCardFlightDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(flights: Flights) {
            binding.flight = flights
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): AirportFlightsViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowCardFlightDesignBinding.inflate(layoutInflater,parent,false)
                return AirportFlightsViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportFlightsViewHolder {
        return AirportFlightsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AirportFlightsViewHolder, position: Int) {
       holder.bind(flightList[position])

        val timeArr = flightList[position].flightStartAndFinishTime.split(";")
        holder.binding.flightStartTimeTextView.text = timeArr[0]
        holder.binding.flightFinishTimeTextView.text = timeArr[1]

        val codeArr = flightList[position].startAndTargetCode.split(",")
        holder.binding.startAirportTextView.text = codeArr[0]
        holder.binding.targetAirportTextView.text = codeArr[1]

        val startTime = timeArr[0].split(":")
        val start = TimeDifference(startTime[0].toInt(),startTime[1].toInt())

        val finishTime = timeArr[1].split(":")
        val finish = TimeDifference(finishTime[0].toInt(),finishTime[1].toInt())


        val difference = difference(finish,start)

        holder.binding.flightDurationTextView.text = "${difference.hours}h ${difference.minutes}m"


    }

    override fun getItemCount(): Int {
        return flightList.size
    }

    fun setData(list: List<Flights>){
        val recyclerDiffUtil = RecyclerDiffUtil(flightList,list)
        val diffResult = DiffUtil.calculateDiff(recyclerDiffUtil)
        flightList = list
        diffResult.dispatchUpdatesTo(this)
    }



}