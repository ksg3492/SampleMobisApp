package com.sunggil.samplemobisapp.vm.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {
    abstract fun onDestroy()
}