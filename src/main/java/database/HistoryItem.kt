package com.myawesomegames.foodscan.database

data class HistoryItem(
    val barcode: String,
    val productName: String,
    val brandName: String?,
    val score: Int,
    val timestamp: Long
)