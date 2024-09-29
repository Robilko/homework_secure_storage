package com.otus.securehomework.presentation

import android.app.Activity
import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import com.otus.securehomework.R
import com.otus.securehomework.presentation.auth.AuthActivity
import com.otus.securehomework.presentation.home.HomeActivity

internal val biometricPromptInfo = BiometricPrompt.PromptInfo.Builder()
    .setTitle("Authentication")
    .setSubtitle("Confirm your identity")
    .setNegativeButtonText("Cancel")
    .setConfirmationRequired(false)
    .build()

internal fun Context.canAuthenticate() = BiometricManager.from(this)
    .canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK)

internal fun Activity.getAuthCallback() = object : BiometricPrompt.AuthenticationCallback() {
    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        showToastShort("Authentication successful")
        startNewActivity(HomeActivity::class.java)
    }

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        showToastShort("Unrecoverable error => $errString")
        startNewActivity(AuthActivity::class.java)
    }

    override fun onAuthenticationFailed() {
        showToastShort("Could not recognise the user")
        startNewActivity(AuthActivity::class.java)
    }
}

internal fun Context.getBiometricErrorMessage(canAuthenticate: Int) = when (canAuthenticate) {
    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> getString(R.string.biometric_error_no_hardware)
    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> getString(R.string.biometric_error_hw_unavailable)
    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> getString(R.string.biometric_error_none_enrolled)
    BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> getString(R.string.biometric_error_security_update_required)
    BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> getString(R.string.biometric_error_unsupported)
    BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> getString(R.string.biometric_status_unknown)
    else -> getString(R.string.other_biometric_error)
}