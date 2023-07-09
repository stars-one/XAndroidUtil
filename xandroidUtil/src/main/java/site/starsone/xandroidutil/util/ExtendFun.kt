package site.starsone.xandroidutil.util

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import java.io.File
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


fun Long.toDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return SimpleDateFormat(format).format(Date(this))
}

fun Date.toDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return SimpleDateFormat(format).format(this)
}

/**
 * 将字节(B)转为对应的单位
 */
fun Long.toUnitString(): String {
    val df = DecimalFormat("#.00")
    val bytes = this
    return when {
        bytes < 1024 -> return df.format(bytes.toDouble()) + "B"
        bytes < 1048576 -> df.format(bytes.toDouble() / 1024) + "K"
        bytes < 1073741824 -> df.format(bytes.toDouble() / 1048576) + "Mb"
        else -> df.format(bytes.toDouble() / 1073741824) + "Gb"
    }
}

/**
 * 前置补0操作
 *
 * 例子:
 * - `1.fillZero(3)` //结果为"001"
 * - `112.fillZero(3)` //结果为"112"
 *
 * @param maxLength 最大位数
 */
fun Int.fillZero(maxLength: Int): String {
    return String.format("%0${maxLength}d", this)
}

/**
 * 前置补0操作
 *
 * 例子:
 * - `1L.fillZero(3)` //结果为"001"
 * - `112L.fillZero(3)` //结果为"112"
 *
 * @param maxLength 最大位数
 */
fun Long.fillZero(maxLength: Int): String {
    return String.format("%0${maxLength}d", this)
}

/**
 * Double保留几位小数
 *
 * @param num
 * @return
 */
fun Double.toFix(num: Int = 2): Double {
    val one = this
    val two = BigDecimal(one)
    return two.setScale(num, BigDecimal.ROUND_HALF_UP).toDouble()
}

/**
 * 将json数据转为List<T>
 *
 * @param T 数据类型
 * @return
 */
fun <T> String.parseJsonToList(clazz: Class<T>): List<T> {
    val gson = Gson()
    val type = `$Gson$Types`.newParameterizedTypeWithOwner(null, ArrayList::class.java, clazz)
    val data: List<T> = gson.fromJson(this, type)
    return data
}

/**
 * 将json字符串数据转为某个类
 *
 * @param T
 * @return
 */
inline fun <reified T> String.parseJsonToObject(): T {
    val gson = Gson()
    val result = gson.fromJson(this, T::class.java)
    return result
}

/**
 * 获取文件的mimeType
 */
fun File.getMimeType(): String {
    val file = this
    if (file == null) {
        return ""
    }
    val extension = file.extension
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: ""
}

/**
 * 获取selector某个状态的drawable
 * - selector里定义`androird:state_pressed="true"`,即为`android.R.attr.state_pressed`
 * - selector里定义`androird:state_pressed="false"`,即为`-android.R.attr.state_pressed`
 *
 * @param flag 可选数值如下: 前面加个`-`,标示为状态为false
- [android.R.attr.state_pressed]：按钮被按下时的状态。
- [android.R.attr.state_focused]：视图获取焦点时的状态。
- [android.R.attr.state_selected]：视图被选中时的状态。
- [android.R.attr.state_checked]：用于可选中的视图，表示视图处于选中状态。
- [android.R.attr.state_enabled]：视图可用时的状态。
- [android.R.attr.state_hovered]：视图被悬停时的状态。
- [android.R.attr.state_activated]：用于用作活动项目的视图。
 *
 */
fun StateListDrawable.getXStateDrawable(@AttrRes flag: Int): Drawable {
    val icon = this
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val index = icon.findStateDrawableIndex(intArrayOf(flag))
        icon.getStateDrawable(index)!!
    } else {
        icon.state = IntArray(android.R.attr.state_checked)
        icon.current
    }
}

/**
 * 获取selector某个状态的color
 * - selector里定义`androird:state_pressed="true"`,即为`android.R.attr.state_pressed`
 * - selector里定义`androird:state_pressed="false"`,即为`-android.R.attr.state_pressed`
 *
 * @param flag 可选数值如下: 前面加个`-`,标示为状态为false
- [android.R.attr.state_pressed]：按钮被按下时的状态。
- [android.R.attr.state_focused]：视图获取焦点时的状态。
- [android.R.attr.state_selected]：视图被选中时的状态。
- [android.R.attr.state_checked]：用于可选中的视图，表示视图处于选中状态。
- [android.R.attr.state_enabled]：视图可用时的状态。
- [android.R.attr.state_hovered]：视图被悬停时的状态。
- [android.R.attr.state_activated]：用于用作活动项目的视图。
 *
 */
fun ColorStateList.getColorForState(@AttrRes flag: Int, @ColorInt defaultColor: Int): Int {
    val array = intArrayOf(flag)
    return getColorForState(array, defaultColor)
}