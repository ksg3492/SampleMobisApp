package com.sunggil.samplemobisapp.data.model.melon

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.sunggil.samplemobisapp.BR

class MelonPlayingItem : BaseObservable() {
    companion object {
        private var INSTANCE : MelonPlayingItem? = null

        fun getInstance() = INSTANCE
            ?: synchronized(this) {
            INSTANCE
                ?: MelonPlayingItem()
                    .also {
                INSTANCE = it
            }
        }
    }

    @get:Bindable
    var title : String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    var subTitle = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.subTitle)
        }
    @get:Bindable
    var progress = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.progress)
        }
    @get:Bindable
    var max = 0
        set(value) {
            field = value
            notifyPropertyChanged(BR.max)
        }
    @get:Bindable
    var thumbnail = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.thumbnail)
        }
    @get:Bindable
    var thumbnailBG = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.thumbnailBG)
        }
}