package io.github.codedrink5154.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.codedrink5154.todoapp.ui.theme.TodoAppTheme
import io.github.codedrink5154.todoapp.ui.screen.MainScreen

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Activity 完成初始化后再读取 Context 相关的登录状态。
        val lastUser = getSharedPreferences("lastUser", MODE_PRIVATE)
            .getString("lastUser", null)

        setContent {
            TodoAppTheme {
                MainScreen(lastUser)
            }
        }
    }
}
