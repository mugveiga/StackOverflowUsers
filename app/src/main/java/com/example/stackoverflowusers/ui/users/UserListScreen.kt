package com.example.stackoverflowusers.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.stackoverflowusers.R
import com.example.stackoverflowusers.data.User

@Composable
fun UserListScreen(viewModel: UserListViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val followed by viewModel.followed.collectAsStateWithLifecycle()
    UserListContent(
        state = state,
        followed = followed,
        onToggleFollow = viewModel::toggleFollow,
        onRetry = viewModel::loadUsers,
    )
}

@Composable
private fun UserListContent(
    state: UsersUiState,
    followed: Set<Long>,
    onToggleFollow: (Long) -> Unit,
    onRetry: () -> Unit,
) {
    Surface(modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()) {
        when (state) {
            UsersUiState.Loading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            is UsersUiState.Error -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message ?: stringResource(R.string.failed_to_retrieve_users))
                    TextButton(onClick = onRetry) { Text(text = stringResource(R.string.retry)) }
                }
            }

            is UsersUiState.Success -> LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
                items(state.users, key = { it.userId }) { user ->
                    UserRow(
                        user = user,
                        isFollowed = user.userId in followed,
                        onToggleFollow = { onToggleFollow(user.userId) },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun UserRow(
    user: User,
    isFollowed: Boolean,
    onToggleFollow: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center,
        ) {
            if (user.profileImage == null) {
                Text(text = user.displayName.first().uppercase())
            } else {
                AsyncImage(
                    model = user.profileImage,
                    contentDescription = user.displayName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = user.displayName)
            Text(text = stringResource(R.string.reputation, user.reputation))
        }
        if (isFollowed) {
            FilledTonalButton(onClick = onToggleFollow) { Text(stringResource(R.string.following)) }
        } else {
            Button(onClick = onToggleFollow) { Text(stringResource(R.string.follow)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UserListScreenPreview() {
    val sample = listOf(
        User(1, "Name1", 123, null),
        User(2, "Name2", 345, "https://www.gravatar.com/avatar/6d8ebb117e8d83d74ea95fb"),
    )
    MaterialTheme {
        UserListContent(
            state = UsersUiState.Success(sample),
            followed = setOf(1L),
            onToggleFollow = {},
            onRetry = {},
        )
    }
}
