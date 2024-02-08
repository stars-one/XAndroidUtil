package site.starsone.xandroidutil.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils

/**
 *  垂直线性布局的间距装饰
 * @param space 间距(单位dp)
 */
class ItemDecorationVertical(val spaceDp: Int) : RecyclerView.ItemDecoration() {

    val space = SizeUtils.dp2px(spaceDp.toFloat())

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position = parent.getChildAdapterPosition(view)

        val allCount = parent.adapter?.itemCount ?: 0

        //最后一个不加边距
        if (position == allCount - 1) {
            return
        }

        outRect.bottom = space
    }
}

