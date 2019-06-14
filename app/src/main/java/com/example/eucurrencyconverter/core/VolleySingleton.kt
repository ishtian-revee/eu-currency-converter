package com.example.eucurrencyconverter.core

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class VolleySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: VolleySingleton(context).also {
                        INSTANCE = it
                    }
                }
    }

    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps us from leaking the Activity or BroadcastReceiver if someone passes one in
        Volley.newRequestQueue(context.applicationContext)
    }

    // this method adds the request from parameter to the request queue
    fun <T> addToRequestQueue(req: Request<T>){ requestQueue.add(req) }
}