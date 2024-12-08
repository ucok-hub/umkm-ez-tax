package com.example.capstone_pajak.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capstone_pajak.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menggunakan layout fragment_home.xml untuk HomeFragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Tombol Start Chat
        val startChatButton = view.findViewById<Button>(R.id.start_chat_button)
        startChatButton.setOnClickListener {
            // Tampilkan pesan atau arahkan ke laman baru
            Toast.makeText(context, "Start Chat Now Button Clicked", Toast.LENGTH_SHORT).show()

            // Tambahkan logika tambahan di sini (contoh: navigasi ke laman ChatFragment)
        }

        return view
    }
}
