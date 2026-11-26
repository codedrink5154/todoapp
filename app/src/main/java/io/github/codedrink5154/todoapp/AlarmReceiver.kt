package io.github.codedrink5154.todoapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.codedrink5154.todoapp.notification.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 接收任务Id
        val taskId = intent.getIntExtra("task_id", 1)
        // 接收任务标题
        val taskTitle = intent.getStringExtra("task_title")
        NotificationHelper(context).showNotification(
            id = taskId,
            title = taskTitle ?: "您有一个任务即将到期",
            content = "Ciallo~"
        )
    }
}
