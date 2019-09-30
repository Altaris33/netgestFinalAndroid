package com.example.catoumatoutrue

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.catoumatoutrue.modele.Client
import kotlinx.android.synthetic.main.activity_card.view.*

class ClientAdapter(var liste : ArrayList<Client>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemCount(): Int {
        println("la liste contient ${liste.size} éléments")
        return liste.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvClientId?.text = liste[position].id.toString()
        holder.tvClientNom?.text = liste[position].nom
        holder.itemView.setOnClickListener {
            val intent = Intent(context,ClientActivity::class.java)
            intent.putExtra("ClientID", liste[position].id)
            intent.putExtra("ClientName", liste[position].nom)
            context.startActivity(intent)
        }

    }
}
class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val tvClientId = view.textViewIdClient
    val tvClientNom = view.textViewNomClient
}