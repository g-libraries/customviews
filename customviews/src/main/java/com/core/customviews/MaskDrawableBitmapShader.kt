package com.core.customviews

import android.graphics.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.drawable.Drawable


class MaskDrawableBitmapShader : Drawable() {

    private var mPictureBitmap: Bitmap? = null
    private val mPaintShader = Paint()
    private var mBitmapShader: BitmapShader? = null
    private var mPath: Path? = null


    fun setPictureBitmap(src: Bitmap) {
        mPictureBitmap = src
        mBitmapShader = BitmapShader(
            mPictureBitmap!!,
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        )

        mPaintShader.shader = mBitmapShader
        mPaintShader.isAntiAlias = true

        mPath = Path()
        mPath!!.addOval(0F, 0F, intrinsicWidth.toFloat(), intrinsicHeight.toFloat(), Path.Direction.CW)
        val subPath = Path()

        subPath.addOval(
            intrinsicWidth * 0.61f,
            0F,
            intrinsicWidth.toFloat(),
            intrinsicHeight * 0.38f,
            Path.Direction.CW
        )

        mPath!!.op(subPath, Path.Op.DIFFERENCE)
    }

   override fun draw(canvas: Canvas) {
        if (mPictureBitmap == null) {
            return
        }
        canvas.drawPath(mPath!!, mPaintShader)
    }

    override fun setAlpha(alpha: Int) {
        mPaintShader.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaintShader.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun getIntrinsicWidth(): Int {
        return if (mPictureBitmap != null) mPictureBitmap!!.width else super.getIntrinsicWidth()
    }

    override fun getIntrinsicHeight(): Int {
        return if (mPictureBitmap != null) mPictureBitmap!!.height else super.getIntrinsicHeight()
    }
}