package io.github.codedrink5154.todoapp.notification

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import java.util.jar.Manifest

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
    ): () -> Unit {
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { }
        )

        return {
            launcher.launch(POST_NOTIFICATIONS)
        }
    }
}