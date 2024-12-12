package com.example.capstone_pajak.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.capstone_pajak.ui.login.LoginActivity
import com.example.capstone_pajak.util.DataStoreHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.capstone_pajak.R

class SplashActivity : AppCompatActivity() {
    private val splashTime: Long = 3000 // Durasi Splash Screen dalam milidetik (3 detik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(splashTime)
            checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch(Dispatchers.Main) {
            DataStoreHelper.getLoginStatus(this@SplashActivity).collect { isLoggedIn ->
                if (isLoggedIn) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finish()
            }
        }
    }
}
