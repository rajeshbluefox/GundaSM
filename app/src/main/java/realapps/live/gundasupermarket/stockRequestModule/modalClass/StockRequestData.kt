package realapps.live.gundasupermarket.stockRequestModule.modalClass

import com.google.gson.annotations.SerializedName


data class StockRequestData(
    @SerializedName("RequestId")
    var RequestId: Int = -1,
    @SerializedName("ProductName")
    var ProductName: String = "",
    @SerializedName("RequestDate")
    var RequestDate: String = "",
    @SerializedName("RequestStatus")
    var RequestStatus: Int = -1,
    @SerializedName("WareHouseId")
    var WareHouseId: Int = -1,
)