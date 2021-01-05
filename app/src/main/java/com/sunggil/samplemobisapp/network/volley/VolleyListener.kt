package com.sunggil.samplemobisapp.network.volley

import com.android.volley.Response

interface VolleyListener<T> : Response.Listener<T>, Response.ErrorListener