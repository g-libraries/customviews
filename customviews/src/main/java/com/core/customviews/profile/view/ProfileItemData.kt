package com.core.customviews.profile.view

import android.graphics.drawable.Drawable
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText

data class ProfileItemData(
    val type: ProfileItemMode,
    val dataType: Int,
    val hint: String,
    var currentData: String?,
    var validation: (String?) -> Boolean,
    var dropDown: Array<String> = arrayOf(),
    var isEditable: Boolean = true,
    var inputType: Int = InputType.TYPE_CLASS_TEXT,
    var onClick: (String, AppCompatEditText) -> Unit = { _, _ -> },
    @DrawableRes
    var drawable: Int? = null,
    var onDrawableClicked: (View) -> Unit = {},
    var lineColorDefault: Int = 0x000,
    var lineColorError: Int = 0x000
)