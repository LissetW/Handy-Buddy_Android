package com.lnd.handybuddy.ui.fragments

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lnd.handybuddy.R
import com.lnd.handybuddy.application.HandyBuddyApp
import com.lnd.handybuddy.data.MessageRepository
import com.lnd.handybuddy.data.TechnicianRepository
import com.lnd.handybuddy.data.local.entity.ChatSessionEntity
import com.lnd.handybuddy.databinding.FragmentChatBinding
import com.lnd.handybuddy.ui.NetworkAware
import com.lnd.handybuddy.ui.adapters.ChatSessionAdapter
import com.lnd.handybuddy.utils.Constants
import com.lnd.handybuddy.utils.NetworkMonitor
import kotlinx.coroutines.launch
import android.content.Context
import com.lnd.handybuddy.data.mappers.toEntity

class ChatFragment : Fragment(), NetworkAware {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageRepository: MessageRepository
    private lateinit var technicianRepository: TechnicianRepository

    private lateinit var adapter: ChatSessionAdapter
    private var shouldRetry = false
    private lateinit var networkMonitor: NetworkMonitor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkMonitor = NetworkMonitor(requireContext()) { isConnected ->
            if (isConnected) {
                if (shouldRetry) fetchMessages()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.connection_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        technicianRepository = (requireActivity().application as HandyBuddyApp).technicianRepository
        messageRepository = (requireActivity().application as HandyBuddyApp).messageRepository
        fetchMessages()
    }

    private fun fetchMessages() {
        binding.pbLoading.visibility = View.VISIBLE

        val db = (requireActivity().application as HandyBuddyApp).database
        val daoChats = db.chatSessionDao()
        val daoMessages = db.messageDao()

        lifecycleScope.launch {
            try {
                // 1. Mostrar lo que tenga local
                val localChats = daoChats.getAllChatSessions()
                showChats(localChats)

                // 2. Si hay internet, descarga, guarda y muestra
                if (hasInternet()) {
                    val remoteTechnicians = technicianRepository.getTechnician()
                    val remoteChats = messageRepository.getMessageSessions()

                    // Mapea sesiones a entidades locales
                    val updatedChats = remoteChats.map { session ->
                        val technicianId = session.messages.firstOrNull { it.sender != "user" }?.sender
                        val technician = remoteTechnicians.find { it.id == technicianId }
                        session.toEntity(technician)
                    }

                    // Mapea mensajes a entidades locales
                    val allMessages = remoteChats.flatMap { session ->
                        session.messages.map { it.toEntity(session.sessionId) }
                    }

                    // Guarda en base local
                    daoChats.insertChatSessions(updatedChats)
                    daoMessages.insertMessages(allMessages)

                    // Muestra en UI
                    showChats(updatedChats)
                }
                else
                {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.connection_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                shouldRetry = false
                Log.d(Constants.LOGTAG, "Loaded chats from local and remote")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(Constants.LOGTAG, "Error al cargar chats", e)
                shouldRetry = true
                Toast.makeText(
                    requireContext(),
                    getString(R.string.connection_error),
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }



    private fun showChats(chats: List<ChatSessionEntity>) {
        if (chats.isEmpty()) {
            binding.rvChatSessions.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
        } else {
            if (!::adapter.isInitialized) {
                adapter = ChatSessionAdapter(chats) { session ->
                    val action = ChatFragmentDirections.actionChatFragmentToMessageDetailFragment(session.sessionId)
                    findNavController().navigate(action)
                }
                binding.rvChatSessions.layoutManager = LinearLayoutManager(requireContext())
                binding.rvChatSessions.adapter = adapter
            } else {
                adapter.updateData(chats)
            }
            binding.rvChatSessions.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
        }
    }

    fun hasInternet(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
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
        if(shouldRetry) {
            fetchMessages()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}