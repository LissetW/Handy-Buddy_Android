package com.lnd.handybuddy.utils

import android.content.Context
import com.lnd.handybuddy.R

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

object ValidationUtils {

    fun validateEmail(context: Context, email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, context.getString(R.string.error_email_required))
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ValidationResult(false, context.getString(R.string.error_email_invalid))
            else -> ValidationResult(true)
        }
    }

    fun basicValidatePassword(context: Context, password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, context.getString(R.string.error_password_required))
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(context: Context, password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, context.getString(R.string.error_password_required))
            password.length < 6 -> ValidationResult(false, context.getString(R.string.error_password_too_short))
            !password.any { it.isUpperCase() } -> ValidationResult(false, context.getString(R.string.error_password_uppercase))
            !password.any { it.isLowerCase() } -> ValidationResult(false, context.getString(R.string.error_password_lowercase))
            !password.any { it.isDigit() } -> ValidationResult(false, context.getString(R.string.error_password_digit))
            !password.any { !it.isLetterOrDigit() } -> ValidationResult(false, context.getString(R.string.error_password_special))
            else -> ValidationResult(true)
        }
    }

    fun basicInputValidation(context: Context, input: String): ValidationResult {
        return when {
            input.isBlank() -> ValidationResult(false, context.getString(R.string.error_input_required))
            else -> ValidationResult(true)
        }
    }

    fun confirmPasswordsMatch(context: Context, password: String, confirmPassword: String): ValidationResult {
        return when {
            password.isBlank() || confirmPassword.isBlank() -> {
                ValidationResult(false, context.getString(R.string.error_password_required))
            }
            password != confirmPassword -> {
                ValidationResult(false, context.getString(R.string.error_passwords_do_not_match))
            }
            else -> {
                ValidationResult(true)
            }
        }
    }
}
