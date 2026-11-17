package io.github.codedrink5154.todoapp.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

object BackStack {

    var stack = NavBackStack<NavKey>()

    fun getLength(): Int {
        return stack.count()
    }

    fun getCurrent(): NavKey? = stack.lastOrNull()

    fun push(destination: NavKey) {
        stack.add(destination)
    }

    fun goBack() {
        if (stack.size <= 1) return
        stack.removeLastOrNull()
    }

    fun switchTab(key: NavKey) {
        if (stack.isEmpty()) {
            stack.add(key)
        } else {
            stack[stack.lastIndex] = key
        }
    }
}
