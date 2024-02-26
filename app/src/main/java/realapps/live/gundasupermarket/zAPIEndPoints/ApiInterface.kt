package realapps.live.gundasupermarket.zAPIEndPoints

import okhttp3.Call
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import realapps.live.gundasupermarket.homeModule.modalClass.AddProductResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetProductsResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetWareHouseResponse
import realapps.live.gundasupermarket.homeModule.modalClass.RequestStockResponse
import realapps.live.gundasupermarket.stockRequestModule.modalClass.GetStockRequestsResponse
import retrofit2.http.*

interface ApiInterface {

    @GET("Products_get.php")
    suspend fun getProductList(): GetProductsResponse

    @GET("Ware_Houses_get.php")
    suspend fun getWareHouseList(): GetWareHouseResponse

    @GET("Requests_get.php")
    suspend fun getStockRequests(): GetStockRequestsResponse

    @FormUrlEncoded
    @POST("Requests_post.php")
    suspend fun postStockRequest(
        @Field("ProductId") productId: Int,
        @Field("RequestDate") requestDate: String,
        @Field("RequestStatus") requestStatus: Int,
        @Field("WareHouseId") wareHouseId: Int,
    ): RequestStockResponse

    @Multipart
    @POST("insert_image.php")
    suspend fun postAddProduct(
        @Part("ProductName") productName: RequestBody,
        @Part("ProductCode") productCode: RequestBody,
        @Part("ProductPrice") productPrice: RequestBody,
        @Part("WareHouseId") wareHouseId: RequestBody,
        @Part productImage: MultipartBody.Part
    ): AddProductResponse


}