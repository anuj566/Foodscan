package com.myawesomegames.foodscan.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NutritionixApiService {
    @GET("v2/search/item")
    suspend fun getProductByUpc(@Query("upc") upc: String): Response<NutritionixResponse>
}

// REPLACE EdamamApiService with this
interface OpenFoodFactsApiService {
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): Response<OpenFoodFactsResponse>
}