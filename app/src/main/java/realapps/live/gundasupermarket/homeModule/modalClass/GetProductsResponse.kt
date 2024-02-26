package realapps.live.gundasupermarket.homeModule.modalClass

import com.google.gson.annotations.SerializedName


data class GetProductsResponse (
    @SerializedName("status")
    val code: Int = -1,
    @SerializedName("data")
    val productsList: ArrayList<Product> =ArrayList()
)