package com.sunggil.samplemobisapp.player

interface PlayerCallback {
    fun onPrepared(duration : Int)

    fun onBuffering()

    fun onPlayed()

    fun onPaused()

    fun onProgress(sec : Int)

    fun onCompletion()

    fun onError(errMsg : Exception)
}