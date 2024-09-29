package com.otus.securehomework.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.otus.securehomework.R
import com.otus.securehomework.data.source.local.UserPreferences
import com.otus.securehomework.presentation.auth.AuthActivity
import com.otus.securehomework.presentation.biometricPromptInfo
import com.otus.securehomework.presentation.canAuthenticate
import com.otus.securehomework.presentation.getAuthCallback
import com.otus.securehomework.presentation.getBiometricErrorMessage
import com.otus.securehomework.presentation.showToastShort
import com.otus.securehomework.presentation.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userPreferences.accessToken.asLiveData().observe(this) { token ->
            token?.let {
                when (val canAuthenticate = canAuthenticate()) {
                    BiometricManager.BIOMETRIC_SUCCESS -> {
                        BiometricPrompt(
                            this,
                            ContextCompat.getMainExecutor(this),
                            getAuthCallback()
                        ).authenticate(biometricPromptInfo)
                    }

                    else -> {
                        startNewActivity(AuthActivity::class.java)
                        val errorMessage = getBiometricErrorMessage(canAuthenticate)
                        showToastShort(errorMessage)
                    }
                }
            } ?: startNewActivity(AuthActivity::class.java)
        }
    }
}