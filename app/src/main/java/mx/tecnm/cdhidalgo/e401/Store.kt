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
import com.google.firebase.storage.ktx.storage
import entities.ProductData

class Store : AppCompatActivity() {
    private lateinit var userText : TextView
    private lateinit var btnSignOut : Button
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var recyclerViewArt: RecyclerView
    private lateinit var listArts: ArrayList<ProductData>

    //private lateinit var listImages: ArrayList<Images>

    private lateinit var adapterArt: ArtsAdapter
    //private lateinit var fireStorage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        userText = findViewById(R.id.user_store)
        btnSignOut = findViewById(R.id.sign_out)

        auth = Firebase.auth
        firestore = Firebase.firestore

        recyclerViewArt = findViewById(R.id.products_list)

        listArts = ArrayList()

        firestore.collection("arts").get().addOnSuccessListener {
            for (doc in it.documents){
                val productData = doc.toObject(ProductData::class.java)
                listArts.add(productData!!)
            }
            adapterArt = ArtsAdapter(listArts, this) {data ->
                goToProductDetails(data)
            }

            recyclerViewArt.layoutManager = LinearLayoutManager(this)
            recyclerViewArt.adapter = adapterArt
        }


        /*Firestorage*/
        /*fireStorage = Firebase.storage
        val storage = fireStorage.reference
        val pathArts = storage.child("Arts")
        pathArts.listAll().addOnSuccessListener {
            for (item in it.items) {
                val img = Images(item.path, "", "", 0.0, "", "artesania")
                firestore.collection("arts").add(img)
            }
        }*/
        val storageReference = Firebase.storage.reference



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