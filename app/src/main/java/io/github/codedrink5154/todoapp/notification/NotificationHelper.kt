package io.github.codedrink5154.todoapp.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.github.codedrink5154.todoapp.R

class NotificationHelper(
    private val context: Context
) {
    /* 创建通知管理器 */
    private val notificationManager = NotificationManagerCompat.from(context)

    /* 定义CHANNEL_ID */
    companion object {
        const val CHANNEL_ID = "reminder"
    }

    /* 创建并注册通知渠道 */
    fun createChannel() {
        // 先检查API等级
        val channel = NotificationChannel(
            CHANNEL_ID,
            "任务提醒",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = context.getSystemService(
            NotificationManager::class.java
        )
        manager.createNotificationChannel(channel)
    }

    /* 直接显示通知 */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(
        id: Int,
        title: String,
        content: String,
    ) {
        val notification = NotificationCompat.Builder(
            context, CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // 使用上方定义的notificationManager
        notificationManager.notify(id, notification)
    }
}
