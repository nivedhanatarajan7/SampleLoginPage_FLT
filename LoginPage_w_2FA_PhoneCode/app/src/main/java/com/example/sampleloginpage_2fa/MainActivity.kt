package com.example.sampleloginpage_2fa

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var phone: EditText
    lateinit var verificationCode: EditText
    lateinit var loginBtn: Button
    lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()
        username = findViewById(R.id.usernamefield)
        password = findViewById(R.id.passwordfield)
        phone = findViewById(R.id.phone)
        verificationCode = findViewById(R.id.vid)
        loginBtn = findViewById(R.id.button)

        loginBtn.setOnClickListener {
            val phoneNumber = phone.text.toString()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Initiate phone number verification
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Sign in with the credential and then handle authentication
                        signInWithCredential(credential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        Toast.makeText(this@MainActivity, "2FA Verification Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                    override fun onCodeSent(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken)
                        this@MainActivity.verificationId = verificationId
                        // Prompt user to enter the verification code received on their phone
                        Toast.makeText(this@MainActivity, "Verification code sent to your phone", Toast.LENGTH_SHORT).show()

                        // Show the verification code input field and enable the login button
                        verificationCode.visibility = View.VISIBLE
                        loginBtn.isEnabled = true
                    }
                })
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User signed in successfully, now handle username/password authentication
                    val usernameinput = username.text.toString()
                    val passwordInput = password.text.toString()

                    // Replace with proper authentication logic (e.g., database check)
                    if (usernameinput.equals("admin") && passwordInput.equals("test")) {
                        Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Login Failed: Username/Password incorrect", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}