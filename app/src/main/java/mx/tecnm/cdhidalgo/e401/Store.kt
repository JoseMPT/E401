package mx.tecnm.cdhidalgo.e401

import adapters.ProductsAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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
    private lateinit var recyclerViewBag: RecyclerView
    private lateinit var recyclerViewFragrances: RecyclerView
    private lateinit var recyclerViewProduct: RecyclerView
    private lateinit var recyclerViewWristbands: RecyclerView

    private lateinit var listArts: ArrayList<ProductData>
    private lateinit var listBags: ArrayList<ProductData>
    private lateinit var listFragrances: ArrayList<ProductData>
    private lateinit var listProduct: ArrayList<ProductData>
    private lateinit var listWristbands: ArrayList<ProductData>

    //private lateinit var listImages: ArrayList<Images>

    private lateinit var adapterArt: ProductsAdapter
    private lateinit var adapterBag: ProductsAdapter
    private lateinit var adapterFragrance: ProductsAdapter
    private lateinit var adapterProduct: ProductsAdapter
    private lateinit var adapterWristband: ProductsAdapter
    //private lateinit var fireStorage: FirebaseStorage

    private lateinit var btnCart: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        userText = findViewById(R.id.user_store)
        btnSignOut = findViewById(R.id.sign_out)
        btnCart = findViewById(R.id.btn_cart_store)

        auth = Firebase.auth
        firestore = Firebase.firestore

        recyclerViewArt = findViewById(R.id.arts_list)
        recyclerViewBag = findViewById(R.id.bags_list)
        recyclerViewFragrances = findViewById(R.id.fragrances_list)
        recyclerViewProduct = findViewById(R.id.product_list)
        recyclerViewWristbands = findViewById(R.id.wristbands_list)

        listArts = ArrayList()
        listBags = ArrayList()
        listFragrances = ArrayList()
        listProduct = ArrayList()
        listWristbands = ArrayList()

        firestore.collection("arts").get().addOnSuccessListener {
            for (doc in it.documents){
                val productData = doc.toObject(ProductData::class.java)
                listArts.add(productData!!)
            }
            adapterArt = ProductsAdapter(listArts, this) { data ->
                goToProductDetails(data)
            }
            recyclerViewArt.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewArt.adapter = adapterArt
        }

        firestore.collection("bags").get().addOnSuccessListener {
            for (doc in it.documents){
                val productData = doc.toObject(ProductData::class.java)
                listBags.add(productData!!)
            }
            adapterBag = ProductsAdapter(listBags, this) { data ->
                goToProductDetails(data)
            }
            recyclerViewBag.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
            recyclerViewBag.adapter = adapterBag
        }

        firestore.collection("fragrances").get().addOnSuccessListener {
            for (doc in it.documents){
                val productData = doc.toObject(ProductData::class.java)
                listFragrances.add(productData!!)
            }
            adapterFragrance = ProductsAdapter(listFragrances, this) { data ->
                goToProductDetails(data)
            }
            recyclerViewFragrances.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewFragrances.adapter = adapterFragrance
        }

        firestore.collection("wristbands").get().addOnSuccessListener {
            for (doc in it.documents){
                val productData = doc.toObject(ProductData::class.java)
                listWristbands.add(productData!!)
            }
            adapterWristband = ProductsAdapter(listWristbands, this) { data ->
                goToProductDetails(data)
            }
            recyclerViewWristbands.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewWristbands.adapter = adapterWristband
        }

        firestore.collection("products").get().addOnSuccessListener {
            for (doc in it.documents){
                val productData = doc.toObject(ProductData::class.java)
                listProduct.add(productData!!)
            }
            adapterProduct = ProductsAdapter(listProduct, this){ data ->
                goToProductDetails(data)
            }
            recyclerViewProduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewProduct.adapter = adapterProduct
        }
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

        btnCart.setOnClickListener {
            val intent = Intent(this, ShoppingCart::class.java)
            startActivity(intent)
        }

        btnSignOut.setOnClickListener {
            signOutUser()
        }
    }

    private fun signOutUser(){
        auth.signOut()
        firestore.clearPersistence()
        listCart.clear()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }
}