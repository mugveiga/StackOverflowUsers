package com.example.stackoverflowusers.data

import com.google.gson.annotations.SerializedName

data class UsersResponse(
    @SerializedName("items") val items: List<User>,
)
