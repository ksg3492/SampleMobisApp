package com.sunggil.samplemobisapp.vm.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    abstract fun onDestroy()
}