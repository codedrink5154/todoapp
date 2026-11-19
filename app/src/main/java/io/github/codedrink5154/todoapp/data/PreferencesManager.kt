package io.github.codedrink5154.todoapp.data

import android.content.Context

class PreferencesManager(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("lastUser", Context.MODE_PRIVATE)

    /* 读取保存的用户名 */
    fun loadSavedUserName(): String? {
        return prefs.getString("lastUser", null)
        // 可能有三种返回值： 正常的字符串，空字符串，空值
    }

    /* 将用户名保存到本地文件中 */
    fun saveUserName(userName: String?) {
        prefs.edit().putString("lastUser", userName ?: "").apply()
    }

    /* 移除登录凭据 */
    fun clearSavedUser() {
        prefs.edit().remove("lastUser").apply()
    }
}
