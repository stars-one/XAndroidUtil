# Android工具库

封装自己常用的一些Android的组件或工具

## 全局配置工具类

使用此工具类可以不用关注配置项的保存或进入APP的初始化读取配置

### 1.新建常量池
```kotlin

class Constants {
    companion object {
        val SP_USER_NAME = "username"
    }
}
```
### 2.配置配置项
```kotlin
class GlobalData {
    companion object {
        //设置默认值
        val userName = GlobalDataConfig(Constants.SP_USER_NAME, "")
    }
}
```
### 3.使用

在需要用的地方使用下面取值
```kotlin
val value = GlobalData.userName.currentValue()
```

数值变更调用此方法:

```kotlin
val value = GlobalData.userName.setValue("myname")
```


## 2.Remix图标
将[Remix Icon图标](https://remixicon.com/)封装在Android中,方便快速调用

基于Remix的2.5.0版本

在xml使用

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <site.starsone.xandroidutil.view.RemixIconTextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:iconName="home-5-fill" />

</LinearLayout>
```

代码中使用:
```kotlin
val tvHome = findViewById<RemixIconTextView>(R.id.tvHome)
tvHome.iconName = "home-5-fill"
```

