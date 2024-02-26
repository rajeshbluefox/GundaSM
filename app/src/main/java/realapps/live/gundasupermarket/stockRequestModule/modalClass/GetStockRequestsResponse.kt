package realapps.live.gundasupermarket.stockRequestModule.modalClass

import com.google.gson.annotations.SerializedName


data class GetStockRequestsResponse (
    @SerializedName("status")
    val code: Int = -1,
    @SerializedName("data")
    val stockRequestList: ArrayList<StockRequestData> =ArrayList()
)