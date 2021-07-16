package com.emreozcan.flightapp.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentProfileBinding
import com.emreozcan.flightapp.models.User
import com.emreozcan.flightapp.viewmodel.MainViewModel


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var userCurrent: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        //TODO databindingle yap
        val planeAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.plane_anim)
        binding.imageView17.startAnimation(planeAnim)

        val textAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_alpha)
        binding.textView9.startAnimation(textAnim)

        val user = mainViewModel.currentUser

        user?.let {
            mainViewModel.getUser(it.uid)
        }


        mainViewModel.userLogin.observe(viewLifecycleOwner, { userLogin ->
            user?.let {
                binding.profileNameEditTextSignup.setText(userLogin.userName)
                binding.profilePasswordTextViewSignup.setText(userLogin.userPassword)
                binding.profileEmailEditTextSignup.setText(userLogin.userEmail)
                binding.profileSurnameEditTextSignup.setText(userLogin.userSurname)
                userCurrent = userLogin
            }
        })

        binding.buttonFlightHistory.setOnClickListener {
            val action = ProfileFragmentDirections.actionActionProfileToFlightHistoryFragment(userCurrent.flightHistoryList.toTypedArray())
            Navigation.findNavController(it).navigate(action)
        }

        binding.buttonLogout.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.succesfully_logout), Toast.LENGTH_LONG).show()
            mainViewModel.logout()
            findNavController().navigate(R.id.action_action_profile_to_loginActivity)
            activity?.finish()
        }
        shouldButtonClick()
        handleDoOnTextChanged()

        binding.buttonUpdate.setOnClickListener {
            val name = binding.profileNameEditTextSignup.text.toString().trim()
            val password = binding.profilePasswordTextViewSignup.text.toString().trim()
            val email = binding.profileEmailEditTextSignup.text.toString().trim()
            val surname = binding.profileSurnameEditTextSignup.text.toString().trim()
            if (name.isNotEmpty()&&password.isNotEmpty()&&email.isNotEmpty()&&surname.isNotEmpty()){
                mainViewModel.changeUserInformations(name, surname, email, password, requireContext())
            }
        }

        return binding.root
    }
    private fun shouldButtonClick() {
        binding.buttonUpdate.isClickable = !binding.profileNameTextInputLayout.isErrorEnabled
                && !binding.profileSurnameTextInputLayout.isErrorEnabled && !binding.profileEmailTextInputLayout.isErrorEnabled
                && !binding.profilePasswordTextInputLayout.isErrorEnabled

        if (!binding.buttonUpdate.isClickable) {
            binding.buttonUpdate.setBackgroundResource(R.drawable.button_notclickable_background)
        } else {
            binding.buttonUpdate.setBackgroundResource(R.drawable.button_background)
        }
    }



    private fun handleDoOnTextChanged() {
        binding.profileNameEditTextSignup.doOnTextChanged { text, _, _, _ ->
            val nameLenght = text!!.length
            when{
                nameLenght<2 -> {
                    binding.profileNameTextInputLayout.error = "Too short"
                }
                nameLenght>15 ->{
                    binding.profileNameTextInputLayout.error = "Too long"
                }
                else -> {
                    binding.profileNameTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

        binding.profileSurnameEditTextSignup.doOnTextChanged { text, _, _, _ ->
            val surnameLength = text!!.length
            when{
                surnameLength<2 -> {
                    binding.profileSurnameTextInputLayout.error = "Too short"
                }
                surnameLength>15 ->{
                    binding.profileSurnameTextInputLayout.error = "Too long"
                }
                else -> {
                    binding.profileSurnameTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

        binding.profileEmailEditTextSignup.doOnTextChanged { text, _, _, _ ->
            val emailLength = text!!.length
            when{
                emailLength<6 -> {
                    binding.profileEmailTextInputLayout.error = "Too short"
                }
                emailLength>20 ->{
                    binding.profileEmailTextInputLayout.error = "Too long"
                }
                else -> {
                    binding.profileEmailTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }

        binding.profilePasswordTextViewSignup.doOnTextChanged { text, _, _, _ ->
            val passwordLength = text!!.length
            when{
                passwordLength<6 -> {
                    binding.profilePasswordTextInputLayout.error = "Too short"
                }
                passwordLength>15 ->{
                    binding.profilePasswordTextInputLayout.error = "Too long"
                }
                else -> {
                    binding.profilePasswordTextInputLayout.isErrorEnabled = false
                }
            }
            shouldButtonClick()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}