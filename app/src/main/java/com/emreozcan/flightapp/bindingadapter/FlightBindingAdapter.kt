package com.emreozcan.flightapp.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.emreozcan.flightapp.R
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
                    "${difference.hours}h ${difference.minutes}m".also { textView.text = it }
                }
                difference.hours < 1 && difference.minutes > 0 -> {
                    "${difference.minutes}m".also { textView.text = it }
                }
                difference.hours>0 && difference.minutes<1 -> {
                    "${difference.hours}h".also { textView.text = it }
                }
                else -> {
                    //TODO resoruceden al
                    textView.text = textView.context.getString(R.string.musteri_vefat_etmistir)
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
            }else{
                textView.context.getString(R.string.could_not_calculate).also { textView.text = it }
            }
        }

    }
}