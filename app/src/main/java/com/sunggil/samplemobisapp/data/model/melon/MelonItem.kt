package com.sunggil.samplemobisapp.data.model.melon

import com.sunggil.samplemobisapp.data.model.base.BaseItem

data class MelonItem(var trackId : String) : BaseItem() {
    var id = 0
    var trackListId = ""
    var albumName = ""
    var albumImg = ""
    var albumImgLarge = ""
    var playTime = 0
    var plylstTitle = ""
    var artistName = ""
    var songName = ""
    var date = 0L

    var isMyList = false
    var isAdult = false
    var isService = true
    var menuId = "15010101"
}