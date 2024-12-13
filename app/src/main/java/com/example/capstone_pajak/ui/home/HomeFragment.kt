package com.example.capstone_pajak.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.navigation.Navigation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstone_pajak.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        // Click Now button - Navigate to Calculate Fragment
        val clickNowButton = view.findViewById<Button>(R.id.click_now_button)
        clickNowButton.setOnClickListener {
            val bottomNavigationView = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigationView?.selectedItemId = R.id.nav_calculate
        }
        // Start Chat Now button - Navigate to AI Chat Fragment
        val startChatButton = view.findViewById<Button>(R.id.start_chat_button)
        startChatButton.setOnClickListener {
            val bottomNavigationView = activity?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigationView?.selectedItemId = R.id.nav_ai_chat
        }

        return view
    }
}
