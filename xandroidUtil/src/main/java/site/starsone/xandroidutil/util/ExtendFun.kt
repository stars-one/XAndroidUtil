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
import kotlin.math.log10
import kotlin.math.pow


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
 * 将字节(B)转为对应的单位(保留2位小数),最大单位为GB
 *
 * - 如果[numUnit]为1,会进行额外的处理(超过100KB,转为MB单位;超过100MB,转为GB单位)
 *
 * @param numUnit 最后输出保留[numUnit]位小数
 */
fun Long.toUnitStringNew(numUnit: Int = 1): String {
    val size = this

    if (size <= 0) {
        return "0 B"
    }

    val units = arrayOf("B", "KB", "MB", "GB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()

    //处理逻辑,如果大于100KB,则转为0.1MB,大于100MB,转为0.1GB
    for (i in 2 downTo 0) {
        if (size > 100 * (1024.0.pow(i))) {
            if (digitGroups >= i) {
                val formattedSize = String.format("%.${numUnit}f", size / 1024.0.pow((i + 1) * 1.0))
                return "$formattedSize ${units[i + 1]}"
            }
        }
    }

    return if (digitGroups == 0) {
        //单位为B,不保留小数点
        String.format("%.0f %s", size.toFloat(), units[digitGroups])
    } else {
        val formattedSize =
            String.format("%.${numUnit}f", size / 1024.0.pow(digitGroups.toDouble()))
        "$formattedSize ${units[digitGroups]}"
    }
}

/**
 * 将字节(B)转为对应的单位Pair(数值和单位分开),最大单位为GB
 *
 * - 如果[numUnit]为1,会进行额外的处理(超过100KB,转为MB单位;超过100MB,转为GB单位)
 *
 * @param numUnit 最后输出保留[numUnit]位小数
 * @return Pair<数值,单位>
 */
fun Long.toUnitStringNewWithUnit(numUnit: Int = 1): Pair<String, String> {
    val size = this

    if (size <= 0) {
        return Pair("0", "B")
    }

    val units = arrayOf("B", "KB", "MB", "GB")
    val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()

    //处理逻辑,如果大于100KB,则转为0.1MB,大于100MB,转为0.1GB
    for (i in 2 downTo 0) {
        if (size > 100 * (1024.0.pow(i))) {
            if (digitGroups >= i) {
                val formattedSize = String.format("%.${numUnit}f", size / 1024.0.pow((i + 1) * 1.0))
                return Pair(formattedSize, units[i + 1])
            }
        }
    }

    return if (digitGroups == 0) {
        //单位为B,不保留小数点
        Pair(String.format("%.0f %s", size.toFloat(), units[digitGroups]), units[digitGroups])
    } else {
        val formattedSize =
            String.format("%.${numUnit}f", size / 1024.0.pow(digitGroups.toDouble()))
        Pair(formattedSize, units[digitGroups])
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