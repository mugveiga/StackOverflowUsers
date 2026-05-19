package com.example.stackoverflowusers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stackoverflowusers.data.ServiceLocator
import com.example.stackoverflowusers.ui.main.MainViewModel
import com.example.stackoverflowusers.ui.main.MainViewModelFactory
import com.example.stackoverflowusers.ui.main.UserListScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(ServiceLocator.userRepository),
                )
                UserListScreen(viewModel)
            }
        }
    }
}
