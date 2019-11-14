package com.core.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import android.view.inputmethod.EditorInfo
import timber.log.Timber

/**
 * EditText which can send DEL event when DEL is fired on soft keyboard
 * when there is no text left
 */
class AuthCodeErasableEditText : ToggleBackgroundEditText {

    companion object {

        const val KEYCODE_DELETE = 198233

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context) : super(context)

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        return CustomInputConnection(
            super.onCreateInputConnection(outAttrs),
            true
        )
    }

    private inner class CustomInputConnection(target: InputConnection, mutable: Boolean) :
        InputConnectionWrapper(target, mutable) {

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            Timber.d("deleteSurroundingText: beforeLength = $beforeLength, afterLength $afterLength")
            return if (beforeLength == 1 && afterLength == 0) {
                // backspace
                sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KEYCODE_DELETE
                    )
                ) && sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KEYCODE_DELETE))
            } else super.deleteSurroundingText(beforeLength, afterLength)

        }

    }

    fun Array<AuthCodeErasableEditText>.getTextFromTextViews(): String {
        var result = ""
        for (view in this) {
            result = result.plus(view.text.toString())
        }
        return result
    }


}