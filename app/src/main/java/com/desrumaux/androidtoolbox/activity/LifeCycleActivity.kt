package com.desrumaux.androidtoolbox.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.desrumaux.androidtoolbox.R
import com.desrumaux.androidtoolbox.fragment.CoucouFragment
import com.desrumaux.androidtoolbox.fragment.CycleFragment
import kotlinx.android.synthetic.main.activity_life_cycle.*
import java.util.logging.Logger


class LifeCycleActivity : AppCompatActivity() {

    val LOG = Logger.getLogger(LifeCycleActivity::class.java.name)

    var isActivityRunning = false

    private lateinit var fragment: Fragment
    private lateinit var fragment2: Fragment
    private lateinit var fragmentManager: FragmentManager
    private var frag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)
        isActivityRunning = true
        showLog("Création de l'app")

        fragment = CycleFragment()
        fragment2 = CoucouFragment()

        fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameFragment, fragment)
        transaction.commit()

        changeFragment.setOnClickListener {
            changerFragment()
        }
    }

    private fun changerFragment() {
        frag = !frag
        val transaction = fragmentManager.beginTransaction()
        if (frag) {
            transaction.replace(R.id.frameFragment, fragment2)
        } else {
            transaction.replace(R.id.frameFragment, fragment)
        }
        transaction.commit()
    }

    fun display_toast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        showLog("Démarrage de l'activité")
    }

    override fun onResume() {
        super.onResume()
        showLog("Reprise de l'activité")
    }

    override fun onPause() {
        super.onPause()
        isActivityRunning = false
        showLog("Activité mise en pause")
    }

    override fun onStop() {
        super.onStop()
        showLog("Activité arrêtée")
    }

    override fun onDestroy() {
        super.onDestroy()
        showLog("Activité détruite")
    }

    override fun onRestart() {
        super.onRestart()
        showLog("Activitée redémarrée")
    }

    private fun showLog(msg: String) {
        if (isActivityRunning) {
            val newMessage = stateText.text.toString() + msg + "\n"
            stateText.text = newMessage
        } else {
            LOG.info(msg)
        }
    }
}