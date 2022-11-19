package site.starsone.xandroidutil.util

import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.ActivityUtils

object XAndroidMethod {
    /**
     * 使用默认浏览器打开网址
     * @param url 网址
     */
    fun openUrlByDefaultBrower(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            //修改不使用默认浏览器打开
            ActivityUtils.startActivity(intent)
        }
    }

    /**
     * 弹窗选择浏览器打开链接
     * @param url 网址
     */
    fun openUrlByBrower(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            ActivityUtils.startActivity(Intent.createChooser(intent, "选择浏览器打开网址"))
        }
    }

}