package com.example.capstone_pajak.ui.menu

import android.os.Bundle
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.capstone_pajak.R

class MenuFragment : Fragment() {
    private lateinit var darkModeSwitch: SwitchCompat
    private lateinit var buildInfoButton: TextView
    private lateinit var aboutDevsButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        setupViews(view)
        return view
    }

    private fun setupViews(view: View) {
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch)
        buildInfoButton = view.findViewById(R.id.buildInfoButton)
        aboutDevsButton = view.findViewById(R.id.aboutDevsButton)

        setupDarkModeSwitch()
        setupBuildInfoButton()
        setupAboutDevsButton()
    }

    private fun setupDarkModeSwitch() {
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun setupBuildInfoButton() {
        buildInfoButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.build_info)
                .setMessage(getString(R.string.version))
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun setupAboutDevsButton() {
        aboutDevsButton.setOnClickListener {
            val message = """
                Machine Learning (ML)
                M481B4KY4506 – Wisnu Rasyidin Azhari
                M322B4KY3158 – Musliadi
                M211B4KY0922 – Chelseano Putra

                Cloud Computing (CC)
                C128B4KY0819 – Bayu Dwi Setianto
                C128B4KY2905 – Muhammad Ilham Sutama

                Mobile Development (MD)
                A128B4KY3595 – Rafif Nuraydin Adipratama
                A128B4KY4243 – Syah Rafi Elyusufi Abighaly

                ${getString(R.string.contact_email)}
            """.trimIndent()

            AlertDialog.Builder(requireContext())
                .setTitle(R.string.about_developers)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show()
        }
    }
}
