package com.sunggil.samplemobisapp.network.volley

import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

/**
 * Make a GET request and return a parsed object from JSON.
 *
 * @param url URL of the request to make
 * @param clazz Relevant class object, for Gson's reflection
 * @param headers Map of request headers
 */
class GsonRequest<T> (
    method : Int,
    url: String,
    private val params : HashMap<String, String>?,
    private val clazz: Class<T>,
    private val headers: MutableMap<String, String>?,
    private val listener: VolleyListener<T>
    ) : Request<T> (method, url, listener) {
    private val gson = Gson()

    override fun getHeaders(): MutableMap<String, String> = headers ?: super.getHeaders()

    override fun deliverResponse(response: T) {
        Log.e("SG2","VOLLEY REQUESTED URL : $url")
        listener.onResponse(response)
    }

//    @Throws(AuthFailureError::class)
//    override fun getParams(): MutableMap<String, String>? {
//        return params
//    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray {
        if (params != null && method == Request.Method.POST) {
            return gson.toJson(params).toByteArray()
        }
        return super.getBody()
    }

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                response?.data ?: ByteArray(0),
                Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
            Response.success(
                gson.fromJson(json, clazz),
                HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: UnsupportedEncodingException) {

            Log.e("SG2", " UnsupportedEncodingException : " , e)
            Response.error(ParseError(e))
        } catch (e: JsonSyntaxException) {
            Log.e("SG2", " JsonSyntaxException : " , e)
            Response.error(ParseError(e))
        }
    }
}