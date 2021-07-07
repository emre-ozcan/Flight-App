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
import com.emreozcan.flightapp.util.Constants.Companion.LEAST_VERSION_CODE
import com.emreozcan.flightapp.util.Constants.Companion.STORE_URL
import com.emreozcan.flightapp.util.Constants.Companion.SUGGESTED_VERSION_CODE
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
            handleForceUpdate()
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
            MaterialAlertDialogBuilder(requireContext()).setTitle("İnternet Bağlantısı Yok")
                .setMessage("Uygulamanın çalışabilmesi için internet bağlantısına ihtiyaç vardır !")
                .setPositiveButton("Tamam") { dialog, which ->
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }.setCancelable(false).show()
        }



        return binding.root
    }
    private fun handleForceUpdate(){
        val remoteConfigDefaults = hashMapOf<String, Any>()
        remoteConfigDefaults[STORE_URL] =
            "https://play.google.com/store/apps/details?id=com.nczsoftware.waterreminderapp&hl=tr&gl=US"
        remoteConfigDefaults[SUGGESTED_VERSION_CODE] = 2
        remoteConfigDefaults[LEAST_VERSION_CODE] = 1
        remoteConfigDefaults[FORCE_UPDATE_REQUIRED] = false

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
        if (isForce){
            updateNeed = true
            MaterialAlertDialogBuilder(requireContext()).setTitle("Yeni Bir Güncelleme Var")
                .setMessage("Uygulamanın yeni bir sürümü mevcutur kullanmak için lütfen güncelleme yapınız !")
                .setPositiveButton("Tamam"){ dialog,which ->
                    goToPlayStore(updateUrl)
                }.setCancelable(false).show()
        }else{
            updateNeed = true
            MaterialAlertDialogBuilder(requireContext()).setTitle("Yeni Bir Güncelleme Var")
                .setMessage("Uygulamanın yeni bir sürümü mevcutur güncellemek ister misiniz ?")
                .setPositiveButton("Evet"){ dialog,which ->
                    goToPlayStore(updateUrl)
                }
                .setNegativeButton("Hayır"){ dialog,which ->
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