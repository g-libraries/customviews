package com.core.customviews.login

import android.text.Editable
import android.text.TextWatcher


class AuthCodeTextWatcher constructor(
    private val views: Array<AuthCodeErasableEditText>,
    private val inputListener: IInputListener
) :
    TextWatcher {

    private var inputCompleted: Boolean = false

    init {
        for (view in views)
            view.addTextChangedListener(this)
    }

    interface IInputListener {
        fun onInput(code: String, completed: Boolean)
    }

    private var tempLength = 0

    override fun afterTextChanged(s: Editable?) {
        val input = views.getTextFromTextViews()
        val length = input.length
        // Handle auth_navigation between fields
        if (length > tempLength && views.size > input.length) {
            views[length].requestFocus()
        } else if (length > 0 && length < views.size) {
            views[length - 1].requestFocus()
        }
        tempLength = length

        // Handle focus when no input
        if (length == 0) views[0].requestFocus()

        // Return full input
        inputCompleted = length >= views.size
        inputListener.onInput(input, inputCompleted)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun getFocusedViewPosition(): Int {
        var position = -1
        for (i in views.indices) {
            if (views[i].isFocused) position = i
        }
        return position
    }

}