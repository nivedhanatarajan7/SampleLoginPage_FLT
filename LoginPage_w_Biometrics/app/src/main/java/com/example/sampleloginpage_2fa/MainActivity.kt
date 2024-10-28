package com.example.sampleloginpage_2fa

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
            Display layout for app's login page
         */
        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)

        /*
            Storage for saved username for future attempts
         */
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "")
        usernameEditText.setText(savedUsername)

        /*
            Check to see if username is saved.
            If username is saved, default to using biometrics for authentication.
            If username is not saved, default to using password for authentication.
         */
        if (!savedUsername.isNullOrEmpty()) {
            checkBiometricAuthentication(this, object : BioMetricAuthCallBack {
                // Authentication success
                override fun onSuccess() {
                    Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                }

                // Authentication failure
                override fun onError() {
                    Toast.makeText(applicationContext, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }

            })
        }

        /*
            Action items when Log In button is clicked
         */
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // If credentials are correct
            if (validateLogin(username, password)) {
                // Save username for future attempts
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.apply()
                // Login successful message
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                // Login failure message
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /*
        Login validation (check username and password)
     */
    private fun validateLogin(username: String, password: String): Boolean {
        // Replace with your actual validation logic
        val validUsername = "user"
        val validPassword = "password"

        return (username == validUsername && password == validPassword)
    }
}