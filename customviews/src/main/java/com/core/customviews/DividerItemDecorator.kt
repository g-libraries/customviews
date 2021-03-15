package com.core.customviews

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecorator constructor(private val mDivider: Drawable, private val includeFirstItem: Boolean = true, private val includeLastItem: Boolean = true) : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount

        val firstIndex = if (!includeFirstItem && childCount >= 1) 1 else 0
        val lastIndex = if (includeLastItem) childCount - 1 else childCount - 2

        for (i in firstIndex..lastIndex) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + mDivider.intrinsicHeight

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(canvas)
        }
    }
}