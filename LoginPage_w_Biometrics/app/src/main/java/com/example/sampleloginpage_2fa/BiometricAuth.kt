
package com.example.sampleloginpage_2fa

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

// Inspired by https://github.com/ChaitanyaDuse/SecureAndroidApp

fun checkBiometricAuthentication(
    activity: AppCompatActivity,
    bioMetricAuthCallBack: BioMetricAuthCallBack,
) {
    val biometricManager = BiometricManager.from(activity)
    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            biometric(activity, bioMetricAuthCallBack)
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
            }
            val authAvailabilityResult = activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable on device")

                })
            authAvailabilityResult.launch(enrollIntent)
        }
    }
}


private fun biometric(
    activity: AppCompatActivity,
    bioMetricAuthCallBack: BioMetricAuthCallBack,
) {

    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(activity.applicationContext,"Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                bioMetricAuthCallBack.onSuccess()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Authenticate with fingerprint or facial recognition")
        .setSubtitle("Use fingerprint or facial recognition")
        promptInfo.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setNegativeButtonText("Authenticate without biometrics")
    biometricPrompt.authenticate(promptInfo.build())
}

interface BioMetricAuthCallBack {
    fun onSuccess()
    fun onError()
}
