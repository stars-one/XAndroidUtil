package site.starsone.xandroidutil.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import com.kongzue.dialogx.dialogs.MessageDialog
import site.starsone.xandroidutil.R
import site.starsone.xandroidutil.util.GlobalDataConfig

/**
 * 设置的单选组件,与GlobalData联用
 */
@SuppressLint("CustomViewStyleable")
class SettingItemRadioGroup(context: Context?, attrs: AttributeSet?) :
    RelativeLayout(context, attrs) {
    var title = ""
    var desc = ""

    private var ivTip: RemixIconTextView
    private var tvTip: TextView
    var rg: RadioGroup

    init {
        val view = View.inflate(context, R.layout.layout_setting_item_rg, this)

        ivTip = view.findViewById(R.id.ivTip)
        tvTip = view.findViewById(R.id.tvTip)
        rg = view.findViewById(R.id.rgOutputType)

        context?.apply {
            val ta = obtainStyledAttributes(attrs, R.styleable.SettingItemRadioGroup)
            title =
                ta.getString(R.styleable.SettingItemRadioGroup_text) ?: ""
            desc = ta.getString(R.styleable.SettingItemRadioGroup_tip) ?: ""

            refreshData()
            ta.recycle()
        }
    }

    private fun refreshData() {
        ivTip.setOnClickListener {
            MessageDialog.show("提示", desc, "确定")
        }
        tvTip.text = title
        if (desc.isBlank()) {
            ivTip.visibility = View.INVISIBLE
        }
        invalidate()
    }

    /**
     * 设置radiogroup的排列方式
     * @param type 1:水平排列 2:垂直排列
     */
    fun setRbOrientation(type: Int) {
        if (type == 1) {
            rg.orientation = LinearLayout.HORIZONTAL
        } else {
            rg.orientation = LinearLayout.VERTICAL
        }
    }

    /**
     * 设置数据,选项是int类型的
     */
    fun setData(data: SettingItemRadioGroupDataInt) {
        //忽略空数据
        if (data.tip.isNotBlank()) {
            this.desc = data.tip
        }
        if (data.title.isNotBlank()) {
            this.title = data.title
        }
        refreshData()

        val currentSelectIndex = data.globalData.currentValue
        val list = data.list

        //创建radiobutton
        val rbList = list.map {
            RadioButton(context).apply {
                id = it.first
                tag = it.first
                text = it.second
                isChecked = tag == currentSelectIndex
            }
        }
        //加入radiogroup中
        rbList.forEach {
            rg.addView(it)
        }

        //监听数据
        rg.setOnCheckedChangeListener { _, checkedId ->
            val rb = this.findViewById<RadioButton>(checkedId)
            val index = rb.tag.toString().toInt()
            data.globalData.setValue(index)
        }
    }

    /**
     * 设置数据,选项是string类型的
     */
    fun setData(data: SettingItemRadioGroupDataString) {
        //忽略空数据
        if (data.tip.isNotBlank()) {
            this.desc = data.tip
        }
        if (data.title.isNotBlank()) {
            this.title = data.title
        }
        refreshData()

        val currentSelectIndex = data.globalData.currentValue
        val list = data.list

        //创建radiobutton

        val rbList = list.mapIndexed { index, it ->
            RadioButton(context).apply {
                id = index
                tag = it.first
                text = it.second
                isChecked = tag == currentSelectIndex
            }
        }
        //加入radiogroup中
        rbList.forEach {
            rg.addView(it)
        }

        //监听数据
        rg.setOnCheckedChangeListener { _, checkedId ->
            val rb = this.findViewById<RadioButton>(checkedId)
            val index = rb.tag.toString()
            data.globalData.setValue(index)
        }
    }
}

data class SettingItemRadioGroupDataInt(
    val list: List<Pair<Int, String>>,
    val globalData: GlobalDataConfig<Int>,
    val title: String = "",
    val tip: String = ""
)

data class SettingItemRadioGroupDataString(
    val list: List<Pair<String, String>>,
    val globalData: GlobalDataConfig<String>,
    val title: String = "",
    val tip: String = ""
)