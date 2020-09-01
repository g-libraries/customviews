package com.core.customviews.profile.view

import android.text.TextWatcher
import android.widget.EditText

enum class ProfileItemMode {
    TEXT, SPINNER, NOT_TEXT
}

enum class ProfileEditModel {
    EDIT, CREATE
}

fun EditText.applyWithDisabledTextWatcher(
    textWatcher: TextWatcher,
    codeBlock: EditText.() -> Unit
) {
    this.removeTextChangedListener(textWatcher)
    codeBlock()
    this.addTextChangedListener(textWatcher)
}