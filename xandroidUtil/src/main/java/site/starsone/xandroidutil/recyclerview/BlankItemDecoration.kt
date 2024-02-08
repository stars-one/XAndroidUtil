/*
 * Copyright (C) 2019 Shen Lin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * An [RecyclerView.ItemDecoration] implementation which provides
 * functionality that add extra blank space between list items.
 *
 * @param spaceHeight The empty space height between two items.
 * @param orientation Layout orientation. Should be [BlankItemDecoration.HORIZONTAL] or [BlankItemDecoration.VERTICAL]
 * @param firstItemPaddingStart Pass non-zero value if first item need a paddingStart
 * @param lastItemPaddingEnd Pass non-zero value if last item need a paddingEnd
 */
class BlankItemDecoration(
    private val orientation: Int = VERTICAL,
    private val spaceHeight: Int = 0,
    private val firstItemPaddingStart: Int = 0,
    private val lastItemPaddingEnd: Int = 0
) : RecyclerView.ItemDecoration() {

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    init {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw IllegalArgumentException("invalid orientation:$orientation")
        }
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.adapter != null) {
            val totalItemCount = parent.adapter?.itemCount ?: 0
            val itemPosition = parent.getChildAdapterPosition(view)
            if (itemPosition != totalItemCount - 1) {
                //For all the items except the last one
                with(spaceHeight) {
                    when (orientation) {
                        HORIZONTAL -> outRect.right = this
                        VERTICAL -> outRect.bottom = this
                    }
                }
                //This is the first item
                if (itemPosition == 0) {
                    drawFirstItemPadding(outRect)
                }
            } else {
                //This is the last, or the first item if total item count is 1
                if (totalItemCount == 1) {
                    drawFirstItemPadding(outRect)
                }
                drawLastPaddingEnd(outRect)
            }
        } else {
            super.getItemOffsets(outRect, view, parent, state)
        }
    }

    private fun drawLastPaddingEnd(outRect: Rect?) {
        if (lastItemPaddingEnd != 0) {
            with(lastItemPaddingEnd) {
                when (orientation) {
                    HORIZONTAL -> outRect?.right = this
                    VERTICAL -> outRect?.bottom = this
                }
            }
        }
    }

    private fun drawFirstItemPadding(outRect: Rect?) {
        if (firstItemPaddingStart != 0) {
            with(firstItemPaddingStart) {
                when (orientation) {
                    HORIZONTAL -> outRect?.left = this
                    VERTICAL -> outRect?.top = this
                }
            }
        }
    }

}