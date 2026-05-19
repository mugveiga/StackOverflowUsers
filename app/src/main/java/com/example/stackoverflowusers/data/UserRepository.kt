package com.example.stackoverflowusers.data

class UserRepository(private val api: StackExchangeApi) {

    suspend fun getUsers(): List<User> = api.getUsers().items
}
