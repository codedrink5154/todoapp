package io.github.codedrink5154.todoapp.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.codedrink5154.todoapp.MainActivity
import io.github.codedrink5154.todoapp.viewmodel.AuthViewModel
import io.github.codedrink5154.todoapp.navigation.BackStack
import io.github.codedrink5154.todoapp.navigation.TodoList
import io.github.codedrink5154.todoapp.data.PreferencesManager

@Composable
fun AuthScreen(innerPadding: PaddingValues, onLoginSuccess: (String) -> Unit, viewModel: AuthViewModel = viewModel()) {
    val isRegister = viewModel.isRegister.collectAsStateWithLifecycle().value

    val userName = viewModel.name.collectAsStateWithLifecycle().value
    val password = viewModel.password.collectAsStateWithLifecycle().value
    val repeatPassword = viewModel.repeatPassword.collectAsStateWithLifecycle().value

    // 获取context
    val context = LocalContext.current

    Column(
        // 防止输入法挡住输入框
        modifier = Modifier.fillMaxSize().imePadding().padding(innerPadding).padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (!isRegister) {
                        Text(
                            text = "登录",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "登录已有账号",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    } else {
                        Text(
                            text = "注册",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = "创建新账号",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    // 输入用户名
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text("用户名") },
                        placeholder = { Text("输入用户名") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    // 输入密码
                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.updatePassword(it) },
                        label = { Text("密码") },
                        placeholder = { Text("输入密码") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation('*'),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    if (!isRegister) {
                        // 登录按钮
                        Button(
                            onClick = {
                                if (viewModel.tryLogin(userName, password)) {
                                    // 清理密码
                                    viewModel.updatePassword("")
                                    // 同步全局用户信息，让“我的”页面立即显示新用户名
                                    onLoginSuccess(userName)
                                    // 切换至待办页面
                                    BackStack.switchTab(TodoList)
                                    // 自动登录已经在AppViewModel中实现
                                    Toast.makeText(context, "登录成功!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                                    viewModel.updatePassword("")
                                }
                            },
                            enabled = userName.isNotBlank() && password.isNotBlank(),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = "登录",
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "没有账户？你可以",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                modifier =
                                    Modifier.clickable(onClick = { viewModel.switchRegisterState() }),
                                text = "注册",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    } else {
                        // 如果是注册页面的话
                        OutlinedTextField(
                            value = repeatPassword,
                            onValueChange = { viewModel.updateRepeatPassword(it) },
                            label = { Text("重复密码") },
                            placeholder = { Text("请重复密码") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation('*'),
                            modifier = Modifier.fillMaxWidth(),
                        )
                        if (repeatPassword.isNotBlank() && password != repeatPassword) {
                            Text(
                                text = "两次输入的密码不一致",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                        Button(
                            // 在这里切换Activity
                            onClick = {
                                val res = viewModel.registerNewAccount(userName, password)
                                if (res) {
                                    Toast.makeText(context, "注册成功!", Toast.LENGTH_SHORT).show()
                                    viewModel.switchRegisterState()
                                } else {
                                    Toast.makeText(context, "该用户已存在,注册失败!", Toast.LENGTH_SHORT).show()
                                    viewModel.updateName("")
                                    viewModel.updatePassword("")
                                    viewModel.updateRepeatPassword("")

                                }
                            },

                            enabled =
                                userName.isNotBlank() &&
                                        password.isNotBlank() &&
                                        repeatPassword.isNotBlank() &&
                                        password == repeatPassword,
                            modifier = Modifier.fillMaxWidth(),
                        ) { Text("注册") }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "已有账户？",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                modifier =
                                    Modifier.clickable(
                                        onClick = { viewModel.switchRegisterState() }
                                    ),
                                text = "登录",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }
    }
}
