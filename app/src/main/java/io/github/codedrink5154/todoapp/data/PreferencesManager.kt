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

/*
`PreferencesManager` 的初始化发生在 **`MainActivity` 第一次访问 `appViewModel` 的时候**。把两段代码连起来看就清楚了。

## 1. 类本身只是模板

```kotlin
class PreferencesManager(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("lastUser", Context.MODE_PRIVATE)
}
```

`class` 定义不会自己执行。只有写出：

```kotlin
PreferencesManager(applicationContext)
```

才会创建一个实例，并把 `applicationContext` 传给构造函数的 `context` 参数。

创建过程中会执行：

```kotlin
private val prefs = context.applicationContext
    .getSharedPreferences("lastUser", Context.MODE_PRIVATE)
```

这一步做了两件事：

- 拿到 `applicationContext`；
- 调用 `getSharedPreferences(...)`，得到 `SharedPreferences` 对象并保存到 `prefs`。

之后 `loadSavedUserName()`、`saveUserName()` 等方法都通过这个 `prefs` 读写。

`MODE_PRIVATE` 表示这个文件只能被本应用访问。

## 2. 在 MainActivity 里怎么被创建

```kotlin
private val appViewModel: AppViewModel by viewModels {
    viewModelFactory {
        initializer {
            AppViewModel(PreferencesManager(applicationContext))
        }
    }
}
```

这行代码可以拆成三层：

- `by viewModels { ... }`：把 `appViewModel` 委托给 Activity 的 `ViewModelStore`，**第一次访问时才创建**，旋转屏幕时复用。
- `viewModelFactory { ... }`：创建一个工厂，告诉系统“怎么构造我的 ViewModel”。
- `initializer { ... }`：真正构造的地方，最后返回 `AppViewModel` 实例。

所以实际调用链是：

```text
MainActivity.onCreate
  → 第一次读取 appViewModel
    → 工厂被调用
      → PreferencesManager(applicationContext)   // 创建存储对象
        → getSharedPreferences(...)              // 打开 prefs 文件
      → AppViewModel(preferencesManager)          // 把 manager 交给 ViewModel
```

## 3. 为什么需要 factory

因为现在的 `AppViewModel` 有构造函数参数：

```kotlin
class AppViewModel(private val preferences: PreferencesManager) : ViewModel()
```

系统默认的工厂只会找**无参构造函数**，所以如果不提供 factory，运行时会报错：

```text
Cannot create an instance of class AppViewModel
```

factory 的作用就是把 `PreferencesManager` 这个依赖传进去。

## 4. 为什么用 applicationContext

`applicationContext` 的生命周期和应用一样长，不会泄漏 Activity。

如果传 `this`（Activity），并且被长期持有，就可能泄漏整个 Activity。`PreferencesManager` 里再调用一次 `context.applicationContext`，是为了即使外面误传了 Activity，也保存应用级 Context。

## 5. 只创建一次

ViewModelProvider 会缓存实例：

- 第一次访问 `appViewModel`：调用 factory，创建 `PreferencesManager` 和 `AppViewModel`；
- 旋转屏幕：复用原来的 ViewModel，不会再调用 factory；
- Activity 真正结束：ViewModel 被清理。

所以 `PreferencesManager` 也跟着 ViewModel 只创建一次。

## 6. 你现在空文件的情况

你现在的代码是：

```kotlin
class PreferencesManager(context: Context) {
}
```

构造函数是空的，`context` 也没被使用，所以看起来“什么都没发生”。加上 `prefs` 属性之后，初始化才有实际动作：

```kotlin
class PreferencesManager(context: Context) {
    private val prefs = context.applicationContext
        .getSharedPreferences("lastUser", Context.MODE_PRIVATE)
}
```

一句话总结：

```text
MainActivity 通过 factory 创建 AppViewModel；
创建 AppViewModel 时，先创建 PreferencesManager(applicationContext)；
PreferencesManager 的构造函数里打开 SharedPreferences 并保存到 prefs；
之后 ViewModel 只调用 manager 的方法，不碰 Context。
```
*/
