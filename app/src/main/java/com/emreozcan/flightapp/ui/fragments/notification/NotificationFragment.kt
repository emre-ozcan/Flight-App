package com.emreozcan.flightapp.ui.fragments.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentNotificationBinding
import com.emreozcan.flightapp.models.notification.NotificationData
import com.emreozcan.flightapp.models.notification.PushNotification
import com.emreozcan.flightapp.services.NotificationService
import com.emreozcan.flightapp.util.Constants.Companion.AIRPORT_TOPIC
import com.emreozcan.flightapp.util.SplittedDate
import com.emreozcan.flightapp.util.dateSplitter
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*


class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding?= null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private var titleLength = 0
    private var messageLength = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater,container,false)

        textInputDoOnTextChange()

        binding.buttonNotification.setOnClickListener {
            val title = binding.titleNotificationEditText.text.toString().trim()
            val message = binding.messageNotificationEditText.text.toString().trim()

            val notificationData = NotificationData(title,message)
            val pushNotification = PushNotification(notificationData,AIRPORT_TOPIC)

            mainViewModel.sendNotification(pushNotification,requireContext())

        }

        binding.buttonNotification.isClickable = false

        binding.buttonSpecific.setOnClickListener {

            val constraintsBuilder = CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now())

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.select_date))
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

            datePicker.show(requireActivity().supportFragmentManager,"datePicker")

            datePicker.addOnPositiveButtonClickListener {
                val splittedDate = dateSplitter(datePicker.headerText)
                createTimePicker(splittedDate)
            }
        }

        return binding.root
    }

    private fun createTimePicker(splittedDate: SplittedDate) {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setTitleText(getString(R.string.select_time))
            .build()

        picker.show(requireActivity().supportFragmentManager,"timePicker")

        picker.addOnPositiveButtonClickListener {

            val calendar = createCalendar(splittedDate,picker)

            val intent = Intent(requireActivity().applicationContext,NotificationService::class.java)
            val pendingIntent = PendingIntent.getBroadcast(requireActivity().applicationContext,0,intent,
                FLAG_ONE_SHOT)
            val alarmManager = requireActivity().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager

            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
            Toast.makeText(this.context,getString(R.string.successfully_created),Toast.LENGTH_LONG).show()

        }
    }

    private fun createCalendar(splittedDate: SplittedDate, picker: MaterialTimePicker): Calendar{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()

        calendar.set(Calendar.DAY_OF_MONTH,splittedDate.day)
        calendar.set(Calendar.MONTH,splittedDate.month)
        calendar.set(Calendar.YEAR,splittedDate.year)

        calendar.set(Calendar.HOUR_OF_DAY,picker.hour)
        calendar.set(Calendar.MINUTE,picker.minute)
        calendar.set(Calendar.SECOND,0)

        return calendar

    }


    private fun textInputDoOnTextChange(){
        binding.messageNotificationEditText.doOnTextChanged { text, _, _, _ ->
            messageLength = text!!.length
            if (messageLength == 0){
                binding.textInputLayoutMessage.error = getString(R.string.empty_blank)
                binding.buttonNotification.isClickable = false
            }else{
                binding.textInputLayoutMessage.isErrorEnabled = false
                binding.buttonNotification.isClickable = true
            }
        }
        binding.titleNotificationEditText.doOnTextChanged { text, _, _, _ ->
            titleLength = text!!.length
            if (titleLength == 0){
                binding.textInputLayoutTitle.error = getString(R.string.empty_blank)
                binding.buttonNotification.isClickable = false
            }else{
                binding.textInputLayoutTitle.isErrorEnabled = false
                binding.buttonNotification.isClickable = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}