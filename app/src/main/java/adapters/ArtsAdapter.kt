package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import entities.ProductData
import mx.tecnm.cdhidalgo.e401.R

class ArtsAdapter (private val productsList : ArrayList<ProductData>, private val onProductClick: (ProductData) -> Unit) : RecyclerView.Adapter<ArtsAdapter.ProductViewHolder>(){
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
        holder.photo.setImageResource(product.image)
        holder.name.text = product.smallName

        holder.itemView.setOnClickListener {
            onProductClick.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

}