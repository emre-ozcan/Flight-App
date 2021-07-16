package com.emreozcan.flightapp.ui.fragments.signin

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
import com.emreozcan.flightapp.databinding.FragmentSignInBinding
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.firebase.analytics.FirebaseAnalytics


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.closeImageViewSignup.setOnClickListener {
            findNavController().popBackStack()
        }

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        handleFocus()
        shouldButtonClick()
        handleDoOnTextChanged()

        binding.buttonSignup.setOnClickListener {
            val name = binding.nameEditTextSignup.text.toString().trim()
            val surname = binding.surnameEditTextSignup.text.toString().trim()
            val email = binding.emailEditTextSignup.text.toString().trim()
            val password = binding.passwordEditTextSignup.text.toString().trim()

            if (name.isNotEmpty()&&surname.isNotEmpty()&&email.isNotEmpty()&&password.isNotEmpty()){
                signupButtonClicked(name,surname,email,password)
            }
        }

        return binding.root
    }

    private fun signupButtonClicked(
        name: String,
        surname: String,
        email: String,
        password: String
    ) {
        val bundle = Bundle().apply {
            this.putString(FirebaseAnalytics.Param.METHOD, "signup")
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)


        mainViewModel.signIn(
            email,
            password,
            name,
            surname,
            this
        )
    }

    private fun shouldButtonClick() {
        binding.buttonSignup.isClickable = !binding.signinNameTextInputLayout.isErrorEnabled
                && !binding.signinSurnameTextInputLayout.isErrorEnabled && !binding.signinEmailTextInputLayout.isErrorEnabled
                && !binding.signinPasswordTextInputLayout.isErrorEnabled

        if (!binding.buttonSignup.isClickable) {
            binding.buttonSignup.setBackgroundResource(R.drawable.button_notclickable_background)
        } else {
            binding.buttonSignup.setBackgroundResource(R.drawable.button_background)
        }
    }

    private fun handleDoOnTextChanged() {
        binding.nameEditTextSignup.doOnTextChanged { text, _, _, _ ->
            val nameLenght = text!!.length
            when {
                nameLenght < 2 -> {
                    binding.signinNameTextInputLayout.error = getString(R.string.too_short)
                }
                nameLenght > 15 -> {
                    binding.signinNameTextInputLayout.error = getString(R.string.too_long)
                }
                else -> {
                    binding.signinNameTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

        binding.surnameEditTextSignup.doOnTextChanged { text, _, _, _ ->
            val surnameLength = text!!.length
            when {
                surnameLength < 2 -> {
                    binding.signinSurnameTextInputLayout.error = getString(R.string.too_short)
                }
                surnameLength > 15 -> {
                    binding.signinSurnameTextInputLayout.error = getString(R.string.too_long)
                }
                else -> {
                    binding.signinSurnameTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

        binding.emailEditTextSignup.doOnTextChanged { text, _, _, _ ->
            val emailLength = text!!.length
            when {
                emailLength < 6 -> {
                    binding.signinEmailTextInputLayout.error = getString(R.string.too_short)
                }
                emailLength > 20 -> {
                    binding.signinEmailTextInputLayout.error = getString(R.string.too_long)
                }
                else -> {
                    binding.signinEmailTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

        binding.passwordEditTextSignup.doOnTextChanged { text, _, _, _ ->
            val passwordLength = text!!.length
            when {
                passwordLength < 6 -> {
                    binding.signinPasswordTextInputLayout.error = getString(R.string.too_short)
                }
                passwordLength > 15 -> {
                    binding.signinPasswordTextInputLayout.error = getString(R.string.too_long)
                }
                else -> {
                    binding.signinPasswordTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

    }

    private fun handleFocus() {
        binding.nameEditTextSignup.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.surnameEditTextSignup.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.surnameEditTextSignup.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.emailEditTextSignup.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.emailEditTextSignup.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.passwordEditTextSignup.requestFocus()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.passwordEditTextSignup.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.buttonSignup.callOnClick()
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