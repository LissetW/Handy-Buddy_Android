package com.lnd.handybuddy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lnd.handybuddy.databinding.FragmentPrivacyPolicyBinding

class PrivacyPolicyFragment : Fragment() {
    private var _binding: FragmentPrivacyPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = binding.wvPrivacyPolicy
        webView.loadUrl("file:///android_asset/privacy_policy.html")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}