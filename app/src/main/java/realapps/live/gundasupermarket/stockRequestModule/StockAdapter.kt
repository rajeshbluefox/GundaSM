package realapps.live.gundasupermarket.stockRequestModule

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import realapps.live.gundasupermarket.databinding.ItemOrderFormNewBinding
import realapps.live.gundasupermarket.stockRequestModule.modalClass.StockRequestData


class StockAdapter(
    private var stockRequestList: ArrayList<StockRequestData>,
    private var context: Context,
    private val listener: (stockRequestData: StockRequestData) -> Unit
) :
    RecyclerView.Adapter<StockAdapter.StockAdapterViewHolder>() {


    class StockAdapterViewHolder(var binding: ItemOrderFormNewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockAdapterViewHolder {
        return StockAdapterViewHolder(
            ItemOrderFormNewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: StockAdapterViewHolder, position: Int) {

        val stockRequestData = stockRequestList[position]

        holder.binding.productName.text = stockRequestData.ProductName
        holder.binding.productPrice.text = "9999"
        holder.binding.tvRequestDate.text = stockRequestData.RequestDate

        when (stockRequestData.RequestStatus) {
            0 -> {
                holder.binding.tvRequestStatus.text = "Pending"
            }

            1 -> {
                holder.binding.tvRequestStatus.text = "Accepted"
            }

            else -> {
                holder.binding.tvRequestStatus.text = "Rejected"
            }
        }


    }

    override fun getItemCount(): Int {
        return stockRequestList.size
    }

}