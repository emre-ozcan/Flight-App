package com.emreozcan.flightapp.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.emreozcan.flightapp.util.TimeDifference
import com.emreozcan.flightapp.util.difference


class FlightBindingAdapter {

    companion object {

        @BindingAdapter("flightStartFinishTime")
        @JvmStatic
        fun calculateTimeDifference(textView: TextView, flightStartAndFinishTime: String) {
            val timeArr = flightStartAndFinishTime.split(";")
            val startTime = timeArr[0].split(":")
            val start = TimeDifference(startTime[0].toInt(), startTime[1].toInt())

            val finishTime = timeArr[1].split(":")
            val finish = TimeDifference(finishTime[0].toInt(), finishTime[1].toInt())

            val difference = difference(finish, start)


            when {
                difference.hours > 0 && difference.minutes > 0 -> {
                    textView.text = "${difference.hours}h ${difference.minutes}m"
                }
                difference.hours < 1 && difference.minutes > 0 -> {
                    textView.text = "${difference.minutes}m"
                }
                difference.hours>0 && difference.minutes<1 -> {
                    textView.text = "${difference.hours}h"
                }
                else -> {
                    textView.text = "MUSTERI VEFAT ETMISTIR"
                }
            }

        }

        @BindingAdapter("splitString","indexOfTextView",requireAll = true)
        @JvmStatic
        fun bindingSplitString(textView: TextView,splitString: String,index: String){
            if (splitString.contains(",")){
                val codeArr = splitString.split(",")
                textView.text = codeArr[index.toInt()]
            }else if (splitString.contains(";")){
                val timeArr = splitString.split(";")
                textView.text = timeArr[index.toInt()]
            }
        }

    }
}