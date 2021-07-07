package com.emreozcan.flightapp.ui.fragments.profile

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentProfileBinding
import com.emreozcan.flightapp.util.showFieldSnackbar
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val planeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.plane_anim)
        binding.imageView17.startAnimation(planeAnim)

        val textAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_alpha)
        binding.textView9.startAnimation(textAnim)

        val user = mainViewModel.currentUser

        user?.let {
            mainViewModel.getUser(it.uid)
        }

        mainViewModel.userLogin.observe(viewLifecycleOwner, { useLogin ->
            user?.let {
                binding.profileNameEditTextSignup.setText(useLogin.userName)
                binding.profilePasswordTextViewSignup.setText(useLogin.userPassword)
                binding.profileEmailEditTextSignup.setText(useLogin.userEmail)
                binding.profileSurnameEditTextSignup.setText(useLogin.userSurname)
            }
        })



        binding.buttonLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Succesfully Logout", Toast.LENGTH_LONG).show()
            mainViewModel.logout()
            findNavController().navigate(R.id.action_action_profile_to_loginActivity)
            activity?.finish()
        }

        binding.buttonUpdate.setOnClickListener {
            val name = binding.profileNameEditTextSignup.text.toString().trim()
            val password = binding.profilePasswordTextViewSignup.text.toString().trim()
            val email = binding.profileEmailEditTextSignup.text.toString().trim()
            val surname = binding.profileSurnameEditTextSignup.text.toString().trim()

            var back = false

            if (name.length < 2) {
                binding.profileNameTextInputLayout.error = "Name lenght is too short"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.profileNameTextInputLayout.error = null
                }, 3000)
            }

            if (password.length < 6) {
                binding.profilePasswordTextInputLayout.error = "Password lenght is too short"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.profilePasswordTextInputLayout.error = null
                }, 3000)
            }

            if (email.length < 6) {
                binding.profileEmailTextInputLayout.error = "Email lenght is too short"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.profileEmailTextInputLayout.error = null
                }, 3000)
            }

            if (!email.contains("@")&& email.length>5 ){
                binding.profileEmailTextInputLayout.error = "Please enter true format email"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.profileEmailTextInputLayout.error = null
                }, 3000)
            }

            if (surname.length < 2) {
                binding.profileSurnameTextInputLayout.error = "Surname lenght is too short"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.profileSurnameTextInputLayout.error = null
                }, 3000)
            }
            if (back){
                return@setOnClickListener
            }

            mainViewModel.changeUserInformations(name, surname, email, password, requireContext())


        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}