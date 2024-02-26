package realapps.live.gundasupermarket.homeModule

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import realapps.live.gundasupermarket.databinding.ItemOrderFormBinding
import realapps.live.gundasupermarket.homeModule.modalClass.Product


class ProductsAdapter(
    private var productsList: ArrayList<Product>,
    private var context: Context,
    private val listener: (product: Product) -> Unit
) :
    RecyclerView.Adapter<ProductsAdapter.ProductsAdapterViewHolder>() {


    class ProductsAdapterViewHolder(var binding: ItemOrderFormBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsAdapterViewHolder {
        return ProductsAdapterViewHolder(
            ItemOrderFormBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ProductsAdapterViewHolder, position: Int) {

        val product = productsList[position]

        holder.binding.productName.text = product.ProductName
        holder.binding.productPrice.text = product.ProductPrice

        Glide.with(context)
            .load(product.Photo)
            .fitCenter()
            .into(holder.binding.productImage)

        if((position+1)%2!=0)
        {
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(0, 0, 6, 6)
        holder.binding.productLt.layoutParams = layoutParams
        }else{
            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            layoutParams.setMargins(6, 0, 0, 6)
            holder.binding.productLt.layoutParams = layoutParams
        }

        holder.binding.btSelect.setOnClickListener {
            listener.invoke(product)
        }

    }

    override fun getItemCount(): Int {
        return productsList.size
    }

}