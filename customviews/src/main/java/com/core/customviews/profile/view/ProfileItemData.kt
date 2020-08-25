package com.core.customviews.profile.view

import android.graphics.drawable.Drawable
import android.text.InputType
import androidx.annotation.DrawableRes

data class ProfileItemData(
    val type: ProfileItemMode,
    val hint: String,
    var currentData: String?,
    var dropDown: Array<String> = arrayOf(),
    var isEditable: Boolean = true,
    var inputType: Int = InputType.TYPE_CLASS_TEXT,
    var validation: (String?) -> Boolean,
    var onClick: (String) -> Unit,
    @DrawableRes
    var drawable: Int? = null,
    var onDrawableClicked: () -> Unit,
    var lineColorDefault: Int,
    var lineColorError: Int
)