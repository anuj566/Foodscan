package com.myawesomegames.foodscan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.myawesomegames.foodscan.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.myawesomegames.foodscan.database.HistoryDbHelper
import com.myawesomegames.foodscan.database.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val historyAdapter = HistoryAdapter()
        binding.historyRecyclerView.adapter = historyAdapter

        // Use a coroutine to read data from the database in the background
        lifecycleScope.launch {
            val items = loadHistoryItems()
            historyAdapter.submitList(items)
        }
    }

    // Add this new function to HistoryActivity
    private suspend fun loadHistoryItems(): List<HistoryItem> = withContext(Dispatchers.IO) {
        val dbHelper = HistoryDbHelper(applicationContext)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            HistoryDbHelper.HistoryEntry.COLUMN_NAME_BARCODE,
            HistoryDbHelper.HistoryEntry.COLUMN_NAME_PRODUCT,
            HistoryDbHelper.HistoryEntry.COLUMN_NAME_BRAND,
            HistoryDbHelper.HistoryEntry.COLUMN_NAME_SCORE,
            HistoryDbHelper.HistoryEntry.COLUMN_NAME_TIMESTAMP
        )

        val sortOrder = "${HistoryDbHelper.HistoryEntry.COLUMN_NAME_TIMESTAMP} DESC"

        val cursor = db.query(
            HistoryDbHelper.HistoryEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        val historyList = mutableListOf<HistoryItem>()
        with(cursor) {
            while (moveToNext()) {
                val item = HistoryItem(
                    barcode = getString(getColumnIndexOrThrow(HistoryDbHelper.HistoryEntry.COLUMN_NAME_BARCODE)),
                    productName = getString(getColumnIndexOrThrow(HistoryDbHelper.HistoryEntry.COLUMN_NAME_PRODUCT)),
                    brandName = getString(getColumnIndexOrThrow(HistoryDbHelper.HistoryEntry.COLUMN_NAME_BRAND)),
                    score = getInt(getColumnIndexOrThrow(HistoryDbHelper.HistoryEntry.COLUMN_NAME_SCORE)),
                    timestamp = getLong(getColumnIndexOrThrow(HistoryDbHelper.HistoryEntry.COLUMN_NAME_TIMESTAMP))
                )
                historyList.add(item)
            }
        }
        cursor.close()
        db.close()
        return@withContext historyList
    }
}