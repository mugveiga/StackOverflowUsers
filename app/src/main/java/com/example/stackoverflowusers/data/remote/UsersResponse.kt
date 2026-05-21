package com.example.stackoverflowusers.data.remote

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("items") val items: List<UserDto>,
)
