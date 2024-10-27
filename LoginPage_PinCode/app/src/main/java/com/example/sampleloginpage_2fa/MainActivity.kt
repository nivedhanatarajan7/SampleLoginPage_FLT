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
        val passwordInstruct = findViewById<TextView>(R.id.passInstructions)
        val pinInstructions = findViewById<TextView>(R.id.pininstructions)
        val pinCreateText = findViewById<EditText>(R.id.pinCreate)
        val pinCreateInstructions = findViewById<TextView>(R.id.pincreateinstructions)
        val pinEditText = findViewById<EditText>(R.id.pin)
        val loginButton = findViewById<Button>(R.id.login_button)
        val pinButton = findViewById<Button>(R.id.pin_button)
        val usePassButton = findViewById<Button>(R.id.usePass)
        val usePinButton = findViewById<Button>(R.id.usePin)
        var onPurposePassword = false;

        // Load saved username
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", "")
        usernameEditText.setText(savedUsername)
        var validPin = sharedPreferences.getString("pin", "")

        // Show or hide fields based on saved username
        if (!savedUsername.isNullOrEmpty()) {
            passwordEditText.visibility = View.GONE
            pinEditText.visibility = View.VISIBLE
            pinInstructions.visibility = View.VISIBLE
            usePassButton.visibility = View.VISIBLE
        } else {
            passwordEditText.visibility = View.VISIBLE
            passwordInstruct.visibility = View.VISIBLE
            pinEditText.visibility = View.GONE
        }

        usePassButton.setOnClickListener {
            passwordEditText.visibility = View.VISIBLE
            passwordInstruct.visibility = View.VISIBLE

            pinEditText.visibility = View.GONE
            pinInstructions.visibility = View.GONE
            usePassButton.visibility = View.GONE
            usePinButton.visibility = View.VISIBLE
            onPurposePassword = true
        }

        usePinButton.setOnClickListener {
            passwordEditText.visibility = View.GONE
            passwordInstruct.visibility = View.GONE

            pinEditText.visibility = View.VISIBLE
            pinInstructions.visibility = View.VISIBLE
            usePassButton.visibility = View.VISIBLE
            usePinButton.visibility = View.GONE
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val pin = pinEditText.text.toString()
            var login = false;

            if (validPin?.let { it1 -> validateLogin(username, password, pin, it1) } == true) {
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.apply()

                if (!onPurposePassword && pin == "") {
                    pinCreateText.visibility = View.VISIBLE
                    pinCreateInstructions.visibility = View.VISIBLE
                    pinButton.visibility = View.VISIBLE

                    pinButton.setOnClickListener {
                        validPin = pinCreateText.text.toString()

                        if (validPin!!.length != 6) {
                            Toast.makeText(this, "PIN is not 6 digits long", Toast.LENGTH_SHORT)
                                .show()
                            login = false;
                        } else {
                            editor.putString("pin", validPin)
                            editor.apply()
                            Toast.makeText(this, "PIN has been set", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                // Proceed with login
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLogin(username: String, password: String, pin: String, savedPin: String): Boolean {
        // Replace with your actual validation logic
        val validUsername = "user"
        val validPassword = "password"

        return (username == validUsername && password == validPassword) || (pin.length == 6 && pin == savedPin)
    }
}