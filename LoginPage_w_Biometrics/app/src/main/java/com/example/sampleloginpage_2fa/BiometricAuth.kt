
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

/*
    Check if the system can use biometrics for authentication
 */
fun checkBiometricAuthentication(
    activity: AppCompatActivity,
    bioMetricAuthCallBack: BioMetricAuthCallBack,
) {
    val biometricManager = BiometricManager.from(activity)
    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        // Authenticate using biometrics
        BiometricManager.BIOMETRIC_SUCCESS -> {
            Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
            biometric(activity, bioMetricAuthCallBack)
        }

        // Request to apply for biometrics
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
            }

            // Device does not have biometrics available
            val authAvailabilityResult = activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable on device")

                })
            authAvailabilityResult.launch(enrollIntent)
        }
    }
}

/*
    Log in with biometrics
 */
private fun biometric(
    activity: AppCompatActivity,
    bioMetricAuthCallBack: BioMetricAuthCallBack,
) {
    val executor = ContextCompat.getMainExecutor(activity)
    // Biometrics prompt
    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            // Authentication error
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(activity.applicationContext,"Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            // Authentication succeeded
            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)
                bioMetricAuthCallBack.onSuccess()
            }

            // Authentication failed
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

            }
        })

    // Prompt info (display attributes)
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Authenticate with fingerprint or facial recognition")
        .setSubtitle("Use fingerprint or facial recognition")
        promptInfo.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setNegativeButtonText("Authenticate without biometrics")
    biometricPrompt.authenticate(promptInfo.build())
}

/*
    Biometrics authentication callback method
 */
interface BioMetricAuthCallBack {
    fun onSuccess()
    fun onError()
}
