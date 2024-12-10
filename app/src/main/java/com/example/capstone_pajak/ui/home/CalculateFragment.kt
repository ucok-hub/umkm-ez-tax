package com.example.capstone_pajak.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.capstone_pajak.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class CalculateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calculate, container, false)

        val spinner = view.findViewById<Spinner>(R.id.spinner_calculation_type)
        val incomeInput = view.findViewById<EditText>(R.id.input_income)
        val yearInput = view.findViewById<TextView>(R.id.input_year)
        val normaInput = view.findViewById<EditText>(R.id.input_norma)
        val hppInput = view.findViewById<EditText>(R.id.input_hpp)
        val businessExpenseInput = view.findViewById<EditText>(R.id.input_business_expense)
        val calculateButton = view.findViewById<Button>(R.id.button_calculate)
        val infoButton = view.findViewById<Button>(R.id.button_info)
        val resultText = view.findViewById<TextView>(R.id.text_result)

        // Format input penghasilan dengan pemisah ribuan
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

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> { // Under 2025
                        yearInput.visibility = View.VISIBLE
                        normaInput.visibility = View.GONE
                        hppInput.visibility = View.GONE
                        businessExpenseInput.visibility = View.GONE
                    }
                    1 -> { // 2025 Onwards
                        yearInput.visibility = View.GONE
                        normaInput.visibility = View.VISIBLE
                        hppInput.visibility = View.GONE
                        businessExpenseInput.visibility = View.GONE
                    }
                    2 -> { // Progressive Tax with Bookkeeping
                        yearInput.visibility = View.GONE
                        normaInput.visibility = View.GONE
                        hppInput.visibility = View.VISIBLE
                        businessExpenseInput.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

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
                    1 -> calculateTax2025Onwards(income, norma, resultText)
                    2 -> calculateTaxProgressive(income, hpp, businessExpense, resultText)
                }
            } else {
                Toast.makeText(context, "Harap masukkan penghasilan!", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Info
        infoButton.setOnClickListener {
            val selectedOption = spinner.selectedItemPosition
            showInfoDialog(selectedOption)
        }

        return view
    }

    private fun showYearPickerDialog(yearInput: TextView, calculationType: Int) {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, _, _ -> yearInput.text = year.toString() },
            currentYear, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.calendarViewShown = false
        datePickerDialog.datePicker.spinnersShown = true

        when (calculationType) {
            0 -> datePickerDialog.datePicker.maxDate =
                Calendar.getInstance().apply { set(2024, Calendar.DECEMBER, 31) }.timeInMillis
            1 -> datePickerDialog.datePicker.minDate =
                Calendar.getInstance().apply { set(2025, Calendar.JANUARY, 1) }.timeInMillis
        }

        datePickerDialog.show()
    }

    private fun showInfoDialog(selectedOption: Int) {
        val infoText = when (selectedOption) {
            0 -> "Under 2025: Masukkan jumlah penghasilan dan tahun berusaha untuk perhitungan pajak sebelum 2025."
            1 -> "2025 Onwards: Masukkan penghasilan dan norma untuk menghitung pajak setelah 2025."
            2 -> "Progressive Tax with Bookkeeping: Masukkan penghasilan, HPP, dan biaya usaha untuk perhitungan progresif."
            else -> "Pilih opsi untuk melihat detail informasi."
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Informasi Perhitungan")
            .setMessage(infoText)
            .setPositiveButton("Tutup") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun calculateTaxUnder2025(income: String, year: String, resultText: TextView) {
        val url = "https://umkm-pajak-api-57151910209.asia-southeast2.run.app/calculate-under2025"
        val body = JSONObject()
        body.put("penghasilan", income.toDouble())
        body.put("tahun", year.toInt())
        body.put("golongan", "pribadi")

        makeApiCall(url, body, resultText)
    }

    private fun calculateTax2025Onwards(income: String, norma: String, resultText: TextView) {
        val url = "https://umkm-pajak-api-57151910209.asia-southeast2.run.app/calculate-2025"
        val body = JSONObject()
        body.put("penghasilan", income.toDouble())
        body.put("norma", norma.toInt())
        body.put("golongan", "K/1")

        makeApiCall(url, body, resultText)
    }

    private fun calculateTaxProgressive(income: String, hpp: String, businessExpense: String, resultText: TextView) {
        val url = "https://umkm-pajak-api-57151910209.asia-southeast2.run.app/calculate-pembukuan-progresif"
        val body = JSONObject()
        body.put("penghasilan", income.toDouble())
        body.put("hargaPokok", hpp.toDouble())
        body.put("biayaUsaha", businessExpense.toDouble())
        body.put("golongan", "TK/0")

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
                        resultText.text = "Pajak Yang Harus di Bayar: $formattedTax"
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
