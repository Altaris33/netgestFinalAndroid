package com.example.catoumatoutrue

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.catoumatoutrue.modele.Client
import com.example.catoumatoutrue.modele.Repertoire
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_client.*
import okhttp3.*
import java.io.IOException

class ClientActivity : AppCompatActivity() {

    var URL : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        val idClient = intent.getIntExtra("ClientID", 0)
        val nomClient = intent.getStringExtra("ClientName")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)
        URL = "http://localhost:8080/resoapi/api/client/$idClient"
        imageViewContacts.setOnClickListener {
            val int = Intent(this, ContactActivity::class.java)
            int.putExtra("ClientID", idClient)
            int.putExtra("ClientName", nomClient)
            this.startActivity(int)
        }

        imageViewMateriel.setOnClickListener {
            val int = Intent(this, MaterielActivity::class.java)
            int.putExtra("ClientID", idClient)
            int.putExtra("ClientName", nomClient)
            this.startActivity(int)
        }

        fetchJson()
    }




    fun fetchJson(){
        //On essaie de récupérer le Json depuis l'URL
        val request = Request.Builder().url(URL).build()
        val okClient = OkHttpClient()
        okClient.newCall(request).enqueue(object : Callback{

            override fun onFailure(call: Call, e: IOException) {
                println("Echec de la requete")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)
                val gson = GsonBuilder().create()
                val client = gson.fromJson(body, Client::class.java)

                runOnUiThread {
                    textViewNom.text = client.nom
                    textViewAdress.text = client.adresse1
                    textViewCompAdresse.text = client.adresse2
                }


            }
        })
    }
}
