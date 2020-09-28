package com.core.customviews.login

import android.view.KeyEvent
import android.view.View
import com.core.base.util.validateCodeInput


class AuthCodeKeyWatcher constructor(
    private val views: Array<AuthCodeErasableEditText>,
    val activeViewChanged: (index: Int) -> Unit
) : View.OnKeyListener {

    init {
        for (view in views)
            view.setOnKeyListener(this)
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        val length = views.getTextFromTextViews().length
        val focusedPosition = getFocusedViewPosition()
        // Handle delete on different devices
        if (keyCode == AuthCodeErasableEditText.KEYCODE_DELETE && event?.action == KeyEvent.ACTION_DOWN && length > 0) {
            views[length - 1].text?.clear()
            views[length - 1].requestFocus()
            activeViewChanged(length - 1)
            return true
        } else if (keyCode == KeyEvent.KEYCODE_DEL && event?.action == KeyEvent.ACTION_DOWN && length > 0) {
            views[length - 1].text?.clear()
            views[length - 1].requestFocus()
            activeViewChanged(length - 1)
            return true
        }
        // Handle input in already non-empty field
        val input = event?.displayLabel.toString()
        if (validateCodeInput(keyCode) && event?.action == KeyEvent.ACTION_DOWN && views.size > length && focusedPosition + 1 == length) {
            views[length].setText(input)
            if (views.size > length + 1) {
                views[length + 1].requestFocus()
                activeViewChanged(length + 1)
            } else {
                views[length].requestFocus()
                views[length].setSelection(1)
                activeViewChanged(length + 1)
            }
        }
        // Handle first input in non-first position
        else if (focusedPosition != 0 && views[0].text?.isEmpty() == true) {
            views[0].setText(input)
        }
        return false
    }

    private fun getFocusedViewPosition(): Int {
        var position = -1
        for (i in views.indices) {
            if (views[i].isFocused) position = i
        }
        return position
    }

}