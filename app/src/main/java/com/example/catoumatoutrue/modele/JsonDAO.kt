package com.example.catoumatoutrue.modele

import com.example.catoumatoutrue.MainActivity
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class JsonDAO {
    var repertoireJsonClients : Repertoire = Repertoire(ArrayList())

    fun fetchJson() {
        val URL = "http://formation.devatom.net/UDEV2/ProjetFilRouge/JSON/exploded/netgest_client.json"
        val request = Request.Builder().url(URL).build()
        val okClient = OkHttpClient()
        okClient.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                println("Echec de la requete")
            }

            override fun onResponse(call: Call, response: Response) {

                val body = "{\"clients\":" + response.body?.string() + "}"
                val gson = GsonBuilder().create()
                repertoireJsonClients = gson.fromJson(body, Repertoire::class.java)
                println("On passe par là")
            }


        })
    }
}