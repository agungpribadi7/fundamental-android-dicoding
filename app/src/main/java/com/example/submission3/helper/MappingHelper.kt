package com.example.submission3.helper

import android.database.Cursor
import com.example.submission3.User
import com.example.submission3.db.DatabaseContract
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
                val nFollower = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.N_FOLLOWER))
                val nFollowing = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.IS_FAVORITE))
                userList.add(User(name, username, avatar, nFollower, nFollowing, repository, company, location, isFav))
            }
        }
        return userList
    }

    fun mapCursorToObject(cursor: Cursor?): User {
        var user = User()
        cursor?.apply {
            moveToFirst()
            val name = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.NAME))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.AVATAR))
            val company = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.COMPANY))
            val location = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.LOCATION))
            val repository = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.REPOSITORY))
            val isFav = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.IS_FAVORITE))
            val nFollower = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.N_FOLLOWER))
            val nFollowing = getString(getColumnIndexOrThrow(DatabaseContract.FavouriteColumns.IS_FAVORITE))
            user = User(name, username, avatar, nFollower, nFollowing, repository, company, location, isFav)
        }
        return user
    }
}