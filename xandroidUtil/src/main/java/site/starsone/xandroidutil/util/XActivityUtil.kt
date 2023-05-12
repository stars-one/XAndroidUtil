package site.starsone.xandroidutil.util

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.UriUtils
import java.io.File

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

    /**
     * 打开文件
     */
    fun openFile(file: File?) {
        file?.let {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            val type = file.getMimeType()

            val uri = UriUtils.file2Uri(file)
            if (type != null) {
                intent.setDataAndType(uri, type)
            } else {
                intent.data = uri
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                ActivityUtils.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                ToastUtils.showShort("抱歉,当前设备未找到能打开pdf文件的APP!")
            }
        }
    }

}