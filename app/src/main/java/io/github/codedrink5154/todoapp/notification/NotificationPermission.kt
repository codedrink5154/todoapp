package io.github.codedrink5154.todoapp.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat.checkSelfPermission

object NotificationPermission {
    /* 检查是否拥有权限 */
    /* 可以在任意位置调用 */
    fun hasPermission(context: Context): Boolean {
        return checkSelfPermission(
            context,
            "POST_NOTIFICATIONS"
        ) == PackageManager.PERMISSION_GRANTED
    }

    /* 用于申请权限,可以在某个Composable中调用 */
    /* 其返回值表示权限申请是否成功 */
    @Composable
    fun rememberNotificationPermissionLauncher(
        // 回调，默认为空
        onResult: (Boolean) -> Unit = {},
    ): () -> Unit {
        // 定义launcher用于获取权限
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = onResult,
        )

        return {
            launcher.launch(POST_NOTIFICATIONS)
        }
    }
}
