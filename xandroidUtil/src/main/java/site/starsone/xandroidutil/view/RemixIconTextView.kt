package site.starsone.xandroidutil.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.blankj.utilcode.util.ResourceUtils
import org.apache.commons.text.StringEscapeUtils
import org.json.JSONArray
import site.starsone.xandroidutil.R

@SuppressLint("AppCompatCustomView")
class RemixIconTextView : TextView {
    val TAG = "RemixIconTextView"

    private val iconfont: Typeface by lazy {
        //初始化读取图标库数据
        RemixIconData.init()
        //读取图标字体
        Typeface.createFromAsset(context.assets, RemixIconData.fontPath)
    }

     var iconName: String
        set(value) {
            field = value
            iconNameToUnicode()
        }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        typeface = iconfont
        val typearr = context.obtainStyledAttributes(attrs, R.styleable.RemixIconTextView)

        iconName = typearr.getString(R.styleable.RemixIconTextView_iconName) ?: ""
        //设置默认字体大小为16sp
        textSize = 20f
        typearr.recycle()

        iconNameToUnicode()
    }


    /**
     * 将图标名转为unicode来显示
     */
    private fun iconNameToUnicode() {
        Log.d(TAG, "图标名: $iconName")
        if (iconName.isNotBlank()) {
            val unicode = RemixIconData.iconIndexMap[iconName]
            unicode?.let {
                text = it
            }
        }
    }

}

object RemixIconData {


    val iconIndexMap = hashMapOf<String, String>()

    val jsonPath = "remixicon.json"
    val fontPath = "remixicon.ttf"

    fun init() {
        if (iconIndexMap.isEmpty()) {
            val json = ResourceUtils.readAssets2String(jsonPath)
            val jsonArray = JSONArray(json)
            val size = jsonArray.length()
            for (i in 0 until size) {
                val jsonObject = jsonArray.getJSONObject(i)
                val key = jsonObject.getString("name")
                val value = jsonObject.getString("unicode")
                iconIndexMap[key] = StringEscapeUtils.unescapeJava(value)
            }
        }
    }

}