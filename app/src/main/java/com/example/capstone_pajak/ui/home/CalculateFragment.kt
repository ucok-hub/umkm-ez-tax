package com.example.capstone_pajak.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.capstone_pajak.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class CalculateFragment : Fragment() {

    private lateinit var selectedGolongan: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculate, container, false)
        
        val normaCekLink = view.findViewById<TextView>(R.id.link_check_norma)
        val spinner = view.findViewById<Spinner>(R.id.spinner_calculation_type)
        val incomeInput = view.findViewById<EditText>(R.id.input_income)
        val yearInput = view.findViewById<TextView>(R.id.input_year)
        val normaInput = view.findViewById<EditText>(R.id.input_norma)
        val hppInput = view.findViewById<EditText>(R.id.input_hpp)
        val businessExpenseInput = view.findViewById<EditText>(R.id.input_business_expense)
        val golonganDropdown = view.findViewById<TextView>(R.id.golongan_dropdown)
        val calculateButton = view.findViewById<Button>(R.id.button_calculate)
        val infoButton = view.findViewById<Button>(R.id.button_info)
        val resultText = view.findViewById<TextView>(R.id.text_result)

        // Initialize default golongan
        selectedGolongan = "K/1"
        golonganDropdown.text = selectedGolongan

        // Format income input with thousand separators
        incomeInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                incomeInput.removeTextChangedListener(this)
                val formatted = formatNumber(s.toString())
                incomeInput.setText(formatted)
                incomeInput.setSelection(formatted.length)
                incomeInput.addTextChangedListener(this)
            }
        })

        // Setup DatePickerDialog for year selection
        yearInput.setOnClickListener {
            showYearPickerDialog(yearInput, spinner.selectedItemPosition)
        }

        // Setup golongan dropdown
        golonganDropdown.setOnClickListener {
            showGolonganSelectionDialog(golonganDropdown)
        }

        // Handle spinner selections
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Control visibility of fields based on selection
                when (position) {
                    0 -> { // Under 2025
                        yearInput.visibility = View.VISIBLE
                        normaInput.visibility = View.GONE
                        businessExpenseInput.visibility = View.GONE
                        hppInput.visibility = View.GONE
                        golonganDropdown.visibility = View.GONE
                        normaCekLink.visibility = View.GONE
                    }
                    1 -> { // 2025 Onwards
                        yearInput.visibility = View.GONE
                        normaInput.visibility = View.VISIBLE
                        businessExpenseInput.visibility = View.GONE
                        hppInput.visibility = View.GONE
                        golonganDropdown.visibility = View.VISIBLE
                        normaCekLink.visibility = View.VISIBLE
                    }
                    2 -> { // Progressive Tax with Bookkeeping
                        yearInput.visibility = View.GONE
                        normaInput.visibility = View.GONE
                        businessExpenseInput.visibility = View.VISIBLE
                        hppInput.visibility = View.VISIBLE
                        golonganDropdown.visibility = View.VISIBLE
                        normaCekLink.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Handle calculate button click
        calculateButton.setOnClickListener {
            val income = cleanNumber(incomeInput.text.toString())
            val year = yearInput.text.toString()
            val norma = normaInput.text.toString()
            val hpp = cleanNumber(hppInput.text.toString())
            val businessExpense = cleanNumber(businessExpenseInput.text.toString())
            val calculationType = spinner.selectedItemPosition

            if (income.isNotEmpty()) {
                resultText.visibility = View.VISIBLE
                when (calculationType) {
                    0 -> calculateTaxUnder2025(income, year, resultText)
                    1 -> calculateTax2025Onwards(income, norma, selectedGolongan, resultText)
                    2 -> calculateTaxProgressive(income, hpp, businessExpense, selectedGolongan, resultText)
                }
            } else {
                Toast.makeText(context, "Harap masukkan penghasilan!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle info button click
        infoButton.setOnClickListener {
            showInfoDialog()
        }

        // Add click listener for norma check link
        normaCekLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://datacenter.ortax.org/ortax/norma/norma")
            }
            startActivity(intent)
        }

        return view
    }

    // Show info dialog for user guidance
    private fun showInfoDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Informasi Penggunaan")
        
        // Get the selected calculation type
        val calculationType = view?.findViewById<Spinner>(R.id.spinner_calculation_type)?.selectedItemPosition ?: 0
        
        val message = when (calculationType) {
            0 -> """
                Perhitungan pajak untuk periode sebelum 2025:
                1. Masukkan penghasilan bruto
                2. Pilih tahun perhitungan
                3. Sistem akan menghitung pajak final 0.5%
            """.trimIndent()
            
            1 -> """
                NPPN adalah metode penghitungan penghasilan neto bagi wajib pajak orang pribadi yang menjalankan usaha atau pekerjaan bebas.
                
                Dalam metode ini, penghasilan neto dihitung dengan cara mengalikan persentase norma tertentu terhadap penghasilan bruto yang diperoleh.
                
                Untuk pengecekan lebih lanjut, silakan klik link di bawah ini
            """.trimIndent()
            
            else -> """
                Perhitungan pajak progresif dengan pembukuan:
                1. Masukkan penghasilan bruto
                2. Masukkan HPP
                3. Masukkan biaya usaha
                4. Pilih golongan
                5. Sistem akan menghitung pajak secara progresif
            """.trimIndent()
        }
        
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun showYearPickerDialog(yearInput: TextView, calculationType: Int) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, _, _ ->
                yearInput.text = year.toString()
            },
            currentYear,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Only allow year selection
        datePickerDialog.datePicker.calendarViewShown = false
        datePickerDialog.datePicker.spinnersShown = true

        // Set year limits based on calculation type
        when (calculationType) {
            0 -> datePickerDialog.datePicker.maxDate =
                Calendar.getInstance().apply { set(2024, Calendar.DECEMBER, 31) }.timeInMillis // Under 2025
            1 -> datePickerDialog.datePicker.minDate =
                Calendar.getInstance().apply { set(2025, Calendar.JANUARY, 1) }.timeInMillis // 2025 Onwards
        }

        datePickerDialog.show()
    }

    private fun showGolonganSelectionDialog(golonganDropdown: TextView) {
        val golonganOptions = arrayOf("K/1", "TK/0", "K/0", "TK/1")
        var selectedIndex = golonganOptions.indexOf(selectedGolongan)

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Golongan")
        builder.setSingleChoiceItems(golonganOptions, selectedIndex) { _, which ->
            selectedIndex = which
        }
        builder.setPositiveButton("OK") { _, _ ->
            selectedGolongan = golonganOptions[selectedIndex]
            golonganDropdown.text = selectedGolongan
        }
        builder.setNegativeButton("Batal", null)
        builder.create().show()
    }

    private fun calculateTaxUnder2025(income: String, year: String, resultText: TextView) {
        val url = "https://umkm-pajak-api-57151910209.asia-southeast2.run.app/calculate-under2025"
        val body = JSONObject()
        body.put("penghasilan", income.toDouble())
        body.put("tahun", year.toInt())
        body.put("golongan", "pribadi")

        makeApiCall(url, body, resultText)
    }

    private fun calculateTax2025Onwards(income: String, norma: String, golongan: String, resultText: TextView) {
        val url = "https://umkm-pajak-api-57151910209.asia-southeast2.run.app/calculate-2025"
        val body = JSONObject()
        body.put("penghasilan", income.toDouble())
        body.put("norma", norma.toInt())
        body.put("golongan", golongan)

        makeApiCall(url, body, resultText)
    }

    private fun calculateTaxProgressive(income: String, hpp: String, businessExpense: String, golongan: String, resultText: TextView) {
        val url = "https://umkm-pajak-api-57151910209.asia-southeast2.run.app/calculate-pembukuan-progresif"
        val body = JSONObject()
        body.put("penghasilan", income.toDouble())
        body.put("hargaPokok", hpp.toDouble())
        body.put("biayaUsaha", businessExpense.toDouble())
        body.put("golongan", golongan)

        makeApiCall(url, body, resultText)
    }

    private fun makeApiCall(url: String, body: JSONObject, resultText: TextView) {
        val client = OkHttpClient()
        val requestBody = RequestBody.create(MediaType.parse("application/json"), body.toString())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    resultText.text = "Error: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                activity?.runOnUiThread {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        val jsonResponse = JSONObject(responseBody!!)
                        val taxAmount = jsonResponse.getLong("taxAmount")
                        val formattedTax = "Rp ${NumberFormat.getNumberInstance(Locale("id", "ID")).format(taxAmount)}"
                        resultText.text = "Hasil Pajak: $formattedTax"
                    } else {
                        resultText.text = "Error: ${response.message()}"
                    }
                }
            }
        })
    }

    private fun cleanNumber(number: String): String {
        return number.replace(".", "").replace(",", "")
    }

    private fun formatNumber(input: String): String {
        val cleanInput = cleanNumber(input)
        val formatter = DecimalFormat("#,###")
        return if (cleanInput.isNotEmpty()) formatter.format(cleanInput.toLong()) else ""
    }
}
