package realapps.live.gundasupermarket.homeModule.modalClass

import com.google.gson.annotations.SerializedName


data class Product(
    @SerializedName("ProductId")
    var ProductId: Int = -1,
    @SerializedName("ProductCode")
    var ProductCode: String = "",
    @SerializedName("ProductName")
    var ProductName: String = "" ,
    @SerializedName("ProductPrice")
    var ProductPrice: String = "",
    @SerializedName("TotalMRP")
    var TotalMRP: Int = -1,
    @SerializedName("ProductImage")
    var Photo: String = "",
    @SerializedName("WareHouseId")
    var wareHouseId : Int = -1
)