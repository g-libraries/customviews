package com.core.customviews

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.core.base.usecases.setOnOneClickListener
import kotlinx.android.synthetic.main.toggle_password_view.view.*


class TogglePassword : ConstraintLayout {

    lateinit var passwordET: EditText

    private lateinit var iconIV: ImageView
    private var showPassword = false

    constructor(context: Context?, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    fun init(attrs: AttributeSet) {
        View.inflate(context, R.layout.toggle_password_view, this)

        val array = context.obtainStyledAttributes(attrs, R.styleable.TogglePassword)

        passwordET = toggle_password_et
        iconIV = toggle_password_iv_icon

        // Mode - if toggle to New Password use another EditText id
        array.getBoolean(
            R.styleable.TogglePassword_tp_new_password_mode,
            false
        ).let { toggleNewPassword ->
            if (toggleNewPassword) {
                passwordET.id = R.id.toggle_password_new_et_id
            } else
                passwordET.id = R.id.toggle_password_et_id
        }

        // EditText
        array.getDimension(R.styleable.TogglePassword_tp_textSize, -1f).let {
            if (it > 0) {
                passwordET.setTextSize(TypedValue.COMPLEX_UNIT_PX, it)
            }
        }

        array.getColor(
            R.styleable.TogglePassword_tp_textColor,
            ContextCompat.getColor(context, android.R.color.black)
        ).let {
            passwordET.setTextColor(it)
        }

        array.getColor(
            R.styleable.TogglePassword_tp_hintColor,
            ContextCompat.getColor(context, android.R.color.darker_gray)
        ).let {
            passwordET.setHintTextColor(it)
        }

        array.getString(R.styleable.TogglePassword_tp_hint)?.let {
            passwordET.hint = it
        }

        array.getResourceId(
            R.styleable.TogglePassword_tp_background,
            android.R.color.transparent
        ).let {
            passwordET.setBackgroundResource(it)
        }

        // Icon
        array.getResourceId(
            R.styleable.TogglePassword_tp_icon,
            android.R.color.transparent
        ).let {
            iconIV.setImageResource(it)
        }

        array.getDimensionPixelSize(
            R.styleable.TogglePassword_tp_icon_padding,
            0
        ).let {
            val params = iconIV.layoutParams as LayoutParams
            params.setMargins(0, 0, it, 0)
            iconIV.layoutParams = params
        }

        array.recycle()

        // Toggle (show) Password logic
        val passwordTransMethod = PasswordTransformationMethod()
        val textTransMethod = SingleLineTransformationMethod()
        iconIV.setOnOneClickListener {
            val selEnd = passwordET.selectionEnd
            if (showPassword) {
                passwordET.transformationMethod = passwordTransMethod
                showPassword = false
            } else {
                passwordET.transformationMethod = textTransMethod
                showPassword = true
            }
            if (passwordET.text.length >= selEnd)
                passwordET.setSelection(selEnd)
            iconIV.isActivated = showPassword
        }
    }

    fun getText(): String {
        return passwordET.text.toString()
    }
}