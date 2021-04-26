package com.example.consumerapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
        var name: String? = "",
        var username: String? = "",
        var img: String? = "",
        var follower:String? = "",
        var following: String? = "",
        var repository:String? = "",
        var company:String? = "",
        var location:String? = "",
        var isFavourite:String? = "") : Parcelable