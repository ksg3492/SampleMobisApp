package com.sunggil.samplemobisapp.ui.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout

abstract class BaseConstraintLayout : ConstraintLayout {
    constructor(c: Context) : this(c, null)

    constructor(c: Context, attr: AttributeSet?) : super(c, attr)

    init {
        View.inflate(context, getLayout(), this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initView()
    }

    @NonNull abstract fun getLayout() : Int
    abstract fun initView()
}