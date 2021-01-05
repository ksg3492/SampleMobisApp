package com.sunggil.samplemobisapp.data.source.genie

import android.content.Context
import com.sunggil.samplemobisapp.data.model.genie.GenieDomain
import com.sunggil.samplemobisapp.network.volley.VolleyListener

class GenieRepository {
    companion object {
        private var INSTANCE : GenieRepository? = null

        fun getInstance() = INSTANCE
            ?: synchronized(this) {
            INSTANCE
                ?: GenieRepository()
                    .also {
                INSTANCE = it
            }
        }
    }

    private var remoteDataSource : GenieRemoteDataSource = GenieRemoteDataSource()

    fun getRealTime(context : Context, params : HashMap<String, String>, listener : VolleyListener<GenieDomain>) {
        remoteDataSource.getRealTime(context, params, listener)
    }
}