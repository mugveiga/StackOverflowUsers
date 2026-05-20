package com.example.stackoverflowusers.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stackoverflowusers.data.FollowRepository
import com.example.stackoverflowusers.data.UserRepository
import com.example.stackoverflowusers.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UsersUiState>(UsersUiState.Loading)
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    val followed: StateFlow<Set<Int>> = followRepository.followedUserIds
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptySet(),
        )

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.value = UsersUiState.Loading
            _uiState.value = try {
                UsersUiState.Success(userRepository.getUsers())
            } catch (t: Throwable) {
                UsersUiState.Error(t.message)
            }
        }
    }

    fun toggleFollow(userId: Int) {
        viewModelScope.launch(ioDispatcher) { followRepository.toggle(userId) }
    }
}
