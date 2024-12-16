package com.example.capstone_pajak.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.capstone_pajak.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_home.xml
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Referensi komponen di layout
        val logoImage = view.findViewById<ImageView>(R.id.logo_image)
        val welcomeText = view.findViewById<TextView>(R.id.welcome_text)
        val appName = view.findViewById<TextView>(R.id.app_name)
        val infoFeature = view.findViewById<TextView>(R.id.info_feature)
        val featureDescription = view.findViewById<TextView>(R.id.feature_description)
        val startChatButton = view.findViewById<Button>(R.id.start_chat_button)

        // Aksi tombol navigasi
        startChatButton.setOnClickListener {
            // Navigate to AI Chat Fragment
            findNavController().navigate(R.id.action_homeFragment_to_AIChatFragment)
        }

        return view
    }
}
