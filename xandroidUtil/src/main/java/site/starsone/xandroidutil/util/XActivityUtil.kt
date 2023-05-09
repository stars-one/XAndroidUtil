package site.starsone.xandroidutil.util

import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils

object XActivityUtil {
    /**
     * 跳转qq的加入群的页面
     * @param qqGroupNumber QQ群号码
     * @param error 错误提示的逻辑lambda函数
     */
    fun joinQqGroup(qqGroupNumber: String, error: (() -> Unit)? = null) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&uin=$qqGroupNumber&card_type=group&source=qrcode"))
        try {
            ActivityUtils.startActivity(intent)
        } catch (e: Exception) {
            // 如果没有安装 QQ 或版本过低，将抛出 ActivityNotFoundException 异常
            if (error == null) {
                ToastUtils.showShort("请先安装最新版QQ")
            } else {
                error.invoke()
            }
        }
    }
}