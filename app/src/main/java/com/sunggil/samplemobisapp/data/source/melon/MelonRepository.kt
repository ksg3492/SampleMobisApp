package com.sunggil.samplemobisapp.data.source.melon

import android.content.Context
import com.sunggil.samplemobisapp.data.model.melon.MelonDomain
import com.sunggil.samplemobisapp.data.model.melon.MelonItem
import com.sunggil.samplemobisapp.data.model.melon.MelonStreamingItem
import com.sunggil.samplemobisapp.network.volley.VolleyListener

class MelonRepository {
    companion object {
        private var INSTANCE : MelonRepository? = null

        fun getInstance() = INSTANCE
            ?: synchronized(this) {
            INSTANCE
                ?: MelonRepository()
                    .also {
                INSTANCE = it
            }
        }
    }

    private var remoteDataSource : MelonRemoteDataSource =
        MelonRemoteDataSource()

    fun getMelonHit24(context : Context, listener : VolleyListener<MelonDomain>) {
        remoteDataSource.getMelonHit24(context, listener)
    }

    fun getMelonStreamingPath(context : Context, item : MelonItem, listener : VolleyListener<MelonStreamingItem>) {
        remoteDataSource.getMelonStreamingPath(context, item, listener)
    }
}