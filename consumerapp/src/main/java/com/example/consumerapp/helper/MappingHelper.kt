package com.example.consumerapp.helper

import android.database.Cursor
import com.example.consumerapp.User
import com.example.consumerapp.db.DatabaseContract
import java.util.ArrayList

class MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User> {
        val userList = ArrayList<User>()

        cursor?.apply {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.NAME))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.AVATAR))
                val company = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.COMPANY))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.LOCATION))
                val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.REPOSITORY))
                val isFav = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.IS_FAVORITE))
                userList.add(User(name, username, avatar, "0", "0", repository, company, location, isFav))
            }
        }
        return userList
    }

}