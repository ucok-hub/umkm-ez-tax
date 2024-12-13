package com.example.capstone_pajak.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.capstone_pajak.R
import com.example.capstone_pajak.ui.home.MainActivity
import com.example.capstone_pajak.ui.register.RegisterActivity
import com.example.capstone_pajak.util.DataStoreHelper
import com.example.capstone_pajak.util.FirebaseAuthHelper
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuthHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        firebaseAuthHelper = FirebaseAuthHelper()
        initializeViews()

        // Check if session is valid
        if (firebaseAuthHelper.isSessionValid()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
    }
    
    private fun initializeViews() {
        val emailEditText: EditText = findViewById(R.id.et_email)
        val passwordEditText: EditText = findViewById(R.id.et_password)
        val loginButton = findViewById<Button>(R.id.login_button)
        val registerButton = findViewById<Button>(R.id.register_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val result = firebaseAuthHelper.loginUser(email, password)
                result.onSuccess {
                    DataStoreHelper.saveLoginStatus(this@LoginActivity, true)
                    DataStoreHelper.saveEmail(this@LoginActivity, email)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }.onFailure {
                    Toast.makeText(this@LoginActivity, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
