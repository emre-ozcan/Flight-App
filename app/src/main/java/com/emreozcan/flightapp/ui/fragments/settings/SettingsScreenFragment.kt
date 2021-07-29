package com.emreozcan.flightapp.ui.fragments.settings

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentSettingsScreenBinding
import com.emreozcan.flightapp.ui.MainActivity
import com.emreozcan.flightapp.util.LocaleHelper
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


class SettingsScreenFragment : Fragment() {
    private var _binding: FragmentSettingsScreenBinding? = null
    private val binding get() = _binding!!

    private var selectedLanguageIndex = 3
    private  var currentLanguageIndex: Int? = null

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsScreenBinding.inflate(inflater,container,false)

        val languages = arrayListOf(getString(R.string.language_turkish),getString(R.string.language_English))

        mainViewModel.readLanguageCode.observe(viewLifecycleOwner,{languageCode ->
            if (languageCode != "sys"){
                currentLanguageIndex = languageToIndex(languageCode)
            }else{
                currentLanguageIndex = 1
            }

        })
        binding.cardViewLanguage.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.languages_string))
                .setNeutralButton(getString(R.string.alert_cancel)){ _, _ ->

                }
                .setPositiveButton(R.string.alert_okay){_,_ ->
                    val languageCode = getLanguageCode(selectedLanguageIndex)
                    if (selectedLanguageIndex!= 3 && languageCode != Locale.getDefault().displayLanguage){
                        mainViewModel.saveLanguageCode(languageCode)
                        LocaleHelper.setLocale(requireContext(),languageCode)
                        activity?.recreate()
                    }
                }
                .setSingleChoiceItems(languages.toTypedArray(),currentLanguageIndex!!){_,which ->
                    selectedLanguageIndex = which


                }.show()
        }

        return binding.root
    }
    private fun languageToIndex(language: String) = when(language){
        "tr" -> 0
        else -> 1
    }

    private fun getLanguageCode(position: Int) = when(position){
        0 -> "tr"
        1 -> "en"
        else -> "none"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}