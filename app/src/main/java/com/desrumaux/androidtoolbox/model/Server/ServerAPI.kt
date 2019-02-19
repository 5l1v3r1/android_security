package com.desrumaux.androidtoolbox.model.Server

import android.app.Activity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


open class ServerAPI(context: Activity){

    private val activity: Activity = context
    private lateinit var token: String
    private lateinit var key : String

    init {
        getTokenFromServer()
        getKeyFromServer()
    }

    private fun getTokenFromServer() {
        val queue = Volley.newRequestQueue(activity)
        val url = "http://10.211.55.5/?param=token"
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                this.token = response
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(activity, token, duration)
                toast.show()
            },
            Response.ErrorListener {
                val text = "Impossible d'accéder au service !"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(activity, text, duration)
                toast.show()
            })
        queue.add(stringRequest)
    }

    private fun getKeyFromServer() {
        val queue = Volley.newRequestQueue(activity)
        val url = "http://10.211.55.5/?param=key"
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                this.key = response
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(activity, key, duration)
                 toast.show()
            },
            Response.ErrorListener {
                val text = "Impossible d'accéder au service !"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(activity, text, duration)
                toast.show()
            })
        queue.add(stringRequest)
    }

    fun getToken(): ByteArray{
        return this.token.toByteArray()
    }

    fun getKey(): String{
        return this.key
    }
}