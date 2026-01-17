package com.myawesomegames.foodscan.network

import com.myawesomegames.foodscan.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val nutritionixClient: Retrofit by lazy {
        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-app-id", BuildConfig.NUTRITIONIX_APP_ID)
                .addHeader("x-app-key", BuildConfig.NUTRITIONIX_APP_KEY)
                .build()
            chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl("https://trackapi.nutritionix.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val openFoodFactsClient: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/") // Open Food Facts URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val nutritionixApi: NutritionixApiService by lazy {
        nutritionixClient.create(NutritionixApiService::class.java)
    }

    val openFoodFactsApi: OpenFoodFactsApiService by lazy {
        openFoodFactsClient.create(OpenFoodFactsApiService::class.java)
    }
}