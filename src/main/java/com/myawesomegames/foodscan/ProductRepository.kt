package com.myawesomegames.foodscan

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.myawesomegames.foodscan.database.HistoryDbHelper
import com.myawesomegames.foodscan.network.FoodItem
import com.myawesomegames.foodscan.network.OpenFoodFactsProduct
import com.myawesomegames.foodscan.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

data class AppProduct(
    val productName: String?,
    val brandName: String?,
    val calories: Double?,
    val fat: Double?,
    val saturatedFat: Double?,
    val carbs: Double?,
    val sugar: Double?,
    val protein: Double?,
    val ingredientsText: String?,
    val sodium: Double? // in mg
) : Serializable

class ProductRepository {

    suspend fun getProduct(barcode: String, context: Context): AppProduct? {
        // --- Try Nutritionix First (Primary API) ---
        try {
            val response = RetrofitClient.nutritionixApi.getProductByUpc(barcode)
            if (response.isSuccessful && response.body()?.foods?.isNotEmpty() == true) {
                val foodItem = response.body()?.foods?.first()!!
                Log.i("ProductRepository", "SUCCESS: Data found in Nutritionix.")
                val appProduct = mapFromNutritionix(foodItem)
                saveProductToHistory(appProduct, barcode, context)
                return appProduct
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "ERROR: Nutritionix API call failed.", e)
        }

        // --- If Nutritionix fails, try Open Food Facts (Fallback API) ---
        Log.w("ProductRepository", "FALLBACK: Trying Open Food Facts API.")
        try {
            val response = RetrofitClient.openFoodFactsApi.getProduct(barcode)
            // THIS IS THE CORRECTED LINE
            if (response.isSuccessful && response.body()?.product?.productName != null) {
                val product = response.body()?.product!!
                Log.i("ProductRepository", "SUCCESS: Data found in Open Food Facts.")
                val appProduct = mapFromOpenFoodFacts(product)
                saveProductToHistory(appProduct, barcode, context)
                return appProduct
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "ERROR: Open Food Facts API call failed.", e)
        }

        Log.e("ProductRepository", "FAILURE: Product not found in any API.")
        return null
    }

    private suspend fun saveProductToHistory(product: AppProduct, barcode: String, context: Context) {
        withContext(Dispatchers.IO) {
            val healthResult = HealthAnalyzer.analyze(product)
            val dbHelper = HistoryDbHelper(context.applicationContext)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(HistoryDbHelper.HistoryEntry.COLUMN_NAME_BARCODE, barcode)
                put(HistoryDbHelper.HistoryEntry.COLUMN_NAME_PRODUCT, product.productName ?: "Unknown")
                put(HistoryDbHelper.HistoryEntry.COLUMN_NAME_BRAND, product.brandName)
                put(HistoryDbHelper.HistoryEntry.COLUMN_NAME_SCORE, healthResult.score)
                put(HistoryDbHelper.HistoryEntry.COLUMN_NAME_TIMESTAMP, System.currentTimeMillis())
            }

            db.insertWithOnConflict(HistoryDbHelper.HistoryEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            db.close()
            Log.i("ProductRepository", "Product ${product.productName} saved to history.")
        }
    }

    private fun mapFromNutritionix(item: FoodItem) = AppProduct(
        productName = item.foodName, brandName = item.brandName, calories = item.calories,
        fat = item.fat, saturatedFat = item.saturatedFat, carbs = item.carbs,
        sugar = item.sugar, protein = item.protein, sodium = item.sodium,
        ingredientsText = null // Nutritionix free tier doesn't provide this
    )

    private fun mapFromOpenFoodFacts(product: OpenFoodFactsProduct): AppProduct {
        val nutriments = product.nutriments
        return AppProduct(
            productName = product.productName,
            brandName = product.brandName,
            calories = nutriments.calories,
            fat = nutriments.fat,
            saturatedFat = nutriments.saturatedFat,
            carbs = nutriments.carbs,
            sugar = nutriments.sugar,
            protein = nutriments.protein,
            sodium = nutriments.sodium?.times(1000), // Convert grams to mg
            ingredientsText = product.ingredientsText // Pass the ingredients
        )
    }
}