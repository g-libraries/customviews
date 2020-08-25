package com.core.customviews.incrdecrview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getResourceIdOrThrow
import com.core.customviews.R
import kotlinx.android.synthetic.main.increase_decrease_view.view.*
import timber.log.Timber
import java.lang.IllegalStateException

class IncrDecrView : ConstraintLayout, IIncrDecrView {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    lateinit var increaseView: ImageView
    lateinit var decreaseView: ImageView
    lateinit var countViewET: EditText

    var currnentNumber = 0
    private var maxNumber: Int = 100000
    private var minNumber: Int = 0

    @LayoutRes
    private var layoutId: Int = 0

    var listener: OnNumberChangedListener? = null

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                s.toString().toInt().let {
                    setCurrentNumber(it)
                }
            } catch (e: NumberFormatException) {
                setCurrentNumber(minNumber)
            }
        }
    }

    fun init(context: Context, attrs: AttributeSet?) {


        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.IncrDecrView)


            try {
                layoutId = a.getResourceIdOrThrow(R.styleable.IncrDecrView_layout)
            } finally {
                a.recycle()
            }
        }

        View.inflate(context, layoutId, this)

        countViewET = incr_decr_view_count_tv
        decreaseView = incr_decr_view_minus_iv
        increaseView = incr_decr_view_plus_iv

        setInitialData()

        setClickListenerForItems()
    }

    fun setInitialData() {
        setCurrentNumber(minNumber)
    }

    fun setMaxNumber(number: Int) {
        maxNumber = number
        numberLogic()
    }

    fun setMinNumber(number: Int) {
        minNumber = number
    }

    private fun maxLimitState() {
        increaseView.alpha = 0.3f
    }

    private fun notMaxLimitState() {
        increaseView.alpha = 1f
    }

    private fun minLimitState() {
        decreaseView.alpha = 0.3f
    }

    private fun notMinLimitState() {
        decreaseView.alpha = 1f
    }

    private fun setClickListenerForItems() {
        increaseView.setOnClickListener {
            setCurrentNumber(++currnentNumber)
        }

        decreaseView.setOnClickListener {
            setCurrentNumber(--currnentNumber)
        }

        countViewET.addTextChangedListener(textWatcher)
    }


    private fun numberLogic() {
        if (currnentNumber > minNumber) {
            notMinLimitState()
        } else {
            minLimitState()
            currnentNumber = minNumber
        }

        if (currnentNumber >= maxNumber) {
            maxLimitState()
            currnentNumber = maxNumber
        } else {
            notMaxLimitState()
        }

        countViewET.removeTextChangedListener(textWatcher)
        countViewET.setText(currnentNumber.toString())
        countViewET.addTextChangedListener(textWatcher)
    }

    override fun setCurrentNumber(currentNumber: Int) {
        currnentNumber = currentNumber
        numberLogic()

        listener?.onChanged(currnentNumber)
    }

    override fun getCurrentNumber() = currnentNumber

    override fun setOnNumberChangedListener(listener: OnNumberChangedListener) {
        this.listener = listener
    }
}