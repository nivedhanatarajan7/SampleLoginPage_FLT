package com.example.sampleloginpage_2fa

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameEditText = findViewById<EditText>(R.id.username)
        val passwordEditText = findViewById<EditText>(R.id.password)
        val pinInstructions = findViewById<TextView>(R.id.pininstructions)
        val pinEditText = findViewById<EditText>(R.id.pin)
        val loginButton = findViewById<Button>(R.id.login_button)

        // Load saved username
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "")
        usernameEditText.setText(savedUsername)

        // Show or hide fields based on saved username
        if (!savedUsername.isNullOrEmpty()) {
            passwordEditText.visibility = View.GONE
            pinEditText.visibility = View.VISIBLE
            pinInstructions.visibility = View.VISIBLE
        } else {
            passwordEditText.visibility = View.VISIBLE
            pinEditText.visibility = View.GONE
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val pin = pinEditText.text.toString()

            if (validateLogin(username, password, pin)) {
                // Save username
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.apply()

                // Proceed with login
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                // Show error message
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLogin(username: String, password: String, pin: String): Boolean {
        // Replace with your actual validation logic
        val validUsername = "user"
        val validPassword = "password"
        val validPin = "123456"

        return (username == validUsername && password == validPassword) || (pin.length == 6 && pin == validPin)
    }
}