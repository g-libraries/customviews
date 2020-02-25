package com.core.customviews.bubblepopup

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Paint.Cap
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.Path.Direction
import android.graphics.RectF
import android.graphics.Shader.TileMode
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.core.customviews.R

class BubbleLayout : RelativeLayout {

    constructor(context: Context?) : super(context) {
        init(context!!, null)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context!!, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context!!, attrs)
    }

    var padding = 30
    var legHalfBase = 30
    var strokeWidth = 2.0f
    var cornerRadius = 8.0f
    var shadowColor = Color.argb(100, 0, 0, 0)
    var bgColor = Color.argb(100, 255, 255, 255)
    var minLegDistance = (padding + legHalfBase).toFloat()
    var bubbleMargin = 16f

    private var mFillPaint: Paint? = null
    private val mPath = Path()
    private val mBubbleLegPrototype = Path()
    private val mPaint = Paint(Paint.DITHER_FLAG)

    private var mBubbleLegOffset = 0.75f
    private var mBubbleOrientation = BubbleLegOrientation.LEFT


    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    enum class BubbleLegOrientation {
        TOP, LEFT, RIGHT, BOTTOM, NONE
    }

    init {
        init(context, null)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.BubbleLayout)

            try {
                padding = a.getDimensionPixelSize(R.styleable.BubbleLayout_padding, padding)
                shadowColor = a.getInt(R.styleable.BubbleLayout_shadowColor, shadowColor)
                bgColor = a.getInt(R.styleable.BubbleLayout_bgColor, bgColor)
                legHalfBase =
                    a.getDimensionPixelSize(R.styleable.BubbleLayout_halfBaseOfLeg, legHalfBase)
                minLegDistance = (padding + legHalfBase).toFloat()
                strokeWidth = a.getFloat(R.styleable.BubbleLayout_bubbleStrokeWidth, strokeWidth)
                bubbleMargin = a.getFloat(R.styleable.BubbleLayout_bubbleMargin, bubbleMargin)
                cornerRadius =
                    a.getFloat(R.styleable.BubbleLayout_bubbleCornerRadius, cornerRadius)
            } finally {
                a.recycle()
            }
        }

        mPaint.color = shadowColor
        mPaint.style = Style.FILL
        mPaint.strokeCap = Cap.BUTT
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = strokeWidth
        mPaint.strokeJoin = Paint.Join.MITER
        mPaint.pathEffect = CornerPathEffect(cornerRadius)

        setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint)

        mFillPaint = Paint(mPaint)
        mFillPaint?.color = bgColor
        mFillPaint?.shader =
            LinearGradient(100f, 0f, 100f, 200f, bgColor, bgColor, TileMode.CLAMP)

        setLayerType(View.LAYER_TYPE_SOFTWARE, mFillPaint)

        mPaint.setShadowLayer(2f, 2f, 5f, shadowColor)

        renderBubbleLegPrototype()

        setPadding(padding, padding, padding, padding)

    }

    private fun renderBubbleLegPrototype() {
        mBubbleLegPrototype.moveTo(0f, 0f)
        mBubbleLegPrototype.lineTo(padding * 1.5f, -padding / 1.5f)
        mBubbleLegPrototype.lineTo(padding * 1.5f, padding / 1.5f)
        mBubbleLegPrototype.close()
    }

    fun setBubbleParams(bubbleOrientation: BubbleLegOrientation, bubbleOffset: Float) {
        mBubbleLegOffset = bubbleOffset
        mBubbleOrientation = bubbleOrientation
    }


    private fun renderBubbleLegMatrix(width: Float, height: Float): Matrix {

        //Leg on the left as default
        var dstX = 0f
        var dstY = 0f
        val matrix = Matrix()

        when (mBubbleOrientation) {
            BubbleLegOrientation.TOP -> {
                dstX = width / 2
                dstY = 0f
                matrix.postRotate(90f)
            }

            BubbleLegOrientation.LEFT -> {
                dstX = 0f
                dstY = height / 2
            }

            BubbleLegOrientation.RIGHT -> {
                dstX = width
                dstY = height / 2
                matrix.postRotate(180f)
            }

            BubbleLegOrientation.BOTTOM -> {
                dstX = width / 2
                dstY = height
                matrix.postRotate(270f)
            }
        }

        matrix.postTranslate(dstX, dstY)
        return matrix
    }

    override fun onDraw(canvas: Canvas) {

        val width = width.toFloat()
        val height = height.toFloat()

        mPath.rewind()
        mPath.addRoundRect(
            RectF(
                padding.toFloat(),
                padding.toFloat(),
                width - padding,
                height - padding
            ), cornerRadius, cornerRadius, Direction.CW
        )
        mPath.addPath(mBubbleLegPrototype, renderBubbleLegMatrix(width, height))

        canvas.drawPath(mPath, mPaint)
        canvas.scale(
            (width - strokeWidth) / width,
            (height - strokeWidth) / height,
            width / 2f,
            height / 2f
        )

        canvas.drawPath(mPath, mFillPaint!!)
    }


}