package com.example.stackoverflowusers.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class FollowRepositoryTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val testDispatcher = StandardTestDispatcher()
    private var scope = CoroutineScope(testDispatcher)

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repo: FollowRepository

    @Before
    fun setUp() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = scope,
            produceFile = { File(tempFolder.root, "follows.preferences_pb") },
        )
        repo = FollowRepository(dataStore)
    }

    @After
    fun tearDown() {
        scope.cancel()
    }

    @Test
    fun `followedUserIds is empty initially`() = runTest(testDispatcher) {
        assertEquals(emptySet<Int>(), repo.followedUserIds.first())
    }

    @Test
    fun `toggle once adds the user id`() = runTest(testDispatcher) {
        repo.toggle(1)
        assertEquals(setOf(1), repo.followedUserIds.first())
    }

    @Test
    fun `toggle twice on the same id remove it`() = runTest(testDispatcher) {
        repo.toggle(1)
        repo.toggle(1)
        assertEquals(emptySet<Int>(), repo.followedUserIds.first())
    }

    @Test
    fun `multiple toggles`() = runTest(testDispatcher) {
        repo.toggle(1)
        repo.toggle(2)
        repo.toggle(3)
        repo.toggle(2)
        assertEquals(setOf(1, 3), repo.followedUserIds.first())
    }
}
