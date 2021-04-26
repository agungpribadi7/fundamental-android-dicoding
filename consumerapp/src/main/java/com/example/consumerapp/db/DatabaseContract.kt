package com.example.consumerapp.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    // Authority yang digunakan
    const val AUTHORITY = "com.example.submission3"
    const val SCHEME = "content"

    class FavouriteColumns : BaseColumns {

        companion object {
            const val TABLE_NAME = "favorite"
            const val _ID = "_id"
            const val USERNAME = "username"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val COMPANY = "company"
            const val LOCATION = "location"
            const val REPOSITORY = "repository"
            const val IS_FAVORITE = "isFav"

            // Base content yang digunakan untuk akses content provider
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }

    }
}
