package dev.weihl.belles.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView ,间距
 */
class SpaceItemDecoration(space: Int, spanCount: Int) : RecyclerView.ItemDecoration() {

    private var space = 10
    private var spaceHalf = 0
    private var spanCount = 2
    private var spanLastIndex = 0

    init {
        this.space = space
        this.spaceHalf = space / 2
        this.spanCount = spanCount
        this.spanLastIndex = spanCount - 1
        if (spanCount <= 0) {
            throw Exception("spanCount need greater than 0 !")
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        // > 2
        val position = parent.getChildLayoutPosition(view)
        when {
            spanCount == 1 -> {
                outRect.left = space
                outRect.right = space
            }
            position % spanCount == 0 -> {
                outRect.left = space
                outRect.right = space / 2
            }
            position % spanCount == spanLastIndex -> {
                outRect.left = spaceHalf
                outRect.right = space
            }
            else -> {
                outRect.left = spaceHalf
                outRect.right = spaceHalf
            }
        }

        if (position < spanCount) {
            outRect.top = space
        }

        outRect.bottom = space

    }

}