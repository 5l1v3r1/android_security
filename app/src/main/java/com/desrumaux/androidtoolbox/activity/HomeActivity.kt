package com.desrumaux.androidtoolbox.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.desrumaux.androidtoolbox.R
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
}
