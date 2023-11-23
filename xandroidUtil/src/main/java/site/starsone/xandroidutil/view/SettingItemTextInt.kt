package site.starsone.xandroidutil.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialogx.dialogs.InputDialog
import com.kongzue.dialogx.dialogs.MessageDialog
import site.starsone.xandroidutil.R
import site.starsone.xandroidutil.util.GlobalDataConfig

/**
 * 点击会弹出对话框输入数字的设置项
 */
@SuppressLint("CustomViewStyleable")
class SettingItemTextInt(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    var title = ""
    var desc = ""
    var showTip = false

    private var settingValue = ""

    private var ivTip: RemixIconTextView
    private var tvTip: TextView
    private var textViewSetting: TextView

    init {
        val view = View.inflate(context, R.layout.layout_setting_item_text_int, this)

        ivTip = view.findViewById(R.id.ivTip)
        tvTip = view.findViewById(R.id.tvTip)
        textViewSetting = view.findViewById(R.id.tvContent)

        context?.apply {
            val ta = obtainStyledAttributes(attrs, R.styleable.SettingItem)
            title = ta.getString(R.styleable.SettingItem_text) ?: ""
            desc = ta.getString(R.styleable.SettingItem_tip) ?: ""
            showTip = ta.getBoolean(R.styleable.SettingItem_showTip,false)

            refreshData()
            ta.recycle()
        }

    }

    private fun refreshData() {
        ivTip.setOnClickListener {
            MessageDialog.show("提示", desc)
        }
        //是否展示提示
        if (showTip) {
            ivTip.visibility = View.VISIBLE
        } else {
            ivTip.visibility = View.GONE
        }

        tvTip.text = title
        textViewSetting.text = settingValue

        invalidate()
    }

    fun setData(data: SettingItemTextDataInt, lbd: ((String) -> Unit)? = null) {
        //忽略空数据
        if (data.tip.isNotBlank()) {
            this.desc = data.tip
        }
        if (data.title.isNotBlank()) {
            this.title = data.title
        }

        this.showTip = data.showTip

        refreshData()

        ivTip.setOnClickListener {
            MessageDialog.show("提示", data.tip, "确定")
        }

        textViewSetting.text = data.globalData.currentValue.toString()

        lbd?.invoke(textViewSetting.text.toString())
        findViewById<View>(R.id.root).setOnClickListener {
            InputDialog(
                "提示",
                data.dialogTip,
                "确定修改",
                "取消"
            ).setOkButton { baseDialog, v, inputStr ->
                //是否为数字(加个判空)
                if (inputStr.isNotBlank() && TextUtils.isDigitsOnly(inputStr)) {
                    val count = inputStr.toInt()
                    if (count <= 0) {
                        ToastUtils.showShort("修改失败,请输入大于0的数字")
                    } else {
                        //存储数据
                        data.globalData.setValue(count)
                        textViewSetting.text = count.toString()
                    }
                } else {
                    ToastUtils.showShort("修改失败,你输入的不是数字")
                }
                false
            }.show()
        }
    }
}

data class SettingItemTextDataInt(
    val globalData: GlobalDataConfig<Int>,
    val title: String="",
    val tip: String="",
    val dialogTip :String ="",
    val showTip:Boolean = false
)