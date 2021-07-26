package com.emreozcan.flightapp.ui.fragments.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentSplashBinding
import com.emreozcan.flightapp.util.Constants
import com.emreozcan.flightapp.util.Constants.Companion.FORCE_UPDATE_REQUIRED
import com.emreozcan.flightapp.util.Constants.Companion.FORCE_UPDATE_REQUIRED_DEFAULT
import com.emreozcan.flightapp.util.Constants.Companion.LEAST_VERSION_CODE
import com.emreozcan.flightapp.util.Constants.Companion.LEAST_VERSION_CODE_DEFAULT
import com.emreozcan.flightapp.util.Constants.Companion.STORE_URL
import com.emreozcan.flightapp.util.Constants.Companion.STORE_URL_DEFAULT
import com.emreozcan.flightapp.util.Constants.Companion.SUGGESTED_VERSION_CODE
import com.emreozcan.flightapp.util.Constants.Companion.SUGGESTED_VERSION_CODE_DEFAULT
import com.emreozcan.flightapp.util.DataResult
import com.emreozcan.flightapp.util.ForceUpdateChecker
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig


class SplashFragment : Fragment(), ForceUpdateChecker.OnUpdateNeedListener {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    private var isOnboardingShowed = false

    private var hasInternetConnection = false

    private val remoteConfig = Firebase.remoteConfig

    private var updateNeed = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hasInternetConnection = mainViewModel.hasInternetConnection()
        if (hasInternetConnection){
            handleForceUpdate()
        }else{
            mainViewModel.dataResult.value = DataResult.Error("There is any Internet Connection !")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        startAnimations()

        mainViewModel.readOnboarding.observe(viewLifecycleOwner, {
            isOnboardingShowed = it
        })

        if (hasInternetConnection) {
            if (!updateNeed){
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    when {
                        mainViewModel.currentUser != null -> {
                            findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
                            activity?.finish()
                        }
                        isOnboardingShowed -> {
                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                        }
                        else -> {
                            findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                        }
                    }

                }, 2000)
            }

        } else {
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.internet_connection))
                .setMessage(getString(R.string.internet_connection_needed))
                .setPositiveButton(R.string.alert_okay) { dialog, which ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }.setCancelable(false).show()
        }


        return binding.root
    }
    private fun handleForceUpdate(){
        val remoteConfigDefaults = hashMapOf<String, Any>()
        remoteConfigDefaults[STORE_URL] = STORE_URL_DEFAULT
        remoteConfigDefaults[SUGGESTED_VERSION_CODE] = SUGGESTED_VERSION_CODE_DEFAULT
        remoteConfigDefaults[LEAST_VERSION_CODE] = LEAST_VERSION_CODE_DEFAULT
        remoteConfigDefaults[FORCE_UPDATE_REQUIRED] = FORCE_UPDATE_REQUIRED_DEFAULT

        remoteConfig.setDefaultsAsync(remoteConfigDefaults)
        remoteConfig.fetch(60).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig.fetchAndActivate()
            }
        }

        ForceUpdateChecker.with(requireContext()).onUpdateNeeded(this).check()
    }
    private fun startAnimations() {
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.plane_anim)
        binding.imageView.startAnimation(anim)

        val animText = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_alpha)
        binding.textView.startAnimation(animText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


    private fun goToPlayStore(updateUrl: String){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onUpdateNeed(updateUrl: String, isForce: Boolean) {
        updateNeed = true
        if (isForce){

            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.new_update))
                .setMessage(getString(R.string.must_update))
                .setPositiveButton(R.string.alert_okay){ dialog,which ->
                    goToPlayStore(updateUrl)
                }.setCancelable(false).show()
        }else{
            MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.new_update)
                .setMessage(getString(R.string.wanna_update))
                .setPositiveButton(getString(R.string.alert_yes)){ dialog, which ->
                    goToPlayStore(updateUrl)
                }
                .setNegativeButton(getString(R.string.alert_no)){ dialog, which ->
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        when {
                            mainViewModel.currentUser != null -> {
                                findNavController().navigate(R.id.action_splashFragment_to_mainActivity)
                                activity?.finish()
                            }
                            isOnboardingShowed -> {
                                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                            }
                            else -> {
                                findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                            }
                        }

                    }, 200)
                }.setCancelable(false).show()
        }
    }
}