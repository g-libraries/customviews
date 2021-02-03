package com.core.customviews.login

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.core.base.util.convertToPhoneNumberWithReplace
import com.core.base.util.isSpace
import timber.log.Timber
import java.lang.IndexOutOfBoundsException

class PhoneNumberEditText : AppCompatEditText {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var isBackSpace = false

    var inputMask = "+XXX XX XXX XX XX"

    var prefixValue = "+380 "

    init {
        addTextWatcher()
    }

    override fun setSelection(index: Int) {
        super.setSelection((text?.length ?: 0).coerceAtMost(index))
    }

    private fun addTextWatcher() {
        addTextChangedListener(object : TextWatcher {
            var textBefore: String = ""
            var lengthBefore = 0

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textBefore = s.toString()
                lengthBefore = textBefore.length
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var selection = selectionStart

                removeTextChangedListener(this)

                var textToConvert = s

                if (textToConvert != null)
                    isBackSpace = textToConvert.length < lengthBefore

                try {
                    if (isBackSpace) {
                        if (textBefore[selection].isSpace()) {
                            val sb = StringBuilder(textBefore)
                            sb.deleteCharAt(selection)
                            sb.deleteCharAt(selection - 1)
                            textToConvert = sb.toString()
                            selection -= 1
                        }
                    }
                } catch (e: StringIndexOutOfBoundsException) {
                    Timber.e(e)
                }

                var resultText = textToConvert.toString().convertToPhoneNumberWithReplace(inputMask)
                val prefixLength = prefixValue.length

                try {
                    if (resultText.toCharArray()[prefixLength] == '0') {
                        resultText = textBefore
                        selection -= 1
                    }
                } catch (e: IndexOutOfBoundsException) {

                }

                if (resultText.startsWith(prefixValue, true)) {

                } else {
                    resultText = try {
                        textBefore = if (resultText.length > prefixLength) textBefore else prefixValue
                        textBefore
                    } catch (e: IndexOutOfBoundsException) {
                        prefixValue
                    }
                }

                setText(resultText)

                addTextChangedListener(this)

                if (selection < prefixLength) {
                    selection = prefixLength - 1
                }

                try {
                    if (resultText[selection].isSpace()) {
                        selection += 1
                    }
                } catch (e: IndexOutOfBoundsException) {

                }

                try {
                    setSelection(selection)
                } catch (e: IndexOutOfBoundsException) {
                    placeCursorToEnd()
                }

            }
        })
    }
}

fun EditText.placeCursorToEnd() {
    this.setSelection(this.text.length)
}

