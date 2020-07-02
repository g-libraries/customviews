package com.core.customviews

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import com.core.base.util.convertToPhoneNumberWithReplace
import com.core.base.util.isSpace
import timber.log.Timber

class PhoneNumberEditText : AppCompatEditText {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var str: CharSequence? = ""
    var isBackSpace = false

    init {
        addTextWatcher()
    }

    private fun addTextWatcher() {
        addTextChangedListener(object : TextWatcher {
            var textBefore: String = ""
            var lengthBefore = 0

            override fun afterTextChanged(s: Editable?) {
                var selection = selectionStart

                removeTextChangedListener(this)

                var textToConvert = str

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

                var resultText = textToConvert.toString().convertToPhoneNumberWithReplace()

                try {
                    if (resultText.toCharArray()[5] == '0') {
                        resultText = textBefore
                        selection -= 1
                    }
                } catch (e: IndexOutOfBoundsException) {

                }

                if (resultText.startsWith("+380 ", true)) {

                } else {
                    resultText = try {
                        textBefore = if (resultText.length > 5) textBefore else "+380 "
                        textBefore
                    } catch (e: IndexOutOfBoundsException) {
                        "+380 "
                    }
                }

                setText(resultText)

                addTextChangedListener(this)

                if (selection < 5) {
                    selection = 4
                } else {

                }

                try {
                    if (resultText[selection].isSpace()) {
                        selection += 1
                    }
                } catch (e: IndexOutOfBoundsException) {

                }

                val maxSelection = text?.length ?: 0
                setSelection(if (selection > maxSelection) maxSelection else selection)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                textBefore = s.toString()
                lengthBefore = textBefore.length
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                str = s
            }
        })
    }
}

fun EditText.placeCursorToEnd() {
    this.setSelection(this.text.length)
}

