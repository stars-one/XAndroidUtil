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

基于Remix的3.2.0版本

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

## 5.SlantedTextView

一个倾斜的TextView,适用于标签效果,搬运[HeZaiJin/SlantedTextView: Android slanted TextView.](https://github.com/HeZaiJin/SlantedTextView)

预览效果

![](https://img2023.cnblogs.com/blog/1210268/202304/1210268-20230417232729946-1292926138.png)

### xml使用

```
<com.haozhang.lib.SlantedTextView
    android:layout_width="80dp"
    android:layout_height="80dp"
    android:gravity="center"
    app:slantedBackgroundColor="@color/secondary_text"
    app:slantedLength="40dp"
    app:slantedMode="left"
    app:slantedText="IOS"
    app:slantedTextColor="@color/primary"
    app:slantedTextSize="16sp"
    />
```

### Java使用
```
SlantedTextView stv = (SlantedTextView) findViewById(R.id.test);

stv.setText("PHP")
        .setTextColor(Color.WHITE)
        .setSlantedBackgroundColor(Color.BLACK)
        .setTextSize(18)
        .setSlantedLength(50)
        .setMode(SlantedTextView.MODE_LEFT);
```

### 属性说明

![](https://img2023.cnblogs.com/blog/1210268/202304/1210268-20230417232809423-1380328654.png)

### SlantedMode模式可选项
![](https://img2023.cnblogs.com/blog/1210268/202304/1210268-20230417232833635-1688884951.png)

## 6.日志查看功能

移植了[getActivity / Logcat](https://github.com/getActivity/Logcat)开源库的功能,稍微调整了下样式

通过下面的代码跳转到页面,可看到效果

```kotlin
startActivity(LogcatActivity::class.java)
```
## 7.扩展方法

- `Long.toDateString` long类型转时间字符串
- `Long.toUnitString` 字节转为对应的单位
- `Long.fillZero` 前置补0
- `Int.fillZero` 前置补0
- `Date.toDateString` date类型转时间字符串
- `Double.toFix` double保留几位小数
- `String.parseJsonToList` json字符串转List对象
- `String.parseJsonToObject` json字符串转Object对象

## 8.XActivityUtil

- `joinQqGroup()` 跳转qq的加入群的页面方法