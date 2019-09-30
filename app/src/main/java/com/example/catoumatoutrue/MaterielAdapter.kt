package com.example.catoumatoutrue

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.catoumatoutrue.modele.Materiel
import kotlinx.android.synthetic.main.material_card.view.*

//import kotlinx.android.synthetic.main.video_row.view.*

class MaterielAdapter(val liste: List<Materiel>): RecyclerView.Adapter<CustomViewHolder>(){



    //number fo items
    override fun getItemCount(): Int {
        return liste.size

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0?.context)
        val cellForRow = layoutInflater.inflate(R.layout.material_card, p0, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, p1: Int) {
        val item = liste.get(p1)
        holder?.view?.textViewTitreMateriel?.text = item.libelle
    }
}

class CustomViewHolder(val view: View):RecyclerView.ViewHolder(view){

}