package com.myawesomegames.foodscan

// The import for the old FoodItem is no longer needed.

// A simple data class to hold our analysis results
data class HealthResult(
    val score: Int,
    val warnings: List<String>
)

object HealthAnalyzer {

    // Thresholds for warnings
    private const val HIGH_SUGAR_THRESHOLD = 15.0 // grams
    private const val HIGH_SODIUM_THRESHOLD = 500.0 // mg
    private const val HIGH_SAT_FAT_THRESHOLD = 5.0 // grams

    fun analyze(product: AppProduct): HealthResult {
        var score = 100
        val warnings = mutableListOf<String>()

        // Use ?.let to safely handle nullable values
        // CORRECTED: Changed all instances of "foodItem" to "product"
        product.sugar?.let {
            if (it > HIGH_SUGAR_THRESHOLD) {
                warnings.add("High Sugar")
            }
            score -= (it * 2).toInt() // Subtract 2 points per gram of sugar
        }

        product.sodium?.let {
            if (it > HIGH_SODIUM_THRESHOLD) {
                warnings.add("High Sodium")
            }
            score -= (it / 20).toInt() // Subtract 1 point per 20mg of sodium
        }

        product.saturatedFat?.let {
            if (it > HIGH_SAT_FAT_THRESHOLD) {
                warnings.add("High Saturated Fat")
            }
            score -= (it * 5).toInt() // Subtract 5 points per gram of saturated fat
        }

        product.protein?.let {
            score += (it * 2).toInt() // Add 2 points per gram of protein
        }

        // Ensure score is between 0 and 100
        val finalScore = score.coerceIn(0, 100)

        return HealthResult(score = finalScore, warnings = warnings)
    }
}