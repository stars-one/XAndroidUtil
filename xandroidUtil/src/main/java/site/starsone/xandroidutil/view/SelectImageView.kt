package site.starsone.xandroidutil.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
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


    private fun init(attrs: AttributeSet?, defStyle: Int) {

        val tp = context.obtainStyledAttributes(attrs, R.styleable.SelectImageView)
        val normalImgDrawable = tp.getDrawable(R.styleable.SelectImageView_normalImg)
        val selectImgDrawable = tp.getDrawable(R.styleable.SelectImageView_selectImg)

        if (normalImgDrawable != null && selectImgDrawable != null) {
            setStateDrawable(normalImgDrawable, selectImgDrawable)
        }

        setOnClick()
        tp.recycle()

    }

    fun setStateDrawable(normalDrawable: Drawable, selectDrawable: Drawable) {
        // 创建 StateListDrawable
        val stateListDrawable = StateListDrawable()

        //选中状态的drawable和未选中状态的drawable
        stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selectDrawable)
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_selected), normalDrawable)

        //val pressedDrawable = resources.getDrawable(R.drawable.guide_choice_selected, null)
        //val defaultDrawable = resources.getDrawable(R.drawable.guide_choice_normal, null)

        setImageDrawable(stateListDrawable)
    }

    fun setOnClick() {
        setOnClickListener {
            isSelected = isSelected.not()
        }
    }
}