package realapps.live.gundasupermarket.homeModule.modalClass

import com.google.gson.annotations.SerializedName


data class GetWareHouseResponse (
    @SerializedName("status")
    val code: Int = -1,
    @SerializedName("data")
    val wareHouseList: ArrayList<WareHouseData> =ArrayList()
)