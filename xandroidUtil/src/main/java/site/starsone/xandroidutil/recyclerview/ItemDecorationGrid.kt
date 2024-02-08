package site.starsone.xandroidutil.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils

/**
 * 网格布局的间距装饰
 * @param spanCount 每行的item数目
 * @param spaceDp 间隔(单位px)
 */
class ItemDecorationGrid(val spanCount: Int = 3, val spaceDp: Int) : RecyclerView.ItemDecoration() {
    val space = SizeUtils.dp2px(spaceDp.toFloat())

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = column * space / spanCount
        outRect.right = space - (column + 1) * space / spanCount

        //item的上边距,这里各位根据需求自己修改,也可以设置下边距
        if (position >= spanCount) {
            outRect.top = space
        }
    }
}