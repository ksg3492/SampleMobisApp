package com.sunggil.samplemobisapp.data.source.genie

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.sunggil.samplemobisapp.data.model.genie.GenieDomain
import com.sunggil.samplemobisapp.data.model.melon.MelonDomain
import com.sunggil.samplemobisapp.data.model.melon.MelonStatus
import com.sunggil.samplemobisapp.network.volley.GsonRequest
import com.sunggil.samplemobisapp.network.volley.VolleyListener
import com.sunggil.samplemobisapp.network.volley.VolleyRequestQueue
import com.sunggil.samplemobisapp.util.Util

class GenieRemoteDataSource {
    val CPID = "MGUxMDIyMTAtZDU5NS00MzkxLWI0YjYt"
    val CPKEY = "MY~Pz!ez01"
    val CPID_TEST = "ZjE4MTYxNDYtYmMwNi00N2E5LThmZmYt"
    val CPKEY_TEST = "PNV*i#ql83"

    private val getGenieHeader = HashMap<String, String>().apply {
        put("Content-Type", "application/json")
        put("Authorization", "Basic ${Util.base64Encoded(CPID_TEST + ":" + CPKEY_TEST)}")
        put("x-device-id", MelonStatus.pcid)
    }

    fun getRealTime(context : Context, params : HashMap<String, String>, listener : VolleyListener<GenieDomain>) {
        val paramStr = Util.mapToStringParams(params)
        val url = "https://dev-apis.genie.co.kr/api/v1/browse/tops$paramStr"

        val gson = GsonRequest(Request.Method.GET,
            url,
            params,
            GenieDomain::class.java,
            getGenieHeader,
            listener
        )

        VolleyRequestQueue.getInstance(context).addToRequestQueue(gson)
    }

}