package com.example.stackoverflowusers.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.also

@Singleton
class FollowRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    val followedUserIds: Flow<Set<Int>> = dataStore.data.map { preferences ->
        preferences[FOLLOWED_KEY].orEmpty().mapNotNullTo(mutableSetOf()) { it.toIntOrNull() }
    }

    suspend fun toggle(userId: Int) {
        dataStore.updateData {
            it.toMutablePreferences().also { preferences ->
                val current = preferences[FOLLOWED_KEY].orEmpty()
                val id = userId.toString()
                preferences[FOLLOWED_KEY] = if (id in current) {
                    current.minus(id)
                } else {
                    current.plus(id)
                }
            }
        }
    }

    private companion object {
        val FOLLOWED_KEY = stringSetPreferencesKey("followed_user_ids")
    }
}
