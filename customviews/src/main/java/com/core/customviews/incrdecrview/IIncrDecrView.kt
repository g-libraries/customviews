package com.core.customviews.incrdecrview

interface IIncrDecrView {
    fun getCurrentNumber(): Int
    fun setCurrentNumber(currentNumber: Int)
    fun setOnNumberChangedListener(listener: OnNumberChangedListener)
}

interface OnNumberChangedListener {
    fun onChanged(currentNumber: Int)
}