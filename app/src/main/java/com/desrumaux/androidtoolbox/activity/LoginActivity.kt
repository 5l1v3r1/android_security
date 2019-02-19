package com.desrumaux.androidtoolbox.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.desrumaux.androidtoolbox.R
import com.desrumaux.androidtoolbox.model.SafetyNet.SafetyAPI
import com.desrumaux.androidtoolbox.model.Server.ServerAPI
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    companion object {
        const val USER_PREFS = "user_prefs"
    }

//    private var sharedPreferences: SharedPreferences? = null   OU :
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)
        val savedId = sharedPreferences.getString("ID", "") ?: ""
        val savedPassword = sharedPreferences.getString("PW", "") ?: ""

        val serverAPI = ServerAPI(this)
        if (!savedId.isEmpty() && !savedPassword.isEmpty()) {
            auth(savedId,savedPassword)
        }
    }

    private fun saveUserPref(id: String, pw: String) {
        val editor = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE).edit()
        editor.putString("ID", id)
        editor.putString("PW", pw)
        editor.apply()
    }

    fun displayToast(view: View) {
        Toast.makeText(this, emailInput.text, Toast.LENGTH_SHORT).show()
    }

    fun doLogin(view: View) {
        if (emailInput.text.toString().isEmpty() || passwordInput.text.toString().isEmpty()) {
            toastInfo(getString(R.string.login_error_empty))
        } else {
            auth(emailInput.text.toString(), passwordInput.text.toString())
            emailInput.text?.clear()
            passwordInput.text?.clear()
        }
    }

    private fun toastInfo(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun goToHome(clearCache: Boolean) {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        if (clearCache) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        if (clearCache) finish()
    }

    private fun auth(mail: String, pass: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.211.55.5/?param=authent&user=" + mail + "&password=" + pass
        // Request
        // a string response from the provided URL.
        val reponse = Response.Listener<String> { response ->
            val text = response
            if (text == "1") {
                saveUserPref(mail,pass)
                val dtext = "Connexion Acceptée: $response"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, dtext, duration)
                toast.show()
                goToHome(true)
            } else {
                val dtext = "Connexion refusée"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, dtext, duration)
                toast.show()
            }
        }
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            reponse,
            Response.ErrorListener {
                val dtext = "Impossible d'accéder au service d'authentification! Veuillez réessayer plus tard."
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, dtext, duration)
                toast.show()
            })
        queue.add(stringRequest)
    }
}
