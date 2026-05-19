package com.example.stackoverflowusers.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: StackExchangeApi,
) {
    suspend fun getUsers(): List<User> = api.getUsers().items
}
