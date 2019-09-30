package com.example.catoumatoutrue

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewNomPage.text = "Contacts"
        json()
    }

    private fun json() {
        @SuppressLint("StaticFieldLeak")
        object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String {
                val url = "http://formation.devatom.net/UDEV2/ProjetFilRouge/JSON/exploded/netgest_personne.json"
                var requete = Request.Builder().url(url).build()
                val reponse = OkHttpClient().newCall(requete).execute()
                println (reponse)
                return reponse.body.toString()
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
            }
        }
    }
}
