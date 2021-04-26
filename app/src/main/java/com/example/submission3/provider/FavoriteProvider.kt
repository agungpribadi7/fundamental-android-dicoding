package com.example.submission3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.submission3.db.DatabaseContract.AUTHORITY
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.CONTENT_URI
import com.example.submission3.db.DatabaseContract.FavouriteColumns.Companion.TABLE_NAME
import com.example.submission3.db.FavoriteHelper

class FavoriteProvider : ContentProvider() {
    companion object {

        private const val DATA = 1
        private const val DATA_ID = 2
        private lateinit var favoriteHelper : FavoriteHelper

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, DATA)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", DATA_ID)
        }
    }

    override fun onCreate(): Boolean {

        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()

        return true
    }

    override fun query(uri: Uri, strings: Array<String>?, s: String?, strings1: Array<String>?, s1: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            DATA -> favoriteHelper.queryAll()
            DATA_ID -> favoriteHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }


    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (DATA) {
            sUriMatcher.match(uri) -> favoriteHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        val updated: Int = when (DATA_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.update(uri.lastPathSegment.toString(),contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (DATA_ID) {
            sUriMatcher.match(uri) -> favoriteHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}