package com.core.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import timber.log.Timber


class MaxHeightNestedScrollView : NestedScrollView {

    private var maxHeight = WITHOUT_MAX_HEIGHT_VALUE

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecC = heightMeasureSpec
        try {
            var heightSize = MeasureSpec.getSize(heightMeasureSpec)
            if (maxHeight != WITHOUT_MAX_HEIGHT_VALUE && heightSize > maxHeight) {
                heightSize = maxHeight
            }
            heightMeasureSpecC = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST)
            layoutParams.height = heightSize
        } catch (e: Exception) {
            Timber.e("Error forcing height : ${e.message}")
        } finally {
            super.onMeasure(widthMeasureSpec, heightMeasureSpecC)
        }
    }

    fun setMaxHeight(maxHeight: Int) {
        this.maxHeight = maxHeight
        invalidate()
    }

    companion object {
        var WITHOUT_MAX_HEIGHT_VALUE = -1
    }
}