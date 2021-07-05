package com.emreozcan.flightapp.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentProfileBinding
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)

        val user = mainViewModel.currentUser

        user?.let {
            mainViewModel.getUser(user.uid)
        }

        mainViewModel.userLogin.observe(viewLifecycleOwner,{ user ->
            user?.let {
                binding.profileNameEditTextSignup.setText(user.userName)
                binding.profilePasswordTextViewSignup.setText(user.userPassword)
                binding.profileEmailEditTextSignup.setText(user.userEmail)
                binding.profileSurnameEditTextSignup.setText(user.userSurname)
            }
        })

        binding.buttonLogout.setOnClickListener {
            Toast.makeText(requireContext(),"Succesfully Logout",Toast.LENGTH_LONG).show()
            mainViewModel.logout()
            findNavController().navigate(R.id.action_action_profile_to_loginActivity)
        }





        return binding.root
    }

}