package com.myawesomegames.foodscan

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.myawesomegames.foodscan.databinding.ActivityKeywordsBinding

class KeywordsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeywordsBinding
    private val sharedPrefs by lazy {
        getSharedPreferences("FoodScanPrefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeywordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addKeywordButton.setOnClickListener {
            addKeyword()
        }

        loadAndDisplayKeywords()
    }

    private fun loadAndDisplayKeywords() {
        binding.keywordChipGroup.removeAllViews()
        val keywords = getKeywords()
        for (keyword in keywords) {
            addChipToGroup(keyword)
        }
    }

    private fun addKeyword() {
        val keyword = binding.keywordEditText.text.toString().trim()
        if (keyword.isNotBlank()) {
            val currentKeywords = getKeywords().toMutableSet()
            currentKeywords.add(keyword.lowercase()) // Save as lowercase for easier matching
            saveKeywords(currentKeywords)

            binding.keywordEditText.text.clear()
            loadAndDisplayKeywords()
        }
    }

    private fun removeKeyword(keyword: String) {
        val currentKeywords = getKeywords().toMutableSet()
        currentKeywords.remove(keyword)
        saveKeywords(currentKeywords)
        loadAndDisplayKeywords()
    }

    private fun getKeywords(): Set<String> {
        return sharedPrefs.getStringSet("avoid_keywords", setOf()) ?: setOf()
    }

    private fun saveKeywords(keywords: Set<String>) {
        sharedPrefs.edit().putStringSet("avoid_keywords", keywords).apply()
    }

    private fun addChipToGroup(keyword: String) {
        val chip = Chip(this)
        chip.text = keyword
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            removeKeyword(keyword)
        }
        binding.keywordChipGroup.addView(chip)
    }
}