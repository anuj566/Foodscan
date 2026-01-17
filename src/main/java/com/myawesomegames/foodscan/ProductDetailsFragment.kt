package com.myawesomegames.foodscan

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.myawesomegames.foodscan.databinding.FragmentProductDetailsBinding
import java.io.Serializable

class ProductDetailsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product: AppProduct? = arguments?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getSerializable(KEY_PRODUCT, AppProduct::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getSerializable(KEY_PRODUCT) as? AppProduct
            }
        }
        val barcode = arguments?.getString(KEY_BARCODE)

        if (product != null) {
            // Product was found, show details
            binding.productDetailsView.isVisible = true
            binding.notFoundView.isVisible = false

            // --- ANALYSIS LOGIC ---
            val healthResult = HealthAnalyzer.analyze(product)
            binding.healthScoreText.text = healthResult.score.toString()
            val scoreColor = when {
                healthResult.score >= 70 -> R.color.health_score_good
                healthResult.score >= 40 -> R.color.health_score_medium
                else -> R.color.health_score_bad
            }
            binding.healthScoreIndicator.setIndicatorColor(ContextCompat.getColor(requireContext(), scoreColor))
            binding.healthScoreText.setTextColor(ContextCompat.getColor(requireContext(), scoreColor))
            val animator = ObjectAnimator.ofInt(binding.healthScoreIndicator, "progress", 0, healthResult.score)
            animator.duration = 1000
            animator.start()
            binding.warningChips.removeAllViews()
            if (healthResult.warnings.isEmpty()) {
                binding.warningChips.isVisible = false
            } else {
                binding.warningChips.isVisible = true
                for (warning in healthResult.warnings) {
                    val chip = Chip(context)
                    chip.text = warning
                    binding.warningChips.addView(chip)
                }
            }
            // --- END OF ANALYSIS LOGIC ---

            // Populate the rest of the details
            binding.productName.text = product.productName ?: "N/A"
            binding.productBrand.text = product.brandName ?: "N/A"
            binding.calories.text = "Calories: ${product.calories ?: "N/A"}"
            binding.carbs.text = "Carbohydrates: ${product.carbs ?: "N/A"}g"
            binding.protein.text = "Protein: ${product.protein ?: "N/A"}g"
            binding.fat.text = "Fat: ${product.fat ?: "N/A"}g"

            // --- NEW INGREDIENT HIGHLIGHTING LOGIC ---
            if (!product.ingredientsText.isNullOrBlank()) {
                binding.ingredientsLabel.isVisible = true
                binding.ingredientsText.isVisible = true
                val keywords = getKeywords()
                val highlightedText = highlightKeywords(product.ingredientsText, keywords)
                binding.ingredientsText.text = highlightedText
            } else {
                binding.ingredientsLabel.isVisible = false
                binding.ingredientsText.isVisible = false
            }
            // --- END OF NEW LOGIC ---

        } else {
            // Product not found logic is unchanged
            binding.productDetailsView.isVisible = false
            binding.notFoundView.isVisible = true
            binding.scannedBarcode.text = "Barcode: $barcode"
        }
    }

    // ADDED THIS FUNCTION to get keywords from SharedPreferences
    private fun getKeywords(): Set<String> {
        val sharedPrefs = requireActivity().getSharedPreferences("FoodScanPrefs", Context.MODE_PRIVATE)
        return sharedPrefs.getStringSet("avoid_keywords", setOf()) ?: setOf()
    }

    // ADDED THIS FUNCTION to handle the highlighting
    private fun highlightKeywords(text: String, keywords: Set<String>): SpannableStringBuilder {
        val spannable = SpannableStringBuilder(text)
        val textToSearch = text.lowercase()

        for (keyword in keywords) {
            if (keyword.isBlank()) continue

            var startIndex = textToSearch.indexOf(keyword.lowercase())
            while (startIndex >= 0) {
                val endIndex = startIndex + keyword.length
                // Apply RED color
                spannable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    startIndex,
                    endIndex,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // Apply BOLD style
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndex,
                    endIndex,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                startIndex = textToSearch.indexOf(keyword.lowercase(), endIndex)
            }
        }
        return spannable
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_PRODUCT = "key_product"
        private const val KEY_BARCODE = "key_barcode"

        fun newInstance(product: AppProduct?, barcode: String): ProductDetailsFragment {
            val args = Bundle()
            args.putSerializable(KEY_PRODUCT, product as Serializable?)
            args.putString(KEY_BARCODE, barcode)
            val fragment = ProductDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}