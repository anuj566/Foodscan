package com.myawesomegames.foodscan.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class HistoryDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    object HistoryEntry : BaseColumns {
        const val TABLE_NAME = "scan_history"
        const val COLUMN_NAME_BARCODE = "barcode"
        const val COLUMN_NAME_PRODUCT = "productName"
        const val COLUMN_NAME_BRAND = "brandName"
        const val COLUMN_NAME_SCORE = "score"
        const val COLUMN_NAME_TIMESTAMP = "timestamp"
    }

    private val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${HistoryEntry.TABLE_NAME} (" +
                "${HistoryEntry.COLUMN_NAME_BARCODE} TEXT PRIMARY KEY," +
                "${HistoryEntry.COLUMN_NAME_PRODUCT} TEXT," +
                "${HistoryEntry.COLUMN_NAME_BRAND} TEXT," +
                "${HistoryEntry.COLUMN_NAME_SCORE} INTEGER," +
                "${HistoryEntry.COLUMN_NAME_TIMESTAMP} INTEGER)"

    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${HistoryEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This simple policy is to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FoodScan.db"
    }
}