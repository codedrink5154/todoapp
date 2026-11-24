package io.github.codedrink5154.todoapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.github.codedrink5154.todoapp.data.TodoTask
import io.github.codedrink5154.todoapp.data.User
import io.github.codedrink5154.todoapp.navigation.TodoList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Page {
    OVERVIEW,
    DATE,
    TIME,
    REMIND_SET,
}

class TodoViewModel : ViewModel() {
    /* 编辑器是否开启 */
    private val _isAdding = MutableStateFlow(false)
    val isEditorOpen: StateFlow<Boolean> = _isAdding.asStateFlow()

    /* 正在编辑的任务ID */
    private val _id = MutableStateFlow<Long?>(null)
    val id: StateFlow<Long?> = _id.asStateFlow()

    /* 详细编辑 */
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    /* 详细编辑时，正在进行的页面 */
    private val _page = MutableStateFlow(Page.OVERVIEW)
    val page: StateFlow<Page> = _page.asStateFlow()

    /* 切换页面 */
    fun switchPage(value: Page) {
        _page.value = value
    }

    /* 自增ID, 用于标识任务 */
    private var nextId: Long = 1L

    /* 定义任务列表 */
    private val _todoList = mutableStateListOf<TodoTask>()
    val todoList: List<TodoTask> = _todoList

    /* 编辑任务时，任务的标题 */
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    /* 编辑任务时，任务的描述 */
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    /* 编辑任务时，任务的截止时间 */
    private val _deadline = MutableStateFlow<Long?>(null)
    val deadline: StateFlow<Long?> = _deadline.asStateFlow()

    /* 编辑任务时，任务的分类 */
    private val _category = MutableStateFlow<String?>(null)
    val category: StateFlow<String?> = _category.asStateFlow()

    /* 编辑任务时，任务的提醒时间 */
    /* 注意这里使用分钟为单位，在写入数据库, 创建通知前要转换 */
    private val _remind = MutableStateFlow<Int?>(null)
    val remind: StateFlow<Int?> = _remind.asStateFlow()

    /* 创建一个新任务，将其加入到列表中 */
    /* 该函数仅能被任务编辑器调用 */
    fun addNewTask() {
        val title = _title.value.trim()
        if (title.isEmpty()) return

        _todoList.add(
            TodoTask(
                id = nextId++,
                title = title,
                category = _category.value,
                description = _description.value,
                deadline = _deadline.value,
                remind = _remind.value,
                done = false,
                createdAt = System.currentTimeMillis(),
            )
        )

        addTaskDone()
    }

    /* 调整某个任务的完成/未完成状态 */
    fun toggleDone(id: Long) {
        val index = _todoList.indexOfFirst { it.id == id }
        if (index < 0) return

        val task = _todoList[index]
        _todoList[index] = task.copy(done = !task.done)
    }

    /* 重置编辑器的临时状态 */
    private fun clearForm() {
        _page.value = Page.OVERVIEW
        _title.value = ""
        _description.value = ""
        _deadline.value = null
        _category.value = null
        _remind.value = null
    }

    /* 添加任务 */
    fun addTask() {
        clearForm()
        _isAdding.value = true
    }

    fun addTaskDone() {
        clearForm()
        _isAdding.value = false
    }

    fun editTask(id: Long) {
        _id.value = id // 更新id
        clearForm()
        val index = _todoList.indexOfFirst { it.id == id } // 取得制定任务的index
        if (index == -1) return
        _title.value = _todoList[index].title
        _description.value = _todoList[index].description
        _category.value = _todoList[index].category
        _deadline.value = _todoList[index].deadline
        _remind.value = _todoList[index].remind
        openEditor()
    }

    fun syncToList() {
        val index = _todoList.indexOfFirst { it.id == _id.value }
        val draftTitle = _title.value.trim()
        if (index < 0) return
        val oldTitle = _todoList[index].title
        _todoList[index] = _todoList[index].copy(
            title = if (draftTitle.isBlank()) oldTitle else _title.value,
            description = _description.value,
            category = _category.value,
            deadline = _deadline.value,
            remind = _remind.value,
        )
    }

    /* 编辑任务 */
    fun openEditor() {
        _isEditing.value = true
    }

    fun closeEditor() {
        syncToList()
        clearForm()
        _isEditing.value = false
    }

    /* 移除某个任务 */
    fun removeTask(
        id: Long
    ) {
        _todoList.removeAll { it.id == id }
    }

    /* 更新编辑器状态 */
    fun updateTitle(value: String) {
        _title.value = value
    }

    fun updateDescription(value: String) {
        _description.value = value
    }

    fun updateDeadline(value: Long?) {
        _deadline.value = value
    }

    fun updateCategory(value: String?) {
        _category.value = value
    }

    fun updateRemind(value: Int?) {
        _remind.value = value
    }
}
