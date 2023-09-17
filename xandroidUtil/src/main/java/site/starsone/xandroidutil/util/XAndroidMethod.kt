package site.starsone.xandroidutil.util

import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.kongzue.dialogx.dialogs.MessageDialog

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

    /**
     * 展示提示对话框(可选择不再提示)
     * @param key 唯一标识(不再提示的标识key,存储在sp中)
     * @param message 信息内容
     * @param title 标题
     * @param okAction 点击确定后的回调
     * @param cancelAction 点击不再提示的回调
     *
     */
    fun showTipDialog(
        key: String,
        message: String,
        title: String = "提示",
        okAction: (() -> Unit)? = null,
        cancelAction: (() -> Unit)? = null
    ) {
        val instance = SPUtils.getInstance()

        if (instance.getBoolean(key, true)) {

            MessageDialog(title, message, "确定", "不再提示")
                .setCancelButton { baseDialog, v ->
                    instance.put(key, false)
                    cancelAction?.invoke()
                    false
                }
                .setOkButton { dialog, v ->
                    okAction?.invoke()
                    false
                }
                .show()
        }
    }

}