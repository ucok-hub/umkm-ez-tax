package com.example.capstone_pajak.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.appcompat.app.AppCompatDelegate
import com.example.capstone_pajak.R
import com.example.capstone_pajak.ui.chat.AIChatFragment
import com.example.capstone_pajak.ui.menu.MenuFragment
import com.example.capstone_pajak.util.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        
        // Check session validity
        SessionManager.checkSession(this)
        
        // Observe login status
        SessionManager.isLoggedIn.observe(this) { isLoggedIn ->
            if (!isLoggedIn) finish()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        
        // Set up Navigation Component with Bottom Navigation
        val navController = findNavController(R.id.fragment_container)
        bottomNavigationView.setupWithNavController(navController)
    }
}
