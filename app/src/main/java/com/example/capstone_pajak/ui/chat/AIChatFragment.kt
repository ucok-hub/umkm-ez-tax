package com.example.capstone_pajak.ui.chat

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.capstone_pajak.R

class AIChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_a_i_chat, container, false)

        // Find TextView references
        val titleLine1 = view.findViewById<TextView>(R.id.tv_title_line1)
        val titleLine2 = view.findViewById<TextView>(R.id.tv_title_line2)

        // Load Bricolage Grotesque Semi Bold font
        val bricolageFont = ResourcesCompat.getFont(requireContext(), R.font.bricolage_grotesque_semi_bold)

        // Apply font to both TextViews
        titleLine1.typeface = bricolageFont
        titleLine2.typeface = bricolageFont

        // Set text and style for the second line
        val spannableText = SpannableString("With SATA AI")

        // Apply black color to "With"
        spannableText.setSpan(
            ForegroundColorSpan(requireContext().getColor(R.color.black)),
            0,
            4, // "With" ends at index 4
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Apply primary blue color to "SATA AI"
        spannableText.setSpan(
            ForegroundColorSpan(requireContext().getColor(R.color.primary)),
            5,
            spannableText.length, // "SATA AI" starts from index 5
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the styled text to the TextView
        titleLine2.text = spannableText

        // Setup START button click
        val startButton: Button = view.findViewById(R.id.btn_start)
        startButton.setOnClickListener {
            try {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ChatRoomFragment())
                    .addToBackStack(null)
                    .commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return view
    }
}
