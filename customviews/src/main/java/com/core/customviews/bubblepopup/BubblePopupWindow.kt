package com.core.customviews.bubblepopup


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

class BubblePopupWindow(private val context: Context) : PopupWindow() {

    private var bubbleView: BubbleLayout? = null

    val measureHeight: Int
        get() {
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return contentView.measuredHeight
        }

    val measuredWidth: Int
        get() {
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            return contentView.measuredWidth
        }

    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT

        isFocusable = true
        isOutsideTouchable = false
        isClippingEnabled = false

        val dw = ColorDrawable(0)
        setBackgroundDrawable(dw)
    }

    fun setBubbleView(view: View) {
        bubbleView = BubbleLayout(context)
        bubbleView!!.setBackgroundColor(Color.TRANSPARENT)
        bubbleView!!.addView(view)
        contentView = bubbleView
    }

    fun setParam(width: Int, height: Int) {
        setWidth(width)
        setHeight(height)
    }

    @JvmOverloads
    fun show(
        parent: View,
        gravity: Int = Gravity.BOTTOM,
        bubbleOffset: Int = 0
    ) {
        val orientation: BubbleLayout.BubbleLegOrientation

        if (!this.isShowing) {
            orientation = when (gravity) {
                Gravity.BOTTOM -> BubbleLayout.BubbleLegOrientation.TOP
                Gravity.TOP -> BubbleLayout.BubbleLegOrientation.BOTTOM
                Gravity.END -> BubbleLayout.BubbleLegOrientation.LEFT
                Gravity.START -> BubbleLayout.BubbleLegOrientation.RIGHT
                else -> BubbleLayout.BubbleLegOrientation.NONE
            }
            bubbleView!!.setBubbleParams(orientation, (measuredWidth / 2).toFloat())

            val location = IntArray(2)
            parent.getLocationOnScreen(location)

            when (gravity) {
                Gravity.BOTTOM -> showAtLocation(
                    parent,
                    Gravity.NO_GRAVITY,
                    location[0] - measuredWidth / 2 + parent.width / 2,
                    location[1] + parent.height + bubbleOffset
                )
                Gravity.TOP -> showAtLocation(
                    parent,
                    Gravity.NO_GRAVITY,
                    location[0] - measuredWidth / 2 + parent.width / 2,
                    location[1] - measureHeight - bubbleOffset
                )
                Gravity.END -> showAtLocation(
                    parent,
                    Gravity.NO_GRAVITY,
                    location[0] + parent.width + bubbleOffset,
                    location[1] - measureHeight / 2 + parent.height / 2
                )
                Gravity.START -> showAtLocation(
                    parent,
                    Gravity.NO_GRAVITY,
                    location[0] - measuredWidth - bubbleOffset,
                    location[1] - measureHeight / 2 + parent.height / 2
                )
                else -> {
                }
            }
        } else {
            this.dismiss()
        }
    }
}