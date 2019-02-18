package com.desrumaux.androidtoolbox.model.SafetyNet

import android.app.Activity
import android.util.Log
import android.widget.Toast
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
import org.json.JSONObject


open class SafetyAPI(context : Activity) {

    private val activity: Activity = context

    private fun isGooglePlayAvailable(): Boolean {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity)
            == ConnectionResult.SUCCESS
        ) {
            // The SafetyNet Attestation API is available.
            Toast.makeText(activity, "Google Play Service Available", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    fun generateToken() {

    }

    fun sendSafetyNetRequest() {
        isGooglePlayAvailable()
        val data: String = "abcdefghijklmnopqrstuvwxyz478994lpkihhgfrdsesrdtfghjkknjbhgytt"
        val safetyNetClient: SafetyNetClient = SafetyNet.getClient(activity)
        val task: Task<SafetyNetApi.AttestationResponse> = safetyNetClient.attest(data.toByteArray(), "AIzaSyCx90qtPsXRqFcnmjgpf_by0XjYRbnXQPo")

            task.addOnSuccessListener(activity) { response: SafetyNetApi.AttestationResponse ->
                // Indicates communication with the service was successful.
                // Use response.jwsResult to get the result data.
                val jsonResult = response.jwsResult
                val jsonObject: JSONObject? = JSONObject()
                jsonObject?.put("signedAttestation", jsonResult.toString())

                googleAPIValidation(jsonObject)

                Toast.makeText(activity,jsonResult, Toast.LENGTH_LONG).show()
                Log.i("App Activity","Reponse"+jsonResult)
            }.addOnFailureListener(activity) { e: Exception ->
                // An error occurred while communicating with the service.
                if (e is ApiException) {
                    // An error with the Google Play services API contains some
                    // additional details.
                    // You can retrieve the status code using the
                    // e.statusCode property.
                } else {
                    // A different, unknown type of error occurred.
                    Log.d("App Error", "Error: ${e.message}")
                }
            }
    }

    private fun googleAPIValidation(jsonObject: JSONObject?) {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this.activity)
        val url =
            "https://www.googleapis.com/androidcheck/v1/attestations/verify?key=AIzaSyCx90qtPsXRqFcnmjgpf_by0XjYRbnXQPo"

        // Request a string response from the provided URL.
        val stringRequest = object : JsonObjectRequest(
            Method.POST, url, jsonObject,
            Response.Listener { response ->
                // Display the first 500 characters of the response string.
                Toast.makeText(activity, "Google Play Service:"+response.toString(), Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener {}) {

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }


        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
