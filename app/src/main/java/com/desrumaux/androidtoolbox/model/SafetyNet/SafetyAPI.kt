package com.desrumaux.androidtoolbox.model.SafetyNet

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.desrumaux.androidtoolbox.BuildConfig
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import com.google.android.gms.safetynet.SafetyNetClient
import com.google.android.gms.tasks.Task
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.desrumaux.androidtoolbox.model.Server.ServerAPI
import org.json.JSONObject


open class SafetyAPI(context : Activity, key : String, token : ByteArray) {

    private val activity: Activity = context
    private var isAllowed: Boolean ?= null
    private var key : String = key
    private var token : ByteArray = token


    private fun isGooglePlayAvailable(): Boolean {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
            == ConnectionResult.SUCCESS
        ) {
            Toast.makeText(activity, "Google Play Service Available", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun sendSafetyNetRequest() {
        if(isGooglePlayAvailable()){
            val safetyNetClient: SafetyNetClient = SafetyNet.getClient(activity)
            val jsonObject: JSONObject? = JSONObject()
            val task: Task<SafetyNetApi.AttestationResponse> = safetyNetClient.attest(token,key)

            task.addOnSuccessListener(activity) { response: SafetyNetApi.AttestationResponse ->
                val jsonResult = response.jwsResult
                jsonObject?.put("signedAttestation", jsonResult.toString())
                googleAPIValidation(jsonObject)
                Toast.makeText(activity,jsonResult, Toast.LENGTH_LONG).show()
            }.addOnFailureListener(activity) { e: Exception ->
                if (e is ApiException) {
                } else {
                    // A different, unknown type of error occurred.
                    Log.d("App Error", "Error: ${e.message}")
                }
            }
        }
    }

    private fun googleAPIValidation(jsonObject: JSONObject?) {
        val queue = Volley.newRequestQueue(this.activity)
        val url = "https://www.googleapis.com/androidcheck/v1/attestations/verify?key="+key

        val stringRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            Response.Listener { response ->
                isAllowed = validationResponseHandler(response)
                Toast.makeText(activity,response.toString(), Toast.LENGTH_SHORT).show()
                if(isAllowed == true){
                    Toast.makeText(activity,"Access Granted", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {}) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        queue.add(stringRequest)
    }

    private fun validationResponseHandler(response: JSONObject): Boolean{
        val isGoogleValidated = response.getBoolean("isValidSignature")
        when(isGoogleValidated){
            true -> return true
            false -> return false
        }
    }
}
