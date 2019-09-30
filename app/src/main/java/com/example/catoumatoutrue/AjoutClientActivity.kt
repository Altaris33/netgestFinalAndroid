package com.example.catoumatoutrue

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_ajout_client.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


class AjoutClientActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajout_client)

        btnInscription.setOnClickListener {
            val client = JsonObject()
            client.addProperty("nom", plainTextName.text.toString())
            client.addProperty("adresse1", plainTextAdress.text.toString())
            client.addProperty("adresse2", plainTextCompAdress.text.toString())
            client.addProperty("idcpville", Integer.parseInt(plainTextCodePostal.text.toString()))
            println(client)
            val url = "http://localhost:8080/resoapi/api/client/ajout"
            val body = client.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
            val cl = OkHttpClient()
            val reponse = cl.newCall(request).execute()
            println(reponse.headers)


        }


    }
}
