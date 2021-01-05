package com.sunggil.samplemobisapp.network.volley

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.Volley

class VolleyRequestQueue constructor(context : Context) {
    companion object {
        private var INSTANCE : VolleyRequestQueue? = null

        fun getInstance(c : Context) =
            INSTANCE
                ?: synchronized(this) {
                INSTANCE ?: VolleyRequestQueue(c).also {
                    INSTANCE = it
                }
            }
    }

    private val requestQueue : RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext, HurlStack(null,
            NoSSLv3Factory()
        ), 1024 * 1024)
    }

    fun <T> addToRequestQueue(req : Request<T>) {
        VolleyLog.DEBUG = true


        req.retryPolicy = DefaultRetryPolicy(10 * 1000, 0, 1.0f)
        req.setShouldCache(false)
        requestQueue.add(req)
    }
}