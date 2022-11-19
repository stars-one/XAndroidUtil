package site.starsone.xandroidutil.util

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils

class GlobalDataConfig<T>(
    val key: String,
    val defaultValue: T,
    var currentValue: T = defaultValue
) {
    init {
        when {
            defaultValue is Boolean -> {
                val item = this as GlobalDataConfig<Boolean>
                item.setValue(SPUtils.getInstance().getBoolean(key, defaultValue))
            }
            defaultValue is String -> {
                val item = this as GlobalDataConfig<String>
                item.setValue(SPUtils.getInstance().getString(key, defaultValue))
            }
            defaultValue is Int -> {
                val item = this as GlobalDataConfig<Int>
                item.setValue(SPUtils.getInstance().getInt(key, defaultValue))
            }
            defaultValue is Double -> {
                //SPUtils里面的似乎没有提供获取Double方法...
            }
            else -> LogUtils.e("不支持的数据类型!!目前只支持string,boolean,int,double四种类型")
        }
    }

    /**
     * 重置当前值为默认值
     */
    fun resetValue() {
        setValue(defaultValue)
    }

    /**
     * 更改数值
     */
    fun setValue(value: T) {
        //更新内存的
        currentValue = value

        //更新本地存储的数据
        updateLocalStorage(value)
    }

    /**
     * 更新本地存储
     */
    private fun updateLocalStorage(value: T) {
        if (value is Boolean) {
            SPUtils.getInstance().put(key, value)
        }
        if (value is Float) {
            SPUtils.getInstance().put(key, value)
        }
        if (value is String) {
            SPUtils.getInstance().put(key, value)
        }
        if (value is Int) {
            SPUtils.getInstance().put(key, value)
        }
        if (value is Long) {
            SPUtils.getInstance().put(key, value)
        }
    }
}