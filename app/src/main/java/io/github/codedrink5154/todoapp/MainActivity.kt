package io.github.codedrink5154.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.codedrink5154.todoapp.data.PreferencesManager
import io.github.codedrink5154.todoapp.notification.NotificationHelper
import io.github.codedrink5154.todoapp.ui.theme.TodoAppTheme
import io.github.codedrink5154.todoapp.ui.screen.MainScreen
import io.github.codedrink5154.todoapp.viewmodel.AppViewModel

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {

    private val appViewModel: AppViewModel by viewModels {
        viewModelFactory {
            initializer {
                AppViewModel(PreferencesManager(applicationContext))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 创建通知渠道
        NotificationHelper(applicationContext).createChannel()

        enableEdgeToEdge()

        setContent {
            TodoAppTheme {
                MainScreen(appViewModel = appViewModel)
            }
        }
    }
}
