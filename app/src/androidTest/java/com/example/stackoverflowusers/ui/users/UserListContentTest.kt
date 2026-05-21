package com.example.stackoverflowusers.ui.users

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.stackoverflowusers.data.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserListContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsProgressIndicator() {
        composeTestRule.setContent {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Loading,
                    followed = emptySet(),
                    onToggleFollow = {},
                    onRetry = {},
                )
            }
        }

        composeTestRule
            .onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertIsDisplayed()
    }

    @Test
    fun errorState_showsMessageAndRetryButton() {
        var retried = false
        composeTestRule.setContent {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Error("error"),
                    followed = emptySet(),
                    onToggleFollow = {},
                    onRetry = { retried = true },
                )
            }
        }

        composeTestRule.onNodeWithText("error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue(retried)
    }

    @Test
    fun successState_showUserInfo() {
        composeTestRule.setContent {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Success(listOf(User(1, "name", 100, null))),
                    followed = emptySet(),
                    onToggleFollow = {},
                    onRetry = {},
                )
            }
        }

        composeTestRule.onNodeWithText("name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reputation: 100").assertIsDisplayed()
    }

    @Test
    fun followButtonClick_invokesCallbackWithId() {
        var clickedId: Int? = null
        composeTestRule.setContent {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Success(listOf(User(1, "name", 100, null))),
                    followed = emptySet(),
                    onToggleFollow = { clickedId = it },
                    onRetry = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Follow").performClick()
        assertEquals(1, clickedId)
    }

    @Test
    fun unfollowButtonVisible_whenUserIsFollowed() {
        composeTestRule.setContent {
            MaterialTheme {
                UserListContent(
                    uiState = UsersUiState.Success(listOf(User(1, "name", 100, null))),
                    followed = setOf(1),
                    onToggleFollow = {},
                    onRetry = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Unfollow").assertIsDisplayed()
    }
}
