package com.example.stackoverflowusers.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil3.compose.AsyncImage
import com.example.stackoverflowusers.R
import com.example.stackoverflowusers.data.User

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            MaterialTheme {
                UserListScreen()
            }
        }
    }
}

private val users = listOf(
    User(1, "Name1", 123, null),
    User(2, "Name2", 345, "https://www.gravatar.com/avatar/6d8ebb117e8d83d74ea95fb"),
)

@Composable
private fun UserListScreen() {
    var followed by remember { mutableStateOf(emptySet<Long>()) }
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp)) {
            items(users, key = { it.id }) { user ->
                UserRow(
                    user = user,
                    isFollowed = user.id in followed,
                    onToggleFollow = {
                        followed = if (user.id in followed) {
                            followed.minus(user.id)
                        } else {
                            followed.plus(user.id)
                        }
                    },
                )
                HorizontalDivider()
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
                Text(text = user.name.first().uppercase())
            } else {
                AsyncImage(
                    model = user.profileImage,
                    contentDescription = user.name,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = user.name,
            )
            Text(
                text = stringResource(R.string.reputation, user.reputation),
            )
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
    MaterialTheme {
        UserListScreen()
    }
}
