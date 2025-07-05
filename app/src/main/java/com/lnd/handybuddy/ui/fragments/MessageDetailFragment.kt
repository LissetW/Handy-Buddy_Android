package com.lnd.handybuddy.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lnd.handybuddy.R
import com.lnd.handybuddy.application.HandyBuddyApp
import com.lnd.handybuddy.data.MessageRepository
import com.lnd.handybuddy.data.TechnicianRepository
import com.lnd.handybuddy.data.mappers.toEntity
import com.lnd.handybuddy.data.remote.RetrofitHelper
import com.lnd.handybuddy.databinding.FragmentMessageDetailBinding
import com.lnd.handybuddy.ui.adapters.MessageAdapter
import com.lnd.handybuddy.utils.Constants
import kotlinx.coroutines.launch

class MessageDetailFragment : Fragment() {

    private var _binding: FragmentMessageDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MessageAdapter

    private val database by lazy {
        (requireActivity().application as HandyBuddyApp).database
    }

    private val messageDao by lazy {
        database.messageDao()
    }

    private val chatSessionDao by lazy {
        database.chatSessionDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sessionId = MessageDetailFragmentArgs.fromBundle(requireArguments()).sessionId

        lifecycleScope.launch {
            try {
                // Obtener mensajes de la sesión localmente
                val localMessages = messageDao.getMessagesForSession(sessionId)
                Log.d(Constants.LOGTAG, "Mensajes locales: ${localMessages.size}")

                if (localMessages.isNotEmpty()) {
                    // Obtener sesión para detalles de técnico localmente
                    val session = chatSessionDao.getChatSessionById(sessionId)
                    adapter = MessageAdapter(localMessages, session?.technicianId)
                    binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvMessages.adapter = adapter


                    if (session != null) {
                        binding.tvTechnicianName.text = (session.technicianName + " " + session.technicianLastName) ?: "Técnico"

                        Glide.with(requireContext())
                            .load(session.technicianImage)
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile)
                            .circleCrop()
                            .into(binding.imgTechnician)
                    }
                } else {
                    // Opcional: si no hay mensajes locales, puedes cargar desde remoto
                    val retrofit = RetrofitHelper().getRetrofit()
                    val repositoryMessage = MessageRepository(retrofit, database.chatSessionDao())
                    val repositoryTechnician = TechnicianRepository(retrofit)

                    val sessions = repositoryMessage.getMessageSessions()
                    val session = sessions.find { it.sessionId == sessionId }

                    if (session != null) {
                        val technicianId = session.messages.find { it.sender != "user" }?.sender ?: "unknown"
                        val localMessages = session.messages.map { it.toEntity(session.sessionId) }
                        adapter = MessageAdapter(localMessages, technicianId)

                        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
                        binding.rvMessages.adapter = adapter

                        val technicians = repositoryTechnician.getTechnician()
                        val technician = technicians.find { it.id == technicianId }
                        if (technician != null) {
                            binding.tvTechnicianName.text = technician.name ?: "Técnico"
                            Glide.with(requireContext())
                                .load(technician.image)
                                .placeholder(R.drawable.ic_profile)
                                .error(R.drawable.ic_profile)
                                .circleCrop()
                                .into(binding.imgTechnician)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(Constants.LOGTAG, "Error loading messages", e)
            }
            finally {
                binding.pbLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
