package com.desrumaux.androidtoolbox.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.desrumaux.androidtoolbox.R
import com.desrumaux.androidtoolbox.adapter.UserAdapter
import com.desrumaux.androidtoolbox.model.api.RandomUser
import com.desrumaux.androidtoolbox.model.api.Result
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_web_service.*
import org.json.JSONObject


class WebServiceActivity : AppCompatActivity() {

    companion object {
        const val API_URL = "https://randomuser.me/api/?results="
    }

    private lateinit var gsonBuilder: GsonBuilder
    private lateinit var gson: Gson
    private lateinit var queue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.desrumaux.androidtoolbox.R.layout.activity_web_service)

        gsonBuilder = GsonBuilder()
        gson = gsonBuilder.create()

        queue = Volley.newRequestQueue(this)

        progressbar.visibility = View.VISIBLE

        getRandomUser(50)
    }

    private fun displayUsers(results: List<Result>) {
        random_users_list.apply {
            layoutManager = LinearLayoutManager(this@WebServiceActivity)
            adapter = UserAdapter(results, this@WebServiceActivity)
        }
    }

    private fun getRandomUser(number: Int) {
        val stringRequest = StringRequest(
            Request.Method.GET, API_URL + number.toString(),
            Response.Listener<String> { response ->
                val jsonObject = JSONObject(response)
                val randomUsers = gson.fromJson(jsonObject.toString(), RandomUser::class.java) as RandomUser
                randomUsers.results?.let { displayUsers(it) }
                progressbar.visibility = View.INVISIBLE
            },
            Response.ErrorListener {
                val error = getString(R.string.web_error_message)
                Log.d(WebServiceActivity::class.java.name, error)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            })
        queue.add(stringRequest)
    }
}
