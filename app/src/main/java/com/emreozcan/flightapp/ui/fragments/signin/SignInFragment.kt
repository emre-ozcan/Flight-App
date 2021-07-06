package com.emreozcan.flightapp.ui.fragments.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

            findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
        }


        binding.buttonSignup.setOnClickListener {
            val name = binding.nameEditTextSignup.text.toString().trim()
            val surname = binding.surnameEditTextSignup.text.toString().trim()
            val email = binding.emailEditTextSignup.text.toString().trim()
            val password = binding.passwordTextViewSignup.text.toString().trim()

            if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                mainViewModel.signIn(
                    email,
                    password,
                    name,
                    surname,
                    this
                )
            }else{
                showFieldSnackbar(it)
            }


        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}