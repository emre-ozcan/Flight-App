package com.emreozcan.flightapp.ui.fragments.qrscanner

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.emreozcan.flightapp.R
import com.emreozcan.flightapp.databinding.FragmentQRScannerBinding
import com.emreozcan.flightapp.models.QRCodeAirport
import com.emreozcan.flightapp.viewmodel.MainViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class QRScannerFragment : Fragment(),PermissionListener {

    private var _binding: FragmentQRScannerBinding? = null
    private val binding get() = _binding!!

    private  var codeScanner: CodeScanner? = null

    private val mainViewModel: MainViewModel by viewModels()

    private var airportCodeList = arrayListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQRScannerBinding.inflate(inflater,container,false)

        mainViewModel.getAirportCodes(requireContext())
        mainViewModel.airportCodesList.observe(viewLifecycleOwner,{ list ->
            for (airport in list){
                airportCodeList.add(airport.airportCode!!)
            }
        })


        Dexter.withContext(requireContext()).withPermission(Manifest.permission.CALL_PHONE)
            .withListener(this).check()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        val scannerView = binding.codeScanner
        codeScanner = CodeScanner(requireContext(),scannerView)

        codeScanner!!.decodeCallback = DecodeCallback { code ->
            activity?.runOnUiThread {
                if (airportCodeList.contains(code.text)){
                    mainViewModel.getFlightsWithQR(code.text,this)
                }else{
                    Toast.makeText(requireContext(),"QR code is wrong",Toast.LENGTH_LONG).show()
                }

            }
        }
        binding.codeScanner.setOnClickListener {
            codeScanner!!.startPreview()
        }

    }

    override fun onResume() {
        super.onResume()
        if (codeScanner != null){
            codeScanner!!.startPreview()
        }

    }

    override fun onPause() {
        if (codeScanner != null){
            codeScanner!!.releaseResources()
        }
        super.onPause()
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        TODO("Not yet implemented")
    }
}