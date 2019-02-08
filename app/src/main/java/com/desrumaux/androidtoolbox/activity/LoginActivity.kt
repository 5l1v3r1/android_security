package com.desrumaux.androidtoolbox.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.desrumaux.androidtoolbox.R
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

        if (isCredentialsOK(savedId, savedPassword)) {
            toastInfo(getString(R.string.login_success_sharedpref))
            goToHome(true)
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
            if (isCredentialsOK(emailInput.text.toString(), passwordInput.text.toString())) {
                saveUserPref(emailInput.text.toString(), passwordInput.text.toString())
                emailInput.text?.clear()
                passwordInput.text?.clear()
                toastInfo(getString(R.string.login_success))
                goToHome(true)
            } else {
                toastInfo(getString(R.string.login_error_wrongValues))
            }
        }
    }

    private fun isCredentialsOK(id: String, password: String) =
        id == "admin" && password == "123"

    private fun toastInfo(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun goToHome(clearCache: Boolean) {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        if (clearCache) intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        if (clearCache) finish()
    }
}
