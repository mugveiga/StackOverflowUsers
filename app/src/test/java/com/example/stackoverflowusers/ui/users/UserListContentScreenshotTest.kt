package com.example.stackoverflowusers.ui.users

import androidx.compose.material3.MaterialTheme
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.example.stackoverflowusers.data.User
import org.junit.Rule
import org.junit.Test

class UserListContentScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
    )

    private val sampleUsers = listOf(
        User(id = 1, name = "name1", reputation = 101, profileImage = null),
        User(id = 2, name = "name2", reputation = 100, profileImage = null),
    )

    @Test
    fun loading() {
        paparazzi.snapshot {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Loading,
                    followed = emptySet(),
                    onToggleFollow = {},
                    onRetry = {},
                )
            }
        }
    }

    @Test
    fun error() {
        paparazzi.snapshot {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Error("error"),
                    followed = emptySet(),
                    onToggleFollow = {},
                    onRetry = {},
                )
            }
        }
    }

    @Test
    fun success() {
        paparazzi.snapshot {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Success(sampleUsers),
                    followed = emptySet(),
                    onToggleFollow = {},
                    onRetry = {},
                )
            }
        }
    }

    @Test
    fun successWithFollowedUser() {
        paparazzi.snapshot {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Success(sampleUsers),
                    followed = setOf(1),
                    onToggleFollow = {},
                    onRetry = {},
                )
            }
        }
    }
}
