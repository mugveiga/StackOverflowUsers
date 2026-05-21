package com.example.stackoverflowusers.data.remote

import com.example.stackoverflowusers.data.User
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("display_name") val displayName: String,
    val reputation: Int,
    @SerializedName("profile_image") val profileImage: String?,
)

fun UserDto.toUser(): User = User(
    id = userId,
    name = displayName,
    reputation = reputation,
    profileImage = profileImage,
)
