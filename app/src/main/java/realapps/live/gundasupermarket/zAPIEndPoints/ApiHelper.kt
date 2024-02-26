package realapps.live.gundasupermarket.zAPIEndPoints

import okhttp3.MultipartBody
import realapps.live.gundasupermarket.homeModule.modalClass.AddProductResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetProductsResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetWareHouseResponse
import realapps.live.gundasupermarket.homeModule.modalClass.RequestStockResponse
import realapps.live.gundasupermarket.stockRequestModule.modalClass.GetStockRequestsResponse

interface ApiHelper {

    suspend fun getProductsList(): GetProductsResponse

    suspend fun getWareHouseList(): GetWareHouseResponse

    suspend fun getStockRequestList(): GetStockRequestsResponse


    suspend fun postStockRequest(productId: Int, wareHouseId: Int): RequestStockResponse

    suspend fun postAddProduct(photo: MultipartBody.Part): AddProductResponse


}