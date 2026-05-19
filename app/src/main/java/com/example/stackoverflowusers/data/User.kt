package com.example.stackoverflowusers.data

data class User(
    val id: Long,
    val name: String,
    val reputation: Int,
    val profileImage: String?,
)
