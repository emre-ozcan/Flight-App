package com.emreozcan.flightapp.ui.fragments.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentLoginBinding
import com.emreozcan.flightapp.viewmodel.MainViewModel


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.buttonLoginSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }

        binding.emailEditTextLogin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.passwordEditTextLogin.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.passwordEditTextLogin.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.buttonLogin.callOnClick()
                return@setOnEditorActionListener true
            }
            false
        }


        binding.buttonLogin.setOnClickListener {
            val email = binding.emailEditTextLogin.text.toString().trim()
            val password = binding.passwordEditTextLogin.text.toString().trim()

            var back = false

            if (email.length < 6) {
                binding.loginEmailTextInputLayout.error = "Email lenght is too short!"
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loginEmailTextInputLayout.error = null
                }, 3000)
                back = true
            }else if (email.length>20){
                binding.loginEmailTextInputLayout.error = "Email lenght is too long!"
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loginEmailTextInputLayout.error = null
                }, 3000)
                back = true
            }

            if (password.length < 6) {
                binding.loginPasswordTextInputLayout.error = "Password length is too short!"
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loginPasswordTextInputLayout.error = null
                }, 3000)
                back = true
            }else if (password.length >15){
                binding.loginPasswordTextInputLayout.error = "Password length is too long!"
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.loginPasswordTextInputLayout.error = null
                }, 3000)
                back = true
            }

            if (back) {
                return@setOnClickListener
            }
            mainViewModel.login(email, password, this)

        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}