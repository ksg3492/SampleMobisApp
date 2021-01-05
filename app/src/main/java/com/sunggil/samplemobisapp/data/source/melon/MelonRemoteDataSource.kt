package com.sunggil.samplemobisapp.data.source.melon

import android.content.Context
import android.os.Build
import android.util.Log
import com.android.volley.Request
import com.sunggil.samplemobisapp.data.model.melon.MelonDomain
import com.sunggil.samplemobisapp.data.model.melon.MelonItem
import com.sunggil.samplemobisapp.data.model.melon.MelonStatus
import com.sunggil.samplemobisapp.data.model.melon.MelonStreamingItem
import com.sunggil.samplemobisapp.network.volley.GsonRequest
import com.sunggil.samplemobisapp.network.volley.VolleyListener
import com.sunggil.samplemobisapp.network.volley.VolleyRequestQueue

class MelonRemoteDataSource {
    private val getMelonHeader = HashMap<String, String>().apply {
        put("Content-Type", "application/json")
        put("company_key", "motrex")
    }

    fun getMelonHit24(context : Context, listener : VolleyListener<MelonDomain>) {
        val gson = GsonRequest(Request.Method.GET,
            "http://www.popmediacloud.com:8081/cloud/api/melon/newChartList",
            null,
            MelonDomain::class.java,
            getMelonHeader,
            listener
        )

        VolleyRequestQueue.getInstance(context).addToRequestQueue(gson)
    }

    fun getMelonStreamingPath(context : Context, item : MelonItem, listener : VolleyListener<MelonStreamingItem>) {
        val params = makeExtraParams()
        params["ukey"] = MelonStatus.member_key
        params["hwKey"] = MelonStatus.pcid
        params["changeSt"] = MelonStatus.isChangeST
        params["sessionId"] = MelonStatus.session_id
        params["cId"] = item.trackId
        params["metaType"] = MelonStatus.metaType
        params["bitrate"] = MelonStatus.bitrate

        //변수 초기화
        MelonStatus.isChangeST= "N" //한번 동시 스트리밍 처리 후 초기화
        MelonStatus.cid = item.trackId
        MelonStatus.menuid = item.menuId
        MelonStatus.bitrate = ""
        MelonStatus.loggingToken = ""


        val gson = GsonRequest(Request.Method.POST,
            "http://www.popmediacloud.com:8081/cloud/api/melon/newStreaming",
            params,
            MelonStreamingItem::class.java,
            getMelonHeader,
            listener
        )

        VolleyRequestQueue.getInstance(context).addToRequestQueue(gson)
    }

    private fun makeUserAgent(): String {
        val osVersion = "Android ${Build.VERSION.RELEASE}"
        return "${MelonStatus.CP_ID}; $osVersion; ${MelonStatus.appVersion}; ${MelonStatus.phoneModel}"
    }

    private fun makeExtraParams(): HashMap<String, String> {
        val params = HashMap<String, String>()
        try {
            params["userAgent"] = makeUserAgent();
        } catch (e : Exception) {
            Log.e("SG2", "makeExtraParams Build.VERSION.RELEASE : ", e);
        }
        params["cpid"] = MelonStatus.CP_ID
        params["cpkey"] = MelonStatus.CP_KEY
        params["cookie"] = MelonStatus.getCookieString()
        return params
    }
}