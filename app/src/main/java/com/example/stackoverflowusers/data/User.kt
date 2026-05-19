package com.example.stackoverflowusers.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("display_name") val displayName: String,
    val reputation: Int,
    @SerializedName("profile_image") val profileImage: String?,
)
