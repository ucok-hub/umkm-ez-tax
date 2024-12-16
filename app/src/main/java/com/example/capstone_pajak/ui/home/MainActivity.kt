package com.example.capstone_pajak.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.capstone_pajak.R
import com.example.capstone_pajak.ui.chat.AIChatFragment
import com.example.capstone_pajak.ui.menu.MenuFragment
import com.example.capstone_pajak.ui.home.HomeFragment
import com.example.capstone_pajak.ui.home.CalculateFragment
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

        // Set HomeFragment sebagai default fragment saat aplikasi pertama kali dibuka
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // Navigasi antar fragment menggunakan fragment manager manual
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> loadFragment(HomeFragment())
                R.id.AIChatFragment -> loadFragment(AIChatFragment())
                R.id.nav_calculate -> loadFragment(CalculateFragment())
                R.id.nav_menu -> loadFragment(MenuFragment())
                else -> false
            }
            true
        }
    }

    /**
     * Fungsi untuk mengganti fragment di dalam container.
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
