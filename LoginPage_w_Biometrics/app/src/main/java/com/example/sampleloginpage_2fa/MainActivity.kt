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

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.login_button)

        // Load saved username
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "")
        usernameEditText.setText(savedUsername)

        if (!savedUsername.isNullOrEmpty()) {
            checkBiometricAuthentication(this, object : BioMetricAuthCallBack {
                override fun onSuccess() {
                    Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                }

                override fun onError() {
                    Toast.makeText(applicationContext, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }

            })
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(username, password)) {
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.apply()
                // Proceed with login
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validateLogin(username: String, password: String): Boolean {
        // Replace with your actual validation logic
        val validUsername = "user"
        val validPassword = "password"

        return (username == validUsername && password == validPassword)
    }
}