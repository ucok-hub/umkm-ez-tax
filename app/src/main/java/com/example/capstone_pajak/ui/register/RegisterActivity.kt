package com.example.capstone_pajak.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.capstone_pajak.R
import com.example.capstone_pajak.ui.home.MainActivity
import com.example.capstone_pajak.util.DataStoreHelper
import com.example.capstone_pajak.util.FirebaseAuthHelper
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuthHelper = FirebaseAuthHelper()

        val emailEditText = findViewById<EditText>(R.id.et_register_email)
        val passwordEditText = findViewById<EditText>(R.id.et_register_password)
        val registerButton = findViewById<Button>(R.id.register_button)
        val loginButton = findViewById<Button>(R.id.to_login_button)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateInput(email, password)) {
                lifecycleScope.launch {
                    try {
                        val result = firebaseAuthHelper.registerUser(email, password)
                        result.onSuccess {
                            DataStoreHelper.saveLoginStatus(this@RegisterActivity, true)
                            DataStoreHelper.saveEmail(this@RegisterActivity, email)
                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            finish()
                        }.onFailure {
                            Toast.makeText(this@RegisterActivity, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        loginButton.setOnClickListener {
            finish() // Return to LoginActivity
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
