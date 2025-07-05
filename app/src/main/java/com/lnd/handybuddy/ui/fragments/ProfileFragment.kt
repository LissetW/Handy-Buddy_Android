package com.lnd.handybuddy.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.lnd.handybuddy.R
import com.lnd.handybuddy.databinding.FragmentProfileBinding
import com.lnd.handybuddy.ui.adapters.ProfileNavAdapter
import com.lnd.handybuddy.utils.message

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navItems = listOf(
            NavItem.SectionHeader("Legal"),
            NavItem.Option("Terms of Service"),
            NavItem.Option("Privacy Policy"),
            NavItem.SectionHeader(""),
            NavItem.Option("Log Out")
        )
        val adapter = ProfileNavAdapter(navItems) { selected ->
            when (selected) {
                "Privacy Policy" -> {
                    findNavController().navigate(R.id.privacyPolicyFragment)
                }
                "Terms of Service" -> {
                    findNavController().navigate(R.id.termsOfServiceFragment)
                }
                "Log Out" -> {
                    FirebaseAuth.getInstance().signOut()
                    requireActivity().message(getString(R.string.success_logout))
                    findNavController().navigate(
                        R.id.loginFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.profileFragment, true)
                            .build()
                    )
                }
                else -> {
                    Toast.makeText(requireContext(), "Clicked on: $selected", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvProfile.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfile.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}