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
import entities.ProductData
import mx.tecnm.cdhidalgo.e401.R

class ArtsAdapter (private val productsList : ArrayList<ProductData>, private val activity: Activity,private val onProductClick: (ProductData) -> Unit) : RecyclerView.Adapter<ArtsAdapter.ProductViewHolder>(){
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val photo : ImageView = itemView.findViewById(R.id.product_image)
        val name : TextView = itemView.findViewById(R.id.product_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_card, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productsList[position]
        val storage = Firebase.storage.reference.child(product.urlImage!!)
        //holder.photo.setImageResource(product.image)
        storage.downloadUrl.addOnSuccessListener {
            Glide.with(activity).load(it).into(holder.photo)
        }

        holder.name.text = product.smallName

        holder.itemView.setOnClickListener {
            onProductClick.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

}