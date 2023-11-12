package site.starsone.xandroidutil.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import kotlin.math.max
import kotlin.math.min

class FlowLayout : ViewGroup {


    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        context.obtainStyledAttributes(attr, intArrayOf(android.R.attr.maxLines)).apply {
            val maxLine = getInt(0, Int.MAX_VALUE)
            Log.d("starsone", "maxLine: $maxLine ")
            recycle()
        }

        context.obtainStyledAttributes(attr, intArrayOf(android.R.attr.maxRows)).apply {
            //这里
            val maxRow = getInt(0, Int.MAX_VALUE)
            Log.d("starsone", "maxRow: $maxRow ")
            recycle()
        }
    }


    val allLineInfoList = mutableListOf<LineInfo>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //这里onMeasure方法会执行多次,所以数据记得清空
        allLineInfoList.clear()

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthLimit = MeasureSpec.getSize(widthMeasureSpec)

        val modeHeight = MeasureSpec.getMode(heightMeasureSpec)
        val heightLimit = MeasureSpec.getSize(heightMeasureSpec)


        var allHeight = 0

        //每行对应的高度
        var lineHeight = 0
        //每一行的总宽度
        var lineWidth = 0

        var lineViewList = mutableListOf<View>()

        //计算得到所有子View的总height
        for (i in 0 until childCount) {
            val childView = this.getChildAt(i)
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)

            val lp = childView.layoutParams as MarginLayoutParams

            val childWidth = childView.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight = childView.measuredHeight + lp.topMargin + lp.bottomMargin

            //如果超过当前最大宽度,则该view是要换行的
            //这里考虑padding
            if (lineWidth + childWidth > widthLimit - (paddingStart + paddingEnd)) {
                    //换行处理

                    //相当于当前的view已经是下一行了,此时记录当前行的的宽高发生重置
                    //总高度变更,重置当前的行的宽度为View的宽度和高度
                    allHeight += lineHeight

                    lineWidth = 0
                    lineHeight = 0

                    //之后再走相同的逻辑,这里其实可以抽取出来,放在if语句的外边,为了好理解,就都写出来了
                    lineWidth += childWidth
                    lineHeight = max(lineHeight, childHeight)

                    //视频中这里相当于直接处理,下面的注释的代码等同于上面的代码
                    //lineWidth = childWidth
                    //lineHeight = childHeight

                    //保存view
                    lineViewList.add(childView)

                    //将当前行的所有View保存,并记录最大高度
                    allLineInfoList.add(LineInfo(lineViewList, lineHeight))
                    //重置列表数据
                    lineViewList = mutableListOf()

                } else {
                //不换行

                lineWidth += childWidth
                lineHeight = max(lineHeight, childHeight)

                //保存view
                lineViewList.add(childView)
            }

            //如果是最后一个,则要把当前行的高度加上
            if (i == childCount - 1) {
                allHeight += lineHeight
                allLineInfoList.add(LineInfo(lineViewList, lineHeight))
            }
        }

        val finalHegiht = when (modeHeight) {
            MeasureSpec.EXACTLY -> heightLimit
            //考虑padding
            MeasureSpec.AT_MOST -> min(heightLimit, allHeight) + paddingTop + paddingBottom
            else -> allHeight + paddingTop + paddingBottom
        }

        setMeasuredDimension(widthLimit, finalHegiht)


    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d("starsone", "onLayout: ${allLineInfoList.flatMap { it.childViews }.size}")

        //考虑padding,所以加上paddingTop
        var top = 0 + paddingTop

        allLineInfoList.forEach {
            val childViewList = it.childViews
            val lineHeight = it.maxHeight

            //每行开始放置时,left都是0(实际可以看做为x坐标)
            //考虑padding
            var left = 0+ paddingLeft

            //如果view为gone(隐藏)
            childViewList.forEach { childView ->

                val lp = childView.layoutParams as MarginLayoutParams

                //左上角的坐标(cLeft,cTop)和右下角的坐标(cRight,CBottom)
                //坐标系方向 (x轴正坐标往右,y轴正坐标往下)
                val cTop = top + lp.topMargin
                val cLeft = left + lp.leftMargin
                val cRight = cLeft + childView.measuredWidth
                val cBottom = cTop + childView.measuredHeight

                childView.layout(cLeft, cTop, cRight, cBottom)

                //放置一个view后,x坐标会发生变更
                left += lp.leftMargin + childView.measuredWidth + lp.rightMargin
            }

            //一行的元素放置完,view的高度发生变化,起始的坐标发生变化
            top += lineHeight

        }

    }


    //view没有设置没有设置LayoutParam
    override fun generateDefaultLayoutParams(): LayoutParams {
        Log.d("starsone", "generateDefaultLayoutParams: 无参")
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    //View是通过Layout.inflater或View.inflate生成的
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        Log.d("starsone", "generateDefaultLayoutParams: AttributeSet参数")
        return MarginLayoutParams(context, attrs)
    }

    //checkLayoutParams如果返回为false,会调用此方法生成LayoutParam
    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        Log.d("starsone", "generateDefaultLayoutParams: LayoutParams参数")
        return MarginLayoutParams(p)
    }

    //addview的时候会调用此方法来判断当前layoutparam
    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }
}

/**
 * 当前行需要摆放的view以及行的最大高度
 */
data class LineInfo(val childViews: MutableList<View>, var maxHeight: Int = 0)