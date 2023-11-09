package mx.tecnm.cdhidalgo.e401

import adapters.CartAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

        var totalValue = 0.0
        if (listCart.isNotEmpty()){
            for (product in listCart){
                totalValue += product.total!!
            }
        }
        val iva = totalValue * 0.16

        total.text = String.format("Subtotal: $%.2f\nIVA: $%.2f\nTotal: $%.2f", totalValue, iva, totalValue+iva)

        btnPurchased.setOnClickListener {
            val confirmData = AlertDialog.Builder(it.context)
            confirmData.setTitle("Finalizar compra")
            confirmData.setMessage("Productos comprados: ${listCart.size}\nTotal: \$${totalValue+iva}")
            confirmData.setPositiveButton("Confirm"){ _, _ ->
                listCart.clear()
                finish()
            }
            confirmData.setNegativeButton("Cancel"){ _, _ ->}
            if (listCart.size <= 0) {
                Toast.makeText(this, "Add products to cart", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            confirmData.show()
        }
    }

    override fun onStart() {
        super.onStart()
        btnBackToStore.setOnClickListener {
            finish()
        }
    }
}