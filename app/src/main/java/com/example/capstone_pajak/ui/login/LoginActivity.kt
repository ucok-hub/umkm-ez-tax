package com.example.capstone_pajak.ui.login

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
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // Akun Dummy
    private val dummyEmail = "admin@example.com"
    private val dummyPassword = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.et_email)
        val passwordEditText = findViewById<EditText>(R.id.et_password)
        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateLogin(email, password)) {
                lifecycleScope.launch {
                    // Simpan status login ke DataStore
                    DataStoreHelper.saveLoginStatus(this@LoginActivity, true)

                    // Pindah ke MainActivity
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Validasi login dengan akun dummy
    private fun validateLogin(email: String, password: String): Boolean {
        return email == dummyEmail && password == dummyPassword
    }
}
