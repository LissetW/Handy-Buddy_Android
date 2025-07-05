package com.lnd.handybuddy.ui.login

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.lnd.handybuddy.R
import com.lnd.handybuddy.databinding.FragmentSignUpBinding
import com.lnd.handybuddy.utils.message
import com.lnd.handybuddy.utils.ValidationUtils

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseAuth = FirebaseAuth.getInstance()

        initializeFields()

        binding.btnSignUp.setOnClickListener {
            if (validateFields()) {
                binding.progressBar.visibility = View.VISIBLE
                createUser(binding.tietEmail.text.toString(), binding.tietPassword.text.toString())
            }
        }
    }

    private fun validateFields(): Boolean {
        val fullName = binding.tietFullName.text.toString().trim()
        val email = binding.tietEmail.text.toString().trim()
        val password = binding.tietPassword.text.toString()
        val confirmPassword = binding.tietConfirmPassword.text.toString()
        var fieldValid = true

        val nameValidation = ValidationUtils.basicInputValidation(requireContext(), fullName)
        val emailValidation = ValidationUtils.validateEmail(requireContext(), email)
        val passwordValidation = ValidationUtils.validatePassword(requireContext(), password)
        val confirmValidation = ValidationUtils.confirmPasswordsMatch(requireContext(), password, confirmPassword)

        if (!nameValidation.isValid) {
            binding.tilFullName.error = nameValidation.errorMessage
            binding.tietFullName.requestFocus()
            binding.tilFullName.errorIconDrawable = null
            fieldValid = false
        }

        if (!emailValidation.isValid) {
            binding.tilEmail.error = emailValidation.errorMessage
            binding.tietEmail.requestFocus()
            binding.tilEmail.errorIconDrawable = null
            fieldValid = false
        }

        if (!passwordValidation.isValid) {
            binding.tilPassword.error = passwordValidation.errorMessage
            binding.tietPassword.requestFocus()
            binding.tilPassword.errorIconDrawable = null
            fieldValid = false
        }

        if (!confirmValidation.isValid) {
            binding.tilConfirmPassword.error = confirmValidation.errorMessage
            binding.tietConfirmPassword.requestFocus()
            binding.tilConfirmPassword.errorIconDrawable = null
            fieldValid = false
        }
        return fieldValid
    }


    private fun createUser(usr: String, psw: String) {
        firebaseAuth.createUserWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            binding.progressBar.visibility = View.GONE
            if (authResult.isSuccessful) {
                firebaseAuth.currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {
                        requireActivity().message(getString(R.string.verification_email_sent))
                    }
                    ?.addOnFailureListener {
                        requireActivity().message(getString(R.string.verification_email_not_sent))
                    }

                requireActivity().message(getString(R.string.user_created))
                actionLoginSuccessful()
            } else {
                handleErrors(authResult)
            }
        }
    }

    private fun actionLoginSuccessful() {
        // Navegar al home o a login
        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
    }

    private fun handleErrors(task: Task<AuthResult>) {
        val message = when (val exception = task.exception) {
            is FirebaseAuthWeakPasswordException -> getString(R.string.invalid_long_password)
            is FirebaseAuthInvalidCredentialsException -> getString(R.string.invalid_email_format)
            is FirebaseAuthUserCollisionException -> getString(R.string.email_already_registered)
            else -> exception?.localizedMessage ?: getString(R.string.unknown_error)
        }
        requireActivity().message(message)
    }

    private fun initializeFields(){
        binding.tietEmail.addTextChangedListener {
            binding.tilEmail.error = null
        }

        binding.tietFullName.addTextChangedListener {
            binding.tilFullName.error = null
        }

        binding.tietPassword.addTextChangedListener {
            binding.tilPassword.error = null
        }

        binding.tietConfirmPassword.addTextChangedListener {
            binding.tilConfirmPassword.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
