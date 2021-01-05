package com.sunggil.samplemobisapp.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.sunggil.samplemobisapp.data.model.base.BaseItem
import com.sunggil.samplemobisapp.data.model.genie.GenieDomain
import com.sunggil.samplemobisapp.data.model.genie.GenieItem
import com.sunggil.samplemobisapp.vm.base.BaseAndroidViewModel
import com.sunggil.samplemobisapp.data.model.melon.MelonDomain
import com.sunggil.samplemobisapp.data.model.melon.MelonItem
import com.sunggil.samplemobisapp.data.model.melon.MelonStreamingItem
import com.sunggil.samplemobisapp.data.source.genie.GenieRepository
import com.sunggil.samplemobisapp.data.source.melon.MelonRepository
import com.sunggil.samplemobisapp.network.volley.VolleyListener
import com.sunggil.samplemobisapp.player.ExoPlayerService

class MainActivityVM(private var app : Application) : BaseAndroidViewModel(app) {
    private val _listData = MutableLiveData<ArrayList<BaseItem>>()
    val listData : LiveData<ArrayList<BaseItem>> get() = _listData

    fun getMelonHit24() {
        MelonRepository.getInstance().getMelonHit24(app.applicationContext, object : VolleyListener<MelonDomain> {
            override fun onResponse(it: MelonDomain) {
                val list = it?.content

                val returnList = ArrayList<BaseItem>()

                for (item in list) {
                    returnList.add(item)
                }
                _listData.value = returnList
            }

            override fun onErrorResponse(it: VolleyError?) {
                Log.e("SG2", "getMelonHit24 Err", it)
            }
        })
    }

    fun getGeineRealTime(page : Int) {
        val map = HashMap<String, String>()
        map.put("pg", page.toString())
        map.put("pgsize", "10")

        GenieRepository.getInstance().getRealTime(app.applicationContext, map, object : VolleyListener<GenieDomain> {
            override fun onResponse(it: GenieDomain) {
                val list = it?.items

                val returnList = ArrayList<BaseItem>()

                for (item in list) {
                    returnList.add(item)
                }
                _listData.value = returnList
            }

            override fun onErrorResponse(it: VolleyError?) {
                Log.e("SG2", "getGeineRealTime Err", it)
            }
        })
    }

    fun getStreamingPath(item : MelonItem) {
        MelonRepository.getInstance().getMelonStreamingPath(app.applicationContext, item, object : VolleyListener<MelonStreamingItem> {
            override fun onResponse(it: MelonStreamingItem) {
                Log.e("SG2", "getStreamingPath PATH : ${it?.GETPATHINFO?.PATH}")
                ExoPlayerService.getInstance().prepare(it)
            }

            override fun onErrorResponse(it: VolleyError?) {
                Log.e("SG2", "getStreamingPath Err", it)
            }
        })
    }

    fun playpause() {
        if (ExoPlayerService.getInstance().isPlaying() ) {
            ExoPlayerService.getInstance().pause()
        } else {
            ExoPlayerService.getInstance().play()
        }
    }

    fun isPlaying() : Boolean {
        return ExoPlayerService.getInstance().isPlaying()
    }


    fun play() {
        if (!ExoPlayerService.getInstance().isPlaying()) {
            ExoPlayerService.getInstance().play()
        }
    }

    fun pause() {
        if (ExoPlayerService.getInstance().isPlaying()) {
            ExoPlayerService.getInstance().pause()
        }
    }

    fun seek(sec : Int, wasPlaying : Boolean) {
        ExoPlayerService.getInstance().seekTo(sec, wasPlaying)
    }

    override fun onDestroy() {
    }
}