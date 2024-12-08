package com.example.capstone_pajak.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone_pajak.R
import com.example.capstone_pajak.ui.chat.AIChatFragment
import com.example.capstone_pajak.ui.menu.MenuFragment
import com.example.capstone_pajak.ui.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set HomeFragment sebagai default fragment saat aplikasi pertama kali dibuka
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // Navigasi antar fragment
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(HomeFragment())
                R.id.nav_ai_chat -> loadFragment(AIChatFragment())
                R.id.nav_calculate -> loadFragment(CalculateFragment())
                R.id.nav_menu -> loadFragment(MenuFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
