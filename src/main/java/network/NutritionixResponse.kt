package com.myawesomegames.foodscan.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NutritionixResponse(
    @SerializedName("foods")
    val foods: List<FoodItem>?
)

data class FoodItem(
    @SerializedName("food_name")
    val foodName: String?,

    @SerializedName("brand_name")
    val brandName: String?,

    @SerializedName("nf_calories")
    val calories: Double?,

    @SerializedName("nf_total_fat")
    val fat: Double?,

    // ADD THIS FIELD
    @SerializedName("nf_saturated_fat")
    val saturatedFat: Double?,

    @SerializedName("nf_total_carbohydrate")
    val carbs: Double?,

    // ADD THIS FIELD
    @SerializedName("nf_sugars")
    val sugar: Double?,

    @SerializedName("nf_protein")
    val protein: Double?,

    // ADD THIS FIELD
    @SerializedName("nf_sodium")
    val sodium: Double?
) : Serializable