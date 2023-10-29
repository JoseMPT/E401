package adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import entities.Shopping
import mx.tecnm.cdhidalgo.e401.R

class CartAdapter(private val listCart : ArrayList<Shopping>, private val activity: Activity) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val photo : ImageView = itemView.findViewById(R.id.image_cart)
        val name : TextView = itemView.findViewById(R.id.name_cart)
        val price : TextView = itemView.findViewById(R.id.price_cart)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = listCart[position]

        //holder.photo.setImageResource(R.drawable.default_product)
        val storage = Firebase.storage.reference.child(product.urlImage!!)
        storage.downloadUrl.addOnSuccessListener {
            Glide.with(activity).load(it).into(holder.photo)
        }
        holder.name.text = product.name
        holder.price.text = String.format("$%.2f ", product.total)
    }
}