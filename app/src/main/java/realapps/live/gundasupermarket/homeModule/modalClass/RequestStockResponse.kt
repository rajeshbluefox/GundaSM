package realapps.live.gundasupermarket.homeModule.modalClass

import com.google.gson.annotations.SerializedName


data class RequestStockResponse(
    @SerializedName("status")
    val code: Int = -1,
    @SerializedName("message")
    val message: String = "",
)