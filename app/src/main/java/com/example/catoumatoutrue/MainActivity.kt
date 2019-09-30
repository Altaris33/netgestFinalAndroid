package com.example.catoumatoutrue

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.example.catoumatoutrue.modele.Repertoire
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private val jsonURL = "http://192.168.8.45:8080/resoapi/api/client"
    var rep : Repertoire = Repertoire(ArrayList())
    private val clientAdapter = ClientAdapter(rep.clients, this)
    var reponse = "KO..."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rv_client_liste.adapter = clientAdapter
        rv_client_liste.layoutManager = LinearLayoutManager(this)
        fetchJSON()

        btnAjoutClient.setOnClickListener {
            this.startActivity(Intent(this, AjoutClientActivity::class.java))
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun fetchJSON() {
        println("On rentre dans la méthode fetch")
        object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(params: Array<Void>): String {
                println("On rentre dans la méthode do in background")
                val request = Request.Builder().url(jsonURL).build()
                val okClient = OkHttpClient()
                val response: Response = okClient.newCall(request).execute()
                val body = "{\"clients\":" + response.body?.string() + "}"
                val gson = GsonBuilder().create()
                rep.clients.addAll(gson.fromJson(body, Repertoire::class.java).clients)
                if(response.isSuccessful){
                    reponse = "OK !!!"
                }
                println("Réponse : $reponse")
                return reponse
            }

            override fun onPostExecute(result: String) {
                if(reponse == "OK !!!"){
                    updateView()
                }
            }
        }.execute()
    }

    fun updateView() {
        if(rep.clients.isNotEmpty()) {
            println("On notifie l'adapter que les données ont changé")
            clientAdapter.notifyDataSetChanged()
        }
        else {
            Toast.makeText(this@MainActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()

        }
    }
}