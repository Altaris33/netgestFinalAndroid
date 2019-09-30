package com.example.catoumatoutrue

import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.example.catoumatoutrue.modele.Inventaire
import com.example.catoumatoutrue.modele.Materiel
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_materiel.*
import kotlinx.android.synthetic.main.ajout_materiel.*
import okhttp3.*
import java.io.IOException


class MaterielActivity : AppCompatActivity() {

    //  =====> Gestion de l'affichage des données concernant les catégories
    private val categories =  arrayOf("Tous","Imprimante", "Switch", "Routeur", "Serveur", "Ordinateur")
    private var choixSpinner : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materiel)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

        //on remplit le spinner avec nos catégories
        spinnerdown.adapter = adapter
        spinnerdown.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                choixSpinner = position
                val text : String = if (position == 0){
                    "Matériels de ${intent.getStringExtra("ClientName")}"
                } else {
                    "${parent.getItemAtPosition(position)}s de ${intent.getStringExtra("ClientName")}"
                }
                textViewTitreMateriel.text = text
                fetchJSON()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        // Gestion de l'événement sur le clique de l'image avec un INFLATER (ajout d'une vue XML seule)
        ajoutmateriel.setOnClickListener {
            //instanciating the XML view
            val inflater:LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.ajout_materiel,null)

            //new instance of Popup Window Object
            val popupWindow = PopupWindow(
                view,
                LinearLayout.LayoutParams.WRAP_CONTENT,         //width of pop up window
                LinearLayout.LayoutParams.WRAP_CONTENT          // window height

            )

            //setting the elevation for the popup window
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                popupWindow.elevation = 10.0F
            }

            //Si on utilise une API 23 ou plus récente
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                //on crée un nouveau slide animation pour la popup avec le cardinal de départ
                val slideIn =  Slide()
                slideIn.slideEdge = Gravity.TOP
                popupWindow.enterTransition = slideIn

                val slideOut = Slide()
                slideOut.slideEdge = Gravity.RIGHT
                popupWindow.enterTransition = slideOut
            }

            //on crée une variable pour faire coincider le type de Widget pour le textView
            val tv = view.findViewById<TextView>(R.id.text_pop)
            val btn_pop = view.findViewById<Button>(R.id.button_cancel)

            //Utilisation d'un lambda car Kotlin bind automatiquement les id passés en XML
            tv.setOnClickListener{
                //textPop.(Color.RED)
            }

            //Fermer la fenêtre sous le clique d'un bouton (ajout_materiel.xml)
            btn_pop.setOnClickListener{
                popupWindow.dismiss()

            }

            //Gestion de l'événement sur la fermeture avec un dismissListener
            popupWindow.setOnDismissListener {
                Toast.makeText(applicationContext, "Modification(s) annulée(s)", Toast.LENGTH_SHORT).show()

            }

            //class TransitionManager qui va gérer l'affichage, apparaition et positionnement de la popup
            TransitionManager.beginDelayedTransition(linearLayout2)
            popupWindow.showAtLocation(
                linearLayout2,
                Gravity.CENTER,
                0,                //gestion des abscisses et ordonnées
                0
            )
        }

        /*button_apply.setOnClickListener {

        }*/

        //appel de la fonction de test de validation de modification de matériel des clients
        handleGetText()

        fetchJSON()

        recyclerView_main.layoutManager = LinearLayoutManager(this)

    }

    // ====> fonction qui gère les entrées utilisateurs
    fun handleGetText(){
        val gettingUserName = findViewById<EditText>(R.id.name_r)
        val gettingUserAdress = findViewById<EditText>(R.id.adresse)
        val gettingUserTypeM = findViewById<EditText>(R.id.type_m)

        //sauvegarder et afficher en rafraichissant la page directement après modification de
        //l'utilisateur
        /*gettingUserName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Toast.makeText(MaterielActivity@this, "The name is $name_r", Toast.LENGTH_SHORT).show()
                gettingUserName.text = Editable.Factory.getInstance().newEditable("The name is $name_r")

            }
        })*/
        if(gettingUserName != null && gettingUserAdress != null && gettingUserTypeM != null){
            Toast.makeText(this, gettingUserName.text, Toast.LENGTH_SHORT).show()
        }


    }



    // ====> Récupérer les données au format JSON et les afficher sur l'écran Matériel Activité
    //Les données intéressées sont l'id du client et le type de matériel
    // Les données JSON seront parsées et mappées avec les classes Kotlin
    // Les classes permettrons ensuite d'obtenir une vue des données via un Recycler View
    fun fetchJSON(){

        val jsonUrl = "http://localhost:8080/resoapi/api/client/materiel"
        val request = Request.Builder().url(jsonUrl).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body="{\"materiel\":" + response.body?.string() + "}"
                val gson = GsonBuilder().create()
                val listMateriels = gson.fromJson(body, Inventaire::class.java)
                val listMateriel : List<Materiel>
                listMateriel = if(choixSpinner == 0) {
                    listMateriels.materiel.filter { it.idclient == intent.getIntExtra("ClientID", 0)}
                } else {
                    listMateriels.materiel.filter { it.idclient == intent.getIntExtra("ClientID", 0) && it.idtype == choixSpinner}
                }
                runOnUiThread {
                   recyclerView_main.adapter = MaterielAdapter(listMateriel)
                }
            }
        
            override fun onFailure(call: Call, e: IOException) {
                println("Echec lors de la récupération des données JSON")
            }
        })


    }




}
