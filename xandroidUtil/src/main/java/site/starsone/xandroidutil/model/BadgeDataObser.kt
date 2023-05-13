package site.starsone.xandroidutil.model

import com.blankj.utilcode.util.ThreadUtils
import com.google.android.material.badge.BadgeDrawable
import kotlin.properties.Delegates

/**
 * 底部导航栏的小红点数据封装观察者数据类
 *
 * 与`com.google.android.material.bottomnavigation.BottomNavigationView`联用
 * @param badgeDrawable 小红点组件
 * @param zeroHide 小红点数量为0是否隐藏,不隐藏则是显示个无数字的小红点
 */
class BadgeObservableData(badgeDrawable: BadgeDrawable,zeroHide:Boolean) {
    var data by Delegates.observable(0) { property, oldValue, newValue ->
        ThreadUtils.runOnUiThread {
            if (newValue <= 0) {
                if (zeroHide) {
                    badgeDrawable.isVisible = false
                } else {
                    badgeDrawable.clearNumber()
                }
            } else {
                badgeDrawable.isVisible = true
                badgeDrawable.number = newValue
            }
        }
    }

    init {
        data = badgeDrawable.number?:0
    }

}

/**
 * 提供个扩展方法,快速将BadgeDrawable转为BadgeObservableData
 * @param zeroHide 小红点数量为0是否隐藏,不隐藏则是显示个无数字的小红点
 */
fun BadgeDrawable.toBadgeObservableData(zeroHide: Boolean=true): BadgeObservableData {
    return BadgeObservableData(this,zeroHide)
}