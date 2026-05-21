package com.example.stackoverflowusers.ui.users

import com.example.stackoverflowusers.data.User
import com.example.stackoverflowusers.data.repository.FollowRepository
import com.example.stackoverflowusers.data.repository.UserRepository
import com.example.stackoverflowusers.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verifyBlocking
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val userRepository: UserRepository = mock()
    private val followRepository: FollowRepository = mock()

    private val sampleUser = User(id = 1, name = "name", reputation = 100, profileImage = null)

    @Test
    fun `initial uiState is Loading before load completes`() = runTest(testDispatcher) {
        userRepository.stub { onBlocking { getUsers() } doReturn listOf(sampleUser) }
        whenever(followRepository.followedUserIds).thenReturn(flowOf(emptySet()))

        val vm = buildViewModel()

        assertEquals(UsersUiState.Loading, vm.uiState.value)
    }

    @Test
    fun `successful load transitions uiState to Success`() = runTest(testDispatcher) {
        userRepository.stub { onBlocking { getUsers() } doReturn listOf(sampleUser) }
        whenever(followRepository.followedUserIds).thenReturn(flowOf(emptySet()))

        val vm = buildViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is UsersUiState.Success)
        assertEquals(listOf(sampleUser), (state as UsersUiState.Success).users)
    }

    @Test
    fun `failed load transitions uiState to Error with throw message`() = runTest(testDispatcher) {
        userRepository.stub { onBlocking { getUsers() } doAnswer { throw RuntimeException("error") } }
        whenever(followRepository.followedUserIds).thenReturn(flowOf(emptySet()))

        val vm = buildViewModel()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertTrue(state is UsersUiState.Error)
        assertEquals("error", (state as UsersUiState.Error).message)
    }

    @Test
    fun `toggleFollow delegates to followRepository`() = runTest(testDispatcher) {
        userRepository.stub { onBlocking { getUsers() } doReturn emptyList() }
        whenever(followRepository.followedUserIds).thenReturn(flowOf(emptySet()))

        val vm = buildViewModel()
        vm.toggleFollow(1)
        advanceUntilIdle()

        verifyBlocking(followRepository) { toggle(1) }
    }

    @Test
    fun `followed reflects followRepository values`() = runTest(testDispatcher) {
        userRepository.stub { onBlocking { getUsers() } doReturn emptyList() }
        whenever(followRepository.followedUserIds).thenReturn(flowOf(setOf(1, 2, 3)))

        val vm = buildViewModel()
        advanceUntilIdle()

        assertEquals(setOf(1, 2, 3), vm.followed.value)
    }

    private fun buildViewModel() = UserListViewModel(
        userRepository = userRepository,
        followRepository = followRepository,
        ioDispatcher = testDispatcher,
    )
}
