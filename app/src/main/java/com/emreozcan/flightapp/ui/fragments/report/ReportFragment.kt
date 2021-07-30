package com.emreozcan.flightapp.ui.fragments.report

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentReportBinding
import com.emreozcan.flightapp.ui.fragments.flighthistory.FlightHistoryFragmentArgs
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoFilter


class ReportFragment : Fragment(), PermissionListener {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    private var selectedBitmap: Bitmap? = null

    private val args: ReportFragmentArgs by navArgs()
    private val mainViewModel: MainViewModel by viewModels()

    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)

        registerLauncher()

        binding.imageViewReport.setOnClickListener {
            Dexter.withContext(it.context).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(this).check()
        }

        binding.buttonSendReport.setOnClickListener {
            val complaint = binding.editTextReport.text.toString().trim()
            if (complaint.isNotEmpty()) {
                if (imageUri != null) {
                    mainViewModel.sendReport(imageUri!!, args.currentUser, complaint, this)
                }else{
                    mainViewModel.sendReport(null, args.currentUser, complaint, this)
                }
            }
        }


        return binding.root
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        imageUri = intentFromResult.data
                        if (imageUri != null) {
                            try {
                                if (Build.VERSION.SDK_INT >= 28) {
                                    val source = ImageDecoder.createSource(
                                        requireActivity().contentResolver,
                                        imageUri!!
                                    )
                                    selectedBitmap = ImageDecoder.decodeBitmap(source)

                                } else {
                                    selectedBitmap = MediaStore.Images.Media.getBitmap(
                                        requireActivity().contentResolver, imageUri
                                    )
                                }
                                binding.imageViewReport.setImageBitmap(selectedBitmap)

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    //TODO Alert Dialog Ekle
                }
            }

    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        val intentToGallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intentToGallery)

    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        //TODO Alert Dialog Ekle
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

}