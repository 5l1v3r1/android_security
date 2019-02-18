package com.desrumaux.androidtoolbox.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.desrumaux.androidtoolbox.R
import com.desrumaux.androidtoolbox.keystore.GlobalCryptor
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        lifecycleImage.setOnClickListener {
            goToActivity(LifeCycleActivity::class.java)
        }

        saveImage.setOnClickListener {
            goToActivity(UserInfoActivity::class.java)
        }

        permissionsImage.setOnClickListener {
            goToActivity(InfoActivity::class.java)
        }

        webservicesImage.setOnClickListener {
            goToActivity(WebServiceActivity::class.java)
        }

        disconnectButton.setOnClickListener {
            resetPrefs()
            goBackToLogin()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        //exampleKeyStore()
        getToken()
    }

    private fun goBackToLogin() {
        val intent = Intent(this@HomeActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun resetPrefs() {
        val sharedPreferences = getSharedPreferences(LoginActivity.USER_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ID", "")
        editor.putString("PW", "")
        editor.apply()
    }

    private fun goToActivity(destClass: Class<*>) {
        startActivity(Intent(this, destClass))
    }

    private fun getToken() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://10.211.55.5/?token=yes"
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->
                val text = response
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            },
            Response.ErrorListener {
                val text = "Impossible d'accéder au service !"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            })
        queue.add(stringRequest)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun exampleKeyStore() {
        val keyStore = GlobalCryptor()

        keyStore.encryptText("key-thibaut", "feffefefef")
        val decreptedText = keyStore.decryptText("key-thibaut")

        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, decreptedText, duration)
        toast.show()
    }
}
