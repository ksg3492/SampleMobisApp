package com.sunggil.samplemobisapp.ui.base

import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sunggil.samplemobisapp.databinding.ActivityMainBinding
import com.sunggil.samplemobisapp.util.Util
import com.sunggil.samplemobisapp.vm.base.BaseAndroidViewModel

abstract class BaseActivity<T : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {
    private lateinit var viewDataBinding : T
    private lateinit var viewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView<T>(this, getLayout()).apply {
            lifecycleOwner = this@BaseActivity
        }

        viewModel = ViewModelProvider(this).get(initViewModel()::class.java)

        Util.checkDeviceInfos(applicationContext)
        bindingLiveData()
        initView()
    }

    fun getDataBinding() : T {
        return viewDataBinding
    }

    fun getViewModel() : VM {
        return viewModel
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        if (getViewModel() is BaseAndroidViewModel) {
            (getViewModel() as BaseAndroidViewModel).onDestroy()
        }

        super.onDestroy()
    }

    @NonNull abstract fun getLayout() : Int
    @NonNull abstract fun initViewModel() : VM
    abstract fun bindingLiveData()
    abstract fun initView()
}