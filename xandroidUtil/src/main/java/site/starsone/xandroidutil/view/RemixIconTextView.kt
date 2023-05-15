package site.starsone.xandroidutil.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.graphics.TypefaceCompat
import org.apache.commons.text.StringEscapeUtils
import org.json.JSONArray
import site.starsone.xandroidutil.R

@SuppressLint("AppCompatCustomView", "RestrictedApi")
class RemixIconTextView : TextView {
    val TAG = "RemixIconTextView"

    val androidTextSize = intArrayOf(android.R.attr.textSize)

    private val iconfont: Typeface by lazy {
        if (RemixIconData.iconfont == null) {
            //初始化读取图标库数据
            val inputStream = context.resources.assets.open(RemixIconData.jsonPath)
            val json = inputStream.bufferedReader().readText()
            RemixIconData.init(json)

            //读取图标字体
            val iconFont = TypefaceCompat.createFromResourcesFontFile(
                context,
                context.resources,
                R.font.remixicon,
                "",
                0
            )
            RemixIconData.iconfont =
                iconFont ?: Typeface.createFromAsset(context.assets, RemixIconData.fontPath)
        }

        RemixIconData.iconfont!!
    }

    var iconName: String = ""
        set(value) {
            field = value
            iconNameToUnicode()
        }

    @SuppressLint("RestrictedApi")
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        typeface = iconfont
        val typearr = context.obtainStyledAttributes(attrs, R.styleable.RemixIconTextView)
        iconName = typearr.getString(R.styleable.RemixIconTextView_iconName) ?: ""
        //设置默认字体大小为16sp
        typearr.recycle()
        iconNameToUnicode()
    }


    /**
     * 将图标名转为unicode来显示
     */
    private fun iconNameToUnicode() {

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

    fun init(json: String) {
        if (iconIndexMap.isEmpty()) {
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

    var iconfont: Typeface? = null

}