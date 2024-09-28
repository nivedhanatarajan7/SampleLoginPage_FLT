package com.example.sampleloginpage_2fa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var username : EditText;
    lateinit var password : EditText;
    lateinit var loginBtn : Button;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.usernamefield);
        password = findViewById(R.id.passwordfield);
        loginBtn = findViewById(R.id.button);

        loginBtn.setOnClickListener {
            val usernameinput = username.text.toString();
            val passwordInput = password.text.toString();

            if(usernameinput.equals("admin") && passwordInput.equals("test")) {
                Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}