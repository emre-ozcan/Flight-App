package com.emreozcan.flightapp.ui.fragments.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentNotificationBinding
import com.emreozcan.flightapp.models.notification.NotificationData
import com.emreozcan.flightapp.models.notification.PushNotification
import com.emreozcan.flightapp.util.Constants.Companion.AIRPORT_TOPIC
import com.emreozcan.flightapp.viewmodel.MainViewModel


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

        return binding.root
    }
    private fun textInputDoOnTextChange(){
        binding.messageNotificationEditText.doOnTextChanged { text, _, _, _ ->
            messageLength = text!!.length
            if (messageLength == 0){
                binding.textInputLayoutMessage.error = "Empty !"
                binding.buttonNotification.isClickable = false
            }else{
                binding.textInputLayoutMessage.isErrorEnabled = false
                binding.buttonNotification.isClickable = true
            }
        }
        binding.titleNotificationEditText.doOnTextChanged { text, _, _, _ ->
            titleLength = text!!.length
            if (titleLength == 0){
                binding.textInputLayoutTitle.error = "Empty !"
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