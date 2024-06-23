package site.starsone.xandroidutil.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.TextView
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
    var showTip = false

    private var ivTip: RemixIconTextView
    private var tvTip: TextView
    var rg: RadioGroup
    var rg2: RadioGroup

    init {
        val view = View.inflate(context, R.layout.layout_setting_item_rg, this)

        ivTip = view.findViewById(R.id.ivTip)
        tvTip = view.findViewById(R.id.tvTip)
        rg = view.findViewById(R.id.rgOutputType)
        rg2 = view.findViewById(R.id.rgOutputType2)

        context?.apply {
            val ta = obtainStyledAttributes(attrs, R.styleable.SettingItem)
            title = ta.getString(R.styleable.SettingItem_text) ?: ""
            desc = ta.getString(R.styleable.SettingItem_tip) ?: ""
            showTip = ta.getBoolean(R.styleable.SettingItem_showTip, false)

            refreshData()
            ta.recycle()
        }
    }

    private fun refreshData() {
        ivTip.setOnClickListener {
            MessageDialog.show("提示", desc, "确定")
        }
        //是否展示提示
        if (showTip) {
            ivTip.visibility = View.VISIBLE
        } else {
            ivTip.visibility = View.GONE
        }
        tvTip.text = title
        if (desc.isBlank()) {
            ivTip.visibility = View.INVISIBLE
        }
        invalidate()
    }

    /**
     * 1:水平排列 2:垂直排列
     */
    var arrangeType = 1

    /**
     * 设置radiogroup的排列方式
     * @param type 1:水平排列 2:垂直排列
     */
    fun setRbOrientation(type: Int) {
        this.arrangeType = type
        if (type == 1) {
            rg2.visibility = View.GONE
        } else {
            rg.visibility = View.GONE
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

        val myrg = if (arrangeType == 1) {
            //水平方向
            rg
        } else {
            rg2
        }
        myrg.removeAllViews()

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
            myrg.addView(it)
        }

        //监听数据
        myrg.setOnCheckedChangeListener { _, checkedId ->
            kotlin.runCatching {
                val rb = this.findViewById<RadioButton>(checkedId)
                val index = rb.tag.toString().toInt()
                data.globalData.setValue(index)
            }
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

        val myrg = if (arrangeType == 1) {
            //水平方向
            rg
        } else {
            rg2
        }
        myrg.removeAllViews()

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
            myrg.addView(it)
        }

        //监听数据
        myrg.setOnCheckedChangeListener { _, checkedId ->
            kotlin.runCatching {
                val rb = this.findViewById<RadioButton>(checkedId)
                val index = rb.tag.toString()
                data.globalData.setValue(index)
            }
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