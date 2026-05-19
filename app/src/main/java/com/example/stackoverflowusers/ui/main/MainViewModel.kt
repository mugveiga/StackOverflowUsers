package com.example.stackoverflowusers.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stackoverflowusers.data.User
import com.example.stackoverflowusers.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface UsersUiState {
    data object Loading : UsersUiState
    data class Success(val users: List<User>) : UsersUiState
    data class Error(val message: String?) : UsersUiState
}

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _state = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val state: StateFlow<UsersUiState> = _state.asStateFlow()

    private val _followed = MutableStateFlow<Set<Long>>(emptySet())
    val followed: StateFlow<Set<Long>> = _followed.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.value = UsersUiState.Loading
            _state.value = try {
                UsersUiState.Success(repository.getUsers())
            } catch (t: Throwable) {
                UsersUiState.Error(t.message)
            }
        }
    }

    fun toggleFollow(userId: Long) {
        _followed.update { current ->
            if (userId in current) current - userId else current + userId
        }
    }
}
