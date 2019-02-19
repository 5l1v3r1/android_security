package com.desrumaux.androidtoolbox.model.Server

import android.app.Activity
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.desrumaux.androidtoolbox.model.SafetyNet.SafetyAPI
import kotlin.system.exitProcess


open class ServerAPI(context: Activity){

    private val activity: Activity = context
    private  var token: String ?= null
    private var key : String ?= null

    init {
        getInfoFromServer()
    }

    private fun getInfoFromServer(){

        val queue = Volley.newRequestQueue(this.activity)
        val url = "http://10.211.55.5/?param=tokenAndKey"
        val stringRequest = object : JsonObjectRequest(
            Method.POST, url, null,
            Response.Listener { response ->
                val key = response.get("key").toString()
                val token= response.get("token").toString().toByteArray()
                val safetyApi = SafetyAPI(activity,key,token)
                safetyApi.sendSafetyNetRequest()
            },
            Response.ErrorListener { error ->
                exitProcess(-1)
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        queue.add(stringRequest)
    }

}