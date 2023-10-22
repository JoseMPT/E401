package mx.tecnm.cdhidalgo.e401

import adapters.ArtsAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import entities.ProductData

class Store : AppCompatActivity() {
    private lateinit var userText : TextView
    private lateinit var btnSignOut : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerViewArt: RecyclerView
    private lateinit var listArts: ArrayList<ProductData>
    private lateinit var adapterArt: ArtsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        userText = findViewById(R.id.user_store)
        btnSignOut = findViewById(R.id.sign_out)

        auth = Firebase.auth
        firestore = Firebase.firestore

        recyclerViewArt = findViewById(R.id.products_list)

        listArts = ArrayList()

        listArts.add(
            ProductData(
            R.drawable.artesania01,
            "Xico Hoja",
            "Xico-Talavera Hoja",
            3309.00,
            "XICO es un personaje que busca generar un cambio positivo a través" +
                    " del arte y la cultura. Respalda el talento emergente y provee una " +
                    "plataforma comercial de impulso creativo.",
            "artesania")
        )
        listArts.add(
            ProductData(
            R.drawable.artesania02,
            "Xico Rojo",
            "Xico-Piel de Alebrije Rojo",
            2959.00,
            "XICO es un personaje que busca generar un cambio positivo a través" +
                    " del arte y la cultura. Respalda el talento emergente y provee una " +
                    "plataforma comercial de impulso creativo.",
            "artesania")
        )
        listArts.add(
            ProductData(
            R.drawable.artesania03,
            "Alebrije Liebre",
            "Alebrije Liebre Chico Azul/Rojo",
            1180.00,
            "Los alebrijes surgen de un sueño del artesano don Pedro " +
                    "Linares: \"Me morí y anduve por montañas, barrancas y en un lugar" +
                    " de ésos, donde había un bosque, de ahí salían los alebrijes\"... " +
                    "Forma y color se integran armoniosa e intensamente. Bello dentro " +
                    "de su fealdad, diabólico, horroroso, simpático e incluso tierno, " +
                    "el alebrije es símbolo del monstruo mexicano. Revoloteo incesante, " +
                    "remolino de garras, crestas y cuernos de colores vibrantes. Cuanto " +
                    "más extraña sea la figura, más alebrije será.",
            "artesania")
        )
        listArts.add(
            ProductData(
            R.drawable.artesania04,
            "Alebrije Camaleón",
            "Alebrije Camaleón Mediano",
            5189.00,
            "Los alebrijes surgen de un sueño del artesano don Pedro " +
                    "Linares: \"Me morí y anduve por montañas, barrancas y en un lugar" +
                    " de ésos, donde había un bosque, de ahí salían los alebrijes\"... " +
                    "Forma y color se integran armoniosa e intensamente. Bello dentro " +
                    "de su fealdad, diabólico, horroroso, simpático e incluso tierno, " +
                    "el alebrije es símbolo del monstruo mexicano. Revoloteo incesante, " +
                    "remolino de garras, crestas y cuernos de colores vibrantes. Cuanto " +
                    "más extraña sea la figura, más alebrije será.",
            "artesania")
        )
        listArts.add(
            ProductData(
            R.drawable.artesania05,
            "Catrina Rosa",
            "Catrina Arrecifes Rosa",
            4540.00,
            "Pieza artesanal con la figura de una catrina cuyo elegante" +
                    " vestir plasma las tradiciones de la cultura mexicana. Diseño " +
                    "Inspirado en la belleza y riqueza de los arrecifes mexicanos," +
                    " los cuales florecen en aguas tropicales siendo el hogar y refugio" +
                    " de diversas especies. Por su gran biodiversidad, se han ganado " +
                    "el título de Selvas del mar. Los arrecifes, fortaleza multicolor" +
                    " que resguarda el equilibrio de la naturaleza, forman diseños " +
                    "orgánicos y exóticos que nos invitan a preservarlos y sumergirnos" +
                    " en la profundidad del mar.",
            "artesania")
        )

        adapterArt = ArtsAdapter(listArts) {
            goToProductDetails(it)
        }

        recyclerViewArt.layoutManager = LinearLayoutManager(this)
        recyclerViewArt.adapter = adapterArt

    }

    private fun goToProductDetails(data: ProductData){
        val intent = Intent(this, ActivityProductDetails::class.java)
        intent.putExtra("product", data)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        /*userText.text = String.format("Welcome %s %s %s!",
            userData.name, userData.lastname1, userData.lastname2
        ).trim()*/
        userText.text = String.format("Welcome %s!",
            (auth.currentUser?.displayName ?: auth.currentUser?.email ?: "User")
        ).trim()

        btnSignOut.setOnClickListener {
            signOutUser()
        }
    }

    private fun signOutUser(){
        auth.signOut()
        firestore.clearPersistence()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}