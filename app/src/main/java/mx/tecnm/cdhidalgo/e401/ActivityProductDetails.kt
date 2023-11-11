package mx.tecnm.cdhidalgo.e401

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import entities.ProductData
import entities.Shopping

class ActivityProductDetails : AppCompatActivity() {
    private lateinit var btnBackFromDetails: ImageButton
    private lateinit var btnPurchase: Button
    private lateinit var btnCart: ImageButton
    private lateinit var cartCount: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        btnBackFromDetails = findViewById(R.id.btn_back_details)
        btnPurchase = findViewById(R.id.btn_add_to_cart)
        btnCart = findViewById(R.id.btn_cart_details)
        cartCount = findViewById(R.id.cart_count_details)
        cartCount.text = listCart.size.toString()
    }

    override fun onStart() {
        super.onStart()
        btnBackFromDetails.setOnClickListener {
            finish()
        }

        btnCart.setOnClickListener {
            val intent = Intent(this, ShoppingCart::class.java)
            startActivity(intent)
            finish()
        }

        val productDetails = intent.getParcelableExtra<ProductData>("product")

        if(productDetails != null){
            val photo: ImageView = findViewById(R.id.product_image_details)
            val name: TextView = findViewById(R.id.product_name_details)
            val description: TextView = findViewById(R.id.product_description_details)
            val price: TextView = findViewById(R.id.product_price_details)

            val storage = Firebase.storage.reference.child(productDetails.urlImage!!)
            //photo.setImageResource(productDetails.image)
            storage.downloadUrl.addOnSuccessListener {
                Glide.with(this).load(it).into(photo)
                name.text = productDetails.name
                description.text = productDetails.description
                price.text = getString(R.string.price_product, String.format("%.2f", productDetails.price))
            }
        }

        btnPurchase.setOnClickListener {
            listCart.add(Shopping(urlImage = productDetails?.urlImage, name = productDetails?.smallName, total = productDetails?.price, itemCount = 1))
            cartCount.text = listCart.size.toString()
            val intent = Intent(this, ShoppingCart::class.java)
            startActivity(intent)
            finish()
        }
    }
}