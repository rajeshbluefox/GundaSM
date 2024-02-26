package realapps.live.gundasupermarket.homeModule.modalClass

import com.google.gson.annotations.SerializedName


data class AddProductResponse (
    @SerializedName("status")
    val code: String = "",
    @SerializedName("message")
    val message: String = "",

)