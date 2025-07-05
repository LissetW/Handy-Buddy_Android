package com.lnd.handybuddy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lnd.handybuddy.databinding.FragmentTermsOfServiceBinding

class TermsOfServiceFragment : Fragment() {
    private var _binding: FragmentTermsOfServiceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTermsOfServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = binding.wvTermsOfService
        webView.loadUrl("file:///android_asset/terms_of_service.html")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}