package mx.tecnm.cdhidalgo.e401

import adapters.CartAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import entities.Shopping

var listCart : ArrayList<Shopping> = ArrayList()
class ShoppingCart : AppCompatActivity() {
    private lateinit var btnBackToStore: ImageButton
    private lateinit var btnPurchased: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartAdapter
    private lateinit var total: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)

        btnBackToStore = findViewById(R.id.btn_back_details)
        btnPurchased = findViewById(R.id.btn_purchased)
        recyclerView = findViewById(R.id.cart_products)
        total = findViewById(R.id.total_cart)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(listCart, this)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        btnBackToStore.setOnClickListener {
            finish()
        }
    }
}