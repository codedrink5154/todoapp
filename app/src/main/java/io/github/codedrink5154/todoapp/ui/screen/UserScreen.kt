package io.github.codedrink5154.todoapp.ui.screen

import android.widget.Space
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import io.github.codedrink5154.todoapp.navigation.BackStack
import io.github.codedrink5154.todoapp.navigation.UserModify
import io.github.codedrink5154.todoapp.navigation.Auth


@Composable
fun UserScreen(innerPadding: PaddingValues, onLogout: () -> Unit, getUserName: () -> String) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserInfoCard(getUserName())
        ButtonField(
            title = "修改资料",
            onClick = { BackStack.push(UserModify) }
        )
        ButtonField(
            title = "退出登录",
            onClick = {
                onLogout()
                // 向lastUser.xml写入空值，取消自动登录
                val sp = context.getSharedPreferences("lastUser", Context.MODE_PRIVATE)
                with(sp.edit()) {
                    putString("lastUser", "")
                    apply()
                }
                BackStack.switchTab(Auth)
            }
        )
    }
}

@Composable
fun ButtonField(
    title: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentSize().clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun UserInfoCard(userName: String) {
    OutlinedCard(
        modifier = Modifier.wrapContentSize().fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))
            Icon(Icons.Default.AccountCircle, "头像", modifier = Modifier.size(64.dp))
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "今天又是自律的一天！",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}
