package com.example.stackoverflowusers.ui.users

import com.example.stackoverflowusers.data.User

sealed interface UsersUiState {
    data object Loading : UsersUiState
    data class Success(val users: List<User>) : UsersUiState
    data class Error(val message: String?) : UsersUiState
}
