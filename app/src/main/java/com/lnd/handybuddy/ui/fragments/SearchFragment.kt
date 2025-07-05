package com.lnd.handybuddy.ui.fragments

import android.os.Bundle
import android.util.Log
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnd.handybuddy.R
import com.lnd.handybuddy.application.HandyBuddyApp
import com.lnd.handybuddy.data.TechnicianRepository
import com.lnd.handybuddy.data.remote.model.TechnicianDto
import com.lnd.handybuddy.databinding.FragmentSearchBinding
import com.lnd.handybuddy.ui.NetworkAware
import com.lnd.handybuddy.ui.adapters.TechniciansAdapter
import com.lnd.handybuddy.utils.Constants
import com.lnd.handybuddy.utils.NetworkMonitor
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), NetworkAware {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: TechnicianRepository
    private var shouldRetry = false
    private lateinit var networkMonitor: NetworkMonitor

    private lateinit var adapter: TechniciansAdapter
    private var allTechnicians: List<TechnicianDto> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkMonitor = NetworkMonitor(requireContext()) {
            if (shouldRetry) {
                fetchTechnicians()
            }
        }

        repository = (requireActivity().application as HandyBuddyApp).technicianRepository
        fetchTechnicians()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = filterTechnicians(s.toString())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun fetchTechnicians() {
        binding.pbLoading.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                allTechnicians = repository.getTechnician()

                adapter = TechniciansAdapter(allTechnicians) { selectedTechnician ->
                    // TODO: manejar clic
                }

                binding.rvTechnicians.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = this@SearchFragment.adapter
                    visibility = View.VISIBLE
                }

                shouldRetry = false
                Log.d(Constants.LOGTAG, "Loaded ${allTechnicians.size} technicians")
            } catch (e: Exception) {
                e.printStackTrace()
                shouldRetry = true
                Toast.makeText(
                    requireContext(),
                    context?.getString(R.string.connection_error),
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    private fun filterTechnicians(query: String) {
        val filtered = allTechnicians.filter {
            val name = it.name.orEmpty()
            val work = it.typeOfWork?.joinToString(", ") ?: ""
            name.contains(query, ignoreCase = true) || work.contains(query, ignoreCase = true)
        }
        adapter.updateData(filtered)
    }

    override fun onStart() {
        super.onStart()
        networkMonitor.register()
    }

    override fun onStop() {
        super.onStop()
        networkMonitor.unregister()
    }

    override fun onNetworkAvailable() {
        if (shouldRetry) {
            fetchTechnicians()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}