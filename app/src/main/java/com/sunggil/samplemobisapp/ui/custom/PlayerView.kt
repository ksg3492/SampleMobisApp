package com.sunggil.samplemobisapp.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.sunggil.samplemobisapp.databinding.LayoutMainPlayerBinding

public class PlayerView : ConstraintLayout {

    constructor(c : Context, attr : AttributeSet, style : Int) : super(c, attr, style) {
        initView(c)
    }

    constructor(c : Context, attr : AttributeSet) : super(c, attr) {
        initView(c)
    }

    constructor(c : Context) : super(c) {
        initView(c)
    }

    lateinit var binding : LayoutMainPlayerBinding

    fun initView(c : Context) {
        binding = LayoutMainPlayerBinding.inflate(LayoutInflater.from(c), this, false)
//        binding.playerItem = MelonPlayingItem.getInstance()

    }


}