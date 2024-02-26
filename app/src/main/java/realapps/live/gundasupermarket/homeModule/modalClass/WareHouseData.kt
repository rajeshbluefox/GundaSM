package realapps.live.gundasupermarket.homeModule.modalClass

import com.google.gson.annotations.SerializedName


data class WareHouseData(
    @SerializedName("WareHouseId")
    var wareHouseID: Int = -1,
    @SerializedName("WareHouseName")
    var wareHouseName: String = ""
)