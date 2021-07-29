package com.emreozcan.flightapp.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentLoginBinding
import com.emreozcan.flightapp.ui.LoginActivity
import com.emreozcan.flightapp.util.LocaleHelper
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.firebase.analytics.FirebaseAnalytics


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var emailTextLength = 0
    private var passwordTextLength = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        binding.buttonLoginSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }

        shouldButtonClick()
        handleTextFocus()
        handleDoOnTextChanged()


        binding.buttonLogin.setOnClickListener {
            val email = binding.emailEditTextLogin.text.toString().trim()
            val password = binding.passwordEditTextLogin.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()){
                loginButton(email,password)
            }
        }

        return binding.root
    }

    private fun loginButton(email: String, password: String) {
        val bundle = Bundle().apply {
            this.putString(FirebaseAnalytics.Param.METHOD, "login")
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle)

        mainViewModel.login(email, password, this)
    }

    private fun handleDoOnTextChanged(){
        binding.emailEditTextLogin.doOnTextChanged { text, _, _, _ ->
            emailTextLength = text!!.length
            when {
                emailTextLength < 6 -> {
                    binding.loginEmailTextInputLayout.error = getString(R.string.too_short)
                }
                emailTextLength > 20 -> {
                    binding.loginEmailTextInputLayout.error = getString(R.string.too_long)
                }
                else -> {
                    binding.loginEmailTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

        binding.passwordEditTextLogin.doOnTextChanged { text, _, _, _ ->
            passwordTextLength = text!!.length
            when {
                passwordTextLength < 6 -> {
                    binding.loginPasswordTextInputLayout.error = getString(R.string.too_short)
                }
                passwordTextLength > 20 -> {
                    binding.loginPasswordTextInputLayout.error = getString(R.string.too_long)
                }
                else -> {
                    binding.loginPasswordTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

    }

    private fun shouldButtonClick() {
        binding.buttonLogin.isClickable = !binding.loginEmailTextInputLayout.isErrorEnabled
                && !binding.loginPasswordTextInputLayout.isErrorEnabled

        if (!binding.buttonLogin.isClickable) {
            binding.buttonLogin.setBackgroundResource(R.drawable.button_notclickable_background)
        } else {
            binding.buttonLogin.setBackgroundResource(R.drawable.button_background)
        }
    }

    private fun handleTextFocus() {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}