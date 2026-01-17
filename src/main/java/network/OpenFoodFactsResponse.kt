package com.myawesomegames.foodscan.network

import com.google.gson.annotations.SerializedName

data class OpenFoodFactsResponse(
    val product: OpenFoodFactsProduct?,
    val status: Int
)

// In OpenFoodFactsResponse.kt
data class OpenFoodFactsProduct(
    @SerializedName("product_name")
    val productName: String?,
    @SerializedName("brands")
    val brandName: String?,
    val nutriments: Nutriments,

    // ADD THIS LINE
    @SerializedName("ingredients_text")
    val ingredientsText: String?
)

data class Nutriments(
    @SerializedName("energy-kcal_100g")
    val calories: Double?,
    @SerializedName("fat_100g")
    val fat: Double?,
    @SerializedName("saturated-fat_100g")
    val saturatedFat: Double?,
    @SerializedName("carbohydrates_100g")
    val carbs: Double?,
    @SerializedName("sugars_100g")
    val sugar: Double?,
    @SerializedName("proteins_100g")
    val protein: Double?,
    @SerializedName("sodium_100g")
    val sodium: Double? // This is in grams
)