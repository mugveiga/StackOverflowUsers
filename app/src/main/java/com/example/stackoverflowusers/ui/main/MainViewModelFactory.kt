package com.example.stackoverflowusers.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stackoverflowusers.data.UserRepository

class MainViewModelFactory(
    private val repository: UserRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == MainViewModel::class.java)
        return MainViewModel(repository) as T
    }
}
