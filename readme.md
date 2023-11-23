# Android工具库

<meta name="referrer" content="no-referrer">

<img src="https://jitpack.io/v/stars-one/xAndroidUtil.svg" />

封装自己常用的一些Android的组件或工具

## 目录
* [Android工具库](#android工具库)
  * [依赖](#依赖)
  * [全局配置工具类](#全局配置工具类)
    * [1.新建常量池](#1新建常量池)
    * [2.配置配置项](#2配置配置项)
    * [3.使用](#3使用)
  * [2.Remix图标](#2remix图标)
  * [3.工具类方法](#3工具类方法)
  * [4.主题样式](#4主题样式)
  * [5.SlantedTextView](#5slantedtextview)
    * [xml使用](#xml使用)
    * [Java使用](#java使用)
    * [属性说明](#属性说明)
    * [SlantedMode模式可选项](#slantedmode模式可选项)
  * [6.日志查看功能](#6日志查看功能)
  * [7.扩展方法](#7扩展方法)
  * [8.XActivityUtil](#8xactivityutil)
  * [9.FloatingActionBtnMenu 悬浮按钮组菜单](#9floatingactionbtnmenu-悬浮按钮组菜单)
    * [效果](#效果)
    * [使用](#使用)
      * [1.xml中添加组件](#1xml中添加组件)
      * [2.设置按钮组菜单项和点击监听器](#2设置按钮组菜单项和点击监听器)
    * [样式修改](#样式修改)
    * [单独使用](#单独使用)
  * [10.设置项组件](#10设置项组件)
    * [1.SettingItemRadioGroup 单选按钮设置项](#1settingitemradiogroup-单选按钮设置项)
    * [2.SettingItemSwitch 开关设置项](#2settingitemswitch-开关设置项)
  * [11.BadgeObservableData 小红点封装类](#11badgeobservabledata-小红点封装类)
  * [12.侧滑菜单布局SuperSlidingPaneLayout](#12侧滑菜单布局superslidingpanelayout)
  * [13.FlowLayout自定义布局](#13flowlayout自定义布局)
  * [14.SelectImageView组件](#14selectimageview组件)


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
- `showTipDialog` 显示提示框(有个不再提示的选项)

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
- `Long.toUnitStringNew` 字节转为对应的单位
- `Long.toUnitStringNewWithUnit` 字节转为对应的单位,得到Pair类型结果的数值和单位分开
- `Long.fillZero` 前置补0
- `Int.fillZero` 前置补0
- `Date.toDateString` date类型转时间字符串
- `Double.toFix` double保留几位小数
- `String.parseJsonToList` json字符串转List对象
- `String.parseJsonToObject` json字符串转Object对象
- `File.getMimeType` 获取文件的mimeType
- `StateListDrawable.getXStateDrawable` 获取selector中某个状态的drawable
- `ColorStateList.getColorForState` 获取selector中某个状态的color

## 8.XActivityUtil

- `joinQqGroup()` 跳转qq的加入群的页面方法,需要传个qq群号
- `openFile()` 打开文件


## 9.FloatingActionBtnMenu 悬浮按钮组菜单

用了第三方,感觉他们的api使用有些麻烦,自己抽空实现了一个简单的垂直排列的悬浮按钮

### 效果
![](https://img2023.cnblogs.com/blog/1210268/202305/1210268-20230510204145313-924436435.gif)

### 使用
#### 1.xml中添加组件
```xml
<site.starsone.xandroidutil.view.FloatingActionBtnMenu
    android:id="@+id/fabMenu"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:iconBgColor="@color/design_default_color_error"
    app:iconColor="@color/design_default_color_secondary"
/>
```
- `iconBgColor` 主按钮的背景色
- `iconColor` 主按钮的图标色

也可以通过方法`setMainBtnStyle()`修改

#### 2.设置按钮组菜单项和点击监听器

有2个方法来快速构建子项
- `buildItemsByMenuData()` 通过menu菜单资源
- `buildItemsByListData()` 通过list数据

**使用buildItemsByMenuData方法:**

> 需要定义个mymenu.xml的菜单文件

```kotlin
val fabMenu = findViewById<FloatingActionBtnMenu>(R.id.fabMenu)
val list = listOf(
    FloatingActionBtnMenu.MenuItemData("", R.drawable.ic_baseline_adb_24),
    FloatingActionBtnMenu.MenuItemData(
        "sec", R.drawable.ic_baseline_adb_24,
        FloatingActionBtnMenu.MenuItemStyle()
    )
)

fabMenu.buildItemsByMenuData(R.menu.mymenu) {
    //按钮的点击监听器
    when (it) {
        R.id.menuFirst -> {
            ToastUtils.showShort("点击了first")
        }
        R.id.menuSec -> {
            ToastUtils.showShort("点击了sec")
        }
    }
}
```

**使用buildItemsByListData方法:**


```kotlin
val fabMenu = findViewById<FloatingActionBtnMenu>(R.id.fabMenu)
val list = listOf(
    FloatingActionBtnMenu.MenuItemData("", R.drawable.ic_baseline_adb_24),
    FloatingActionBtnMenu.MenuItemData(
        "sec", R.drawable.ic_baseline_adb_24,
        FloatingActionBtnMenu.MenuItemStyle()
    )
)
fabMenu.buildItemsByListData(list) {
    when (it) {
        0 -> ToastUtils.showShort("点击了first")
        1 -> ToastUtils.showShort("点击了sec")
    }
}
```
### 样式修改

样式数据实体类为MenuItemStyle,解释如下:

```kotlin
/**
 * @param textColor 左侧文本颜色
 * @param textBgColor 左侧文本背景色
 * @param iconColor 右侧图标颜色
 * @param iconBgColor 右侧图标背景色
 */
data class MenuItemStyle(
    @ColorInt val textColor: Int = ColorUtils.getColor(R.color.black),
    @ColorInt val textBgColor: Int = ColorUtils.getColor(R.color.white),
    @ColorInt val iconColor: Int = ColorUtils.getColor(R.color.white),
    @ColorInt val iconBgColor: Int = ColorUtils.getColor(R.color.purple_700)
)
```

使用`buildItemsByMenuData()`方法来构建的话,可以使用第二个参数,接收一个List<MenuItemStyle>来设置每个item的样式颜色,**注意要和menu资源文件的item项数要保持一致!**

而使用`buildItemsByListData()`方法来构建的话,FloatingActionBtnMenu.MenuItemData实体类里的第三个参数可附加样式

### 单独使用

考虑到自己APP的需求,有时候可能APP只会有个添加功能,就懒得改回使用FAB了,索性就让这个组件支持单个使用(即点击后不会弹出多个按钮了)

使用的话很简单,设置一下点击事件即可(不创建多个菜单子项就可以了),如下代码所示

```kotlin
val fabMenu = findViewById<FloatingActionBtnMenu>(R.id.fabMenu)
fabMenu.setMainBtnClickListener{
    //点击操作...
}
```

## 10.设置项组件

可自动保存设置项数值的组件,无需关注保存和读取逻辑

> PS: showTip属性是后续新增的,3个都可以使用

### 1.SettingItemRadioGroup 单选按钮设置项

![](https://img2023.cnblogs.com/blog/1210268/202305/1210268-20230512135421564-801072668.png)

支持GlobalData的Int和String类型
```xml
<site.starsone.xandroidutil.view.SettingItemRadioGroup
    android:id="@+id/sirb"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:tip="提示说明"
    app:text="下载模式"/>
```

```kotlin
object MyGlobalData {
    val mode = GlobalDataConfig("mymode", 1)
}

val sItemRb = findViewById<SettingItemRadioGroup>(R.id.sirb)
sItemRb.setData(SettingItemRadioGroupDataInt(listOf(
    Pair(1,"模式1"),
    Pair(2,"模式2"),"下载模式","提示文本"
),MyGlobalData.mode))
```

- `setRbOrientation()` 设置排列方向

### 2.SettingItemSwitch 开关设置项

![](https://img2023.cnblogs.com/blog/1210268/202305/1210268-20230512144010188-1976392548.png)

```xml
<site.starsone.xandroidutil.view.SettingItemSwitch
    android:id="@+id/siSwtich"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:tip="提示说明"
    app:text="测试开关"/>
```

```kotlin
object MyGlobalData {
    val openFlag = GlobalDataConfig("openFlag", true)
}

val siSwtich = findViewById<SettingItemSwitch>(R.id.siSwtich)
siSwtich.setData(SettingItemSwitchData(MyGlobalData.openFlag))
```

### 3.SettingItemTextInt 数字输入设置项

点击整个item会弹出对话框,让用户输入数字

![](https://img2023.cnblogs.com/blog/1210268/202311/1210268-20231124005433468-1905324635.png)

```xml
<site.starsone.xandroidutil.view.SettingItemTextInt
    android:id="@+id/siText"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:tip="提示说明222"
    app:text="测试开关223"
    app:showTip="false"
    />
```

```kotlin
object MyGlobalData {
    val mode = GlobalDataConfig("openFlag", 3)
}

val siText = findViewById<SettingItemTextInt>(R.id.siText)
siText.setData(SettingItemTextDataInt(MyGlobalData.mode,dialogTip = "输入数字,此选项重启才会生效",showTip = true))
```

## 11.BadgeObservableData 小红点封装类

与`com.google.android.material.bottomnavigation.BottomNavigationView`联用,方便更新小红点数据

```kotlin
//从底部导航栏得到小红点组件对象
val badgeDrawable = bottomNav.getOrCreateBadge(R.menu.menuAdd)
//将小红点组件对象转为我们的实体类
val badgeObservableData = badgeDrawable.toBadgeObservableData()

//更新小红点的数量,会自动更新数据,且包含了主线程切换操作
badgeObservableData.data = 20
```

`toBadgeObservableData(zeroHide)` 是一个扩展方法

提供了个`zeroHide`参数,如果为false的话,当小红点上的数值为0,则会有下面的效果(下载中那个菜单);为true的话,则是直接隐藏小红点的显示

![](https://img2023.cnblogs.com/blog/1210268/202305/1210268-20230514011136780-1506912908.png)

## 12.侧滑菜单布局SuperSlidingPaneLayout

复制于[jenly1314/SuperSlidingPaneLayout: SuperSlidingPaneLayout 是基于SlidingPaneLayout扩展修改，新增几种不同的侧滑效果。](https://github.com/jenly1314/SuperSlidingPaneLayout),更新为androidx版本

xml布局:

```xml
<?xml version="1.0" encoding="utf-8"?>
<site.starsone.xandroidutil.view.SuperSlidingPaneLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/sPanelLayout"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    app:mode="default_"
    app:compat_sliding="false"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <!--  注意顺序,一定是目录布局,然后再到内容  -->

    <!--  侧边栏目录布局  -->
    <include layout="@layout/main_drawer" android:layout_width="match_parent" android:layout_height="match_parent"/>

    <!-- 内容布局   -->
    <include layout="@layout/main_content" android:layout_width="match_parent" android:layout_height="match_parent"/>

</site.starsone.xandroidutil.view.SuperSlidingPaneLayout>
```
## 13.FlowLayout自定义布局

参考视频[定制TagFlowLayout实现单选多选效果1_Android多种方式实现流式布局-慕课网](https://www.imooc.com/video/19656)写的一个自定义流式布局

使用的示例xml:
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.example.myapplication.view.FlowLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/flowlayout"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:maxLines="10"
    android:maxRows="11"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".FlowLayoutTestActivity">

    <Button
        android:layout_width="wrap_content"
        android:layout_margin="4dp"
        android:layout_height="wrap_content"
        android:text="helo"/>

</com.example.myapplication.view.FlowLayout>
```

## 14.SelectImageView组件

需求来源: 项目中经常会有个根据点击从而对图片进行切换,比如勾选功能(之前的解决方法就是写个selector,然后更改imageview的isSelected属性来实现)

不过还是觉得每次要创建一个selector文件比较麻烦,于是想通过直接在xml中使用2个图片来优化流程,于是就有了此组件

组件里面已经包含了点击事件,使用的时候不需要再去设置,如果想要设置,也提供了个方法`setOnClick`

```xml
<!--    正常使用,点击切换图标的选中状态    -->
<site.starsone.xandroidutil.view.SelectImageView
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   app:selectImg="@drawable/ic_baseline_adb_24_gray"
   app:normalImg="@drawable/ic_baseline_adb_24_rose"
   />

<!--   选中才展示图标的效果 ,使用透明色  -->
<site.starsone.xandroidutil.view.SelectImageView
   android:layout_marginStart="16dp"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   app:selectImg="#00000000"
   app:normalImg="@drawable/ic_baseline_adb_24_rose"
   />

<!--       着色效果    -->
<site.starsone.xandroidutil.view.SelectImageView
   android:layout_marginStart="16dp"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   app:selectImgTint="#03bbf3"
   app:selectImg="@drawable/ic_baseline_adb_24_gray"
   app:normalImg="@drawable/ic_baseline_adb_24_rose"
   />
```

## 15.通用Recyclerview的item装饰ItemDecoration


### `ItemDecorationVertical`

可实现垂直线性布局中间item的分割间距效果,效果图如下

![](https://img2023.cnblogs.com/blog/1210268/202311/1210268-20231120105600992-1582479830.png)

```kotlin
val itemDero = ItemDecorationVertical(12)
mrecyclerview.addItemDecoration(itemDero)
```

### `ItemDecorationGrid`

可实现网格布局item的分割间距效果,效果图如下

![](https://img2023.cnblogs.com/blog/1210268/202311/1210268-20231120105656057-1153202704.png)

```kotlin
//每行3个
val itemDero = ItemDecorationGrid(3,12) {outRect, space, position ->  }
mrecyclerview.addItemDecoration(itemDero)
```
