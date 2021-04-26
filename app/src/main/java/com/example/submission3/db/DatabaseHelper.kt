package com.example.submission3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.FavouriteColumns.TABLE_NAME}")
        onCreate(db)
    }

    companion object {

        private const val DATABASE_NAME = "submission_dicoding3"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_NOTE = "CREATE TABLE ${DatabaseContract.FavouriteColumns.TABLE_NAME}" +
                " (${DatabaseContract.FavouriteColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.FavouriteColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.FavouriteColumns.USERNAME} TEXT NOT NULL," +
                " ${DatabaseContract.FavouriteColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContract.FavouriteColumns.COMPANY} TEXT NOT NULL," +
                " ${DatabaseContract.FavouriteColumns.LOCATION} TEXT NOT NULL," +
                " ${DatabaseContract.FavouriteColumns.REPOSITORY} TEXT NOT NULL," +
                " ${DatabaseContract.FavouriteColumns.IS_FAVORITE} TEXT NOT NULL," +
                " ${DatabaseContract.FavouriteColumns.N_FOLLOWER} TEXT NOT NULL," +
                " ${DatabaseContract.FavouriteColumns.N_FOLLOWING} TEXT NOT NULL)"
    }
}
