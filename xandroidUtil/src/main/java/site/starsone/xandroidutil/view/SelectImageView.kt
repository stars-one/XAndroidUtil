package site.starsone.xandroidutil.view

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.View
import site.starsone.xandroidutil.R

class SelectImageView : androidx.appcompat.widget.AppCompatImageView {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }


    var normalImgTintColor = -1
    var selectImgTintColor = -1

    var normalDrawable: Drawable? = null
    var selectDrawable: Drawable? = null

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        val tp = context.obtainStyledAttributes(attrs, R.styleable.SelectImageView)
        normalDrawable = tp.getDrawable(R.styleable.SelectImageView_normalImg)
        selectDrawable = tp.getDrawable(R.styleable.SelectImageView_selectImg)
        normalImgTintColor = tp.getColor(R.styleable.SelectImageView_normalImgTint, -1)
        selectImgTintColor = tp.getColor(R.styleable.SelectImageView_selectImgTint, -1)

        setMyDrawable(normalDrawable, selectDrawable)

        setOnClick()
        tp.recycle()
    }

    /**
     * 设置着色
     * @param normalTintColor 正常状态的图标着色,可为空
     * @param selectTintColor 选中状态的图标着色,可为空
     */
    fun setStateColor(normalTintColor: Int?, selectTintColor: Int?) {
        this.normalImgTintColor = normalTintColor ?: -1
        this.selectImgTintColor = selectTintColor ?: -1

        setMyDrawable(normalDrawable, selectDrawable)
    }

    /**
     * 设置图标
     */
    fun setStateDrawable(normalDrawable: Drawable?, selectDrawable: Drawable?) {
        this.normalDrawable = normalDrawable
        this.selectDrawable = selectDrawable

        setMyDrawable(normalDrawable, selectDrawable)
    }

    private fun setMyDrawable(normalDrawable: Drawable?, selectDrawable: Drawable?) {
        // 创建 StateListDrawable
        val stateListDrawable = StateListDrawable()

        //图标着色
        selectDrawable?.apply {
            if (selectImgTintColor != -1) {
                val colorFilter =
                    PorterDuffColorFilter(selectImgTintColor, PorterDuff.Mode.SRC_ATOP)
                setColorFilter(colorFilter)
                invalidateSelf()
            }
        }

        normalDrawable?.apply {
            if (normalImgTintColor != -1) {
                val colorFilter =
                    PorterDuffColorFilter(normalImgTintColor, PorterDuff.Mode.SRC_ATOP)
                setColorFilter(colorFilter)
                invalidateSelf()
            }
        }

        //选中状态的drawable和未选中状态的drawable
        selectDrawable?.apply {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), this)
        }

        normalDrawable.apply {
            stateListDrawable.addState(intArrayOf(-android.R.attr.state_selected), this)
        }

        setImageDrawable(stateListDrawable)
    }

    /**
     * 设置点击事件
     */
    fun setOnClick(action: ((v: View, isSelected: Boolean) -> Unit)? = null) {
        setOnClickListener {
            isSelected = isSelected.not()
            action?.invoke(it, isSelected)
        }
    }
}