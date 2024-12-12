package com.example.capstone_pajak.utils

import android.content.Context
import android.util.Log
import java.util.Locale

object Preprocessing {

    private val stopWords = setOf(
        "yang", "dan", "di", "ke", "dari", "ini", "itu", "dengan", "untuk",
        "pada", "adalah", "saya", "anda", "kami"
    )

    // Fungsi untuk menghapus akhiran kata
    private fun removeSuffix(word: String): String {
        val suffixes = listOf("nya", "ku", "mu")
        for (suffix in suffixes) {
            if (word.endsWith(suffix)) {
                return word.dropLast(suffix.length)
            }
        }
        return word
    }

    // Fungsi preprocessing utama
    fun preprocessPipeline(text: String): String {
        val words = text.lowercase(Locale.getDefault())
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }

        val processedWords = words.map { word -> removeSuffix(word) }
            .filter { word -> word !in stopWords }

        return processedWords.joinToString(" ")
    }

    // Fungsi untuk load dataset JSON intents
    fun loadDataset(context: Context, jsonFileName: String): Pair<List<String>, List<String>> {
        val assetManager = context.assets
        val patterns = mutableListOf<String>()
        val tags = mutableListOf<String>()

        try {
            val inputStream = assetManager.open(jsonFileName)
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = org.json.JSONObject(jsonText)
            val intents = jsonObject.getJSONArray("intents")

            for (i in 0 until intents.length()) {
                val intent = intents.getJSONObject(i)
                val tag = intent.getString("tag")
                val patternsArray = intent.getJSONArray("patterns")

                for (j in 0 until patternsArray.length()) {
                    val pattern = patternsArray.getString(j)
                    patterns.add(preprocessPipeline(pattern))
                    tags.add(tag)
                }
            }
        } catch (e: Exception) {
            Log.e("Preprocessing", "Error loading dataset: ${e.message}")
        }

        return Pair(patterns, tags)
    }

    // Fungsi untuk encode tags menjadi indeks numerik
    fun encodeTags(tags: List<String>): Map<String, Int> {
        val tagSet = tags.toSet()
        return tagSet.mapIndexed { index, tag -> tag to index }.toMap()
    }
}
