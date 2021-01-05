package com.sunggil.samplemobisapp.data.model.melon

data class MelonDomain(var listType : String) {
    var error = ""
    var content : ArrayList<MelonItem> = ArrayList<MelonItem>()
}