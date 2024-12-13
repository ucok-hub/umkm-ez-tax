package com.example.capstone_pajak.ui.menu

import android.os.Bundle
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.capstone_pajak.R
import com.example.capstone_pajak.ui.login.LoginActivity
import com.example.capstone_pajak.util.DataStoreHelper
import com.example.capstone_pajak.util.FirebaseAuthHelper
import com.example.capstone_pajak.util.SessionManager
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {
    private lateinit var buildInfoButton: TextView
    private lateinit var aboutDevsButton: TextView
    private lateinit var logoutButton: TextView

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
        buildInfoButton = view.findViewById(R.id.buildInfoButton)
        aboutDevsButton = view.findViewById(R.id.aboutDevsButton)
        logoutButton = view.findViewById(R.id.logoutButton)

        setupBuildInfoButton()
        setupAboutDevsButton()
        setupLogoutButton()
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

    private fun openEmail() {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:rafifn.a18@gmail.com")
            }
            startActivity(intent)
        } catch (e: Exception) {
            AlertDialog.Builder(requireContext())
                .setMessage("No email app found.")
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

                Contact: Click here to email us
            """.trimIndent()

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle(R.string.about_developers)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .create()

            dialog.show()

            // Make the message clickable
            dialog.findViewById<TextView>(android.R.id.message)?.apply {
                setOnClickListener {
                    openEmail()
                }
            }
        }
    }

    private fun setupLogoutButton() {
        logoutButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    SessionManager.logout(requireContext())
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
