package com.emreozcan.flightapp.ui.fragments.signin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentSignInBinding
import com.emreozcan.flightapp.util.showFieldSnackbar
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.closeImageViewSignup.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.nameEditTextSignup.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                binding.surnameEditTextSignup.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.surnameEditTextSignup.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                binding.emailEditTextSignup.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.emailEditTextSignup.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                binding.passwordEditTextSignup.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.passwordEditTextSignup.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                binding.buttonSignup.callOnClick()
                return@setOnEditorActionListener true
            }
            false
        }


        binding.buttonSignup.setOnClickListener {
            val name = binding.nameEditTextSignup.text.toString().trim()
            val surname = binding.surnameEditTextSignup.text.toString().trim()
            val email = binding.emailEditTextSignup.text.toString().trim()
            val password = binding.passwordEditTextSignup.text.toString().trim()

            var back = false

            if (name.length < 2) {
                binding.signinNameTextInputLayout.error = "Name lenght is too short!"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.signinNameTextInputLayout.error = null
                }, 3000)
            }else if (name.length > 15){
                binding.signinNameTextInputLayout.error = "Name lenght is too long!"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.signinNameTextInputLayout.error = null
                }, 3000)
            }

            if (surname.length < 2) {
                binding.signinSurnameTextInputLayout.error = "Surname lenght is too short!"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.signinSurnameTextInputLayout.error = null
                }, 3000)
            }else if (surname.length > 15){
                binding.signinSurnameTextInputLayout.error = "Surname lenght is too long!"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.signinSurnameTextInputLayout.error = null
                }, 3000)
            }

            if (email.length < 6) {
                binding.signinEmailTextInputLayout.error = "Email lenght is too short!"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.signinEmailTextInputLayout.error = null
                }, 3000)
            }else if (email.length >20){
                binding.signinEmailTextInputLayout.error = "Email lenght is too long!"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.signinEmailTextInputLayout.error = null
                }, 3000)
            }
            if (password.length < 6) {
                binding.signinPasswordTextInputLayout.error = "Password lenght is too short!"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.signinPasswordTextInputLayout.error = null
                }, 3000)
            }else if (password.length>15){
                binding.signinPasswordTextInputLayout.error = "Password lenght is too long!"
                back = true
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.signinPasswordTextInputLayout.error = null
                }, 3000)
            }

            if (back) {
                return@setOnClickListener
            }

            mainViewModel.signIn(
                email,
                password,
                name,
                surname,
                this
            )


        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}