package com.core.customviews

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView


class BottomShapeImageView : AppCompatImageView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    var viewWidth: Int = 0
    var viewHeight: Int = 0

    var radiuscorner: Int = 20

    lateinit var bottomShape: Path
    lateinit var bottomShapePaint: Paint

    internal fun init() {
        setWillNotDraw(false)

        bottomShape = Path()

        bottomShapePaint = Paint(ANTI_ALIAS_FLAG)
        bottomShapePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Create a circular path.
        val halfWidth = width / 2
        val halfHeight = height / 2
        bottomShape.addCircle(halfWidth.toFloat(), halfHeight.toFloat(), radiuscorner.toFloat(), Path.Direction.CCW)

        canvas.drawPath(bottomShape, bottomShapePaint)
    }
}