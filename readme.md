# Android工具库

<meta name="referrer" content="no-referrer">

<img src="https://jitpack.io/v/stars-one/xAndroidUtil.svg" />

封装自己常用的一些Android的组件或工具

## 依赖

需要先引用JitPack仓库源

```
<dependency>
    <groupId>com.github.stars-one</groupId>
    <artifactId>xAndroidUtil</artifactId>
    <version>0.1</version>
</dependency>


implementation 'com.github.stars-one:xAndroidUtil:0.1'
```
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

新增监听回调功能,用法如下:
```kotlin
GlobalData.userName.addCallBack{
    //里面你的逻辑,如
    val valueResult = GlobalData.userName.currentValue
}
```

**当配置的数据发生变更,都会执行一次回调**

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

## 3.工具类方法

> PS: 位于`XAndroidMethod`类中

- `openUrlByDefaultBrower` 使用默认浏览器打开网址
- `openUrlByBrower` 弹窗选择浏览器打开链接

## 4.主题样式

- `ripple_common_bg.xml` 水波纹背景