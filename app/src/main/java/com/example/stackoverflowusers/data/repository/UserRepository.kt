package com.example.stackoverflowusers.data.repository

import com.example.stackoverflowusers.data.User
import com.example.stackoverflowusers.data.remote.StackExchangeApi
import com.example.stackoverflowusers.data.remote.toUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: StackExchangeApi,
) {
    suspend fun getUsers(): List<User> = api.getUsers().items.map { it.toUser() }
}
