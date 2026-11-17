package io.github.codedrink5154.todoapp.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

object BackStack {

    var stack = NavBackStack<NavKey>()

    fun getCurrent(): NavKey? = stack.lastOrNull()

    fun goBack() {
        if (stack.isNotEmpty()) {
            stack.removeLast()
        }
    }

    fun switchTab(key: NavKey) {
        if (stack.isEmpty()) {
            stack.add(key)
        } else {
            stack[stack.lastIndex] = key
        }
    }
}
