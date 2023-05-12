package site.starsone.xandroidutil.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import com.kongzue.dialogx.dialogs.MessageDialog
import site.starsone.xandroidutil.R
import site.starsone.xandroidutil.util.GlobalDataConfig


@SuppressLint("CustomViewStyleable")
class SettingItemSwitch(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    var title = ""
    var desc = ""
    private var open = false

    private var ivTip: RemixIconTextView
    private var tvTip: TextView
    private var switchSetting: Switch

    init {
        val view = View.inflate(context, R.layout.layout_setting_item_swtich, this)

        ivTip = view.findViewById(R.id.ivTip)
        tvTip = view.findViewById(R.id.tvTip)
        switchSetting = view.findViewById(R.id.switchSetting)

        context?.apply {
            val ta = obtainStyledAttributes(attrs, R.styleable.SettingItem)
            title = ta.getString(R.styleable.SettingItem_text) ?: ""
            desc = ta.getString(R.styleable.SettingItem_tip) ?: ""

            refreshData()
            ta.recycle()
        }

    }

    private fun refreshData() {
        ivTip.setOnClickListener {
            MessageDialog.show("提示", desc)
        }
        tvTip.text = title
        switchSetting.isChecked = open
        invalidate()
    }

    fun setData(data: SettingItemSwitchData, lbd: ((Boolean) -> Unit)? = null) {
        //忽略空数据
        if (data.tip.isNotBlank()) {
            this.desc = data.tip
        }
        if (data.title.isNotBlank()) {
            this.title = data.title
        }
        refreshData()

        ivTip.setOnClickListener {
            MessageDialog.show("提示", data.tip, "确定")
        }

        switchSetting.isChecked = data.globalData.currentValue

        lbd?.invoke(switchSetting.isChecked)
        switchSetting.setOnCheckedChangeListener { buttonView, isChecked ->
            data.globalData.setValue(isChecked)
            lbd?.invoke(isChecked)
        }
    }
}

data class SettingItemSwitchData(
    val globalData: GlobalDataConfig<Boolean>,
    val title: String="",
    val tip: String=""
)