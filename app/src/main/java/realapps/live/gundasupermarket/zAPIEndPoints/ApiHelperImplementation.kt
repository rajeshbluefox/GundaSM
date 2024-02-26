package realapps.live.gundasupermarket.zAPIEndPoints

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import realapps.live.gundasupermarket.homeModule.modalClass.AddProductResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetProductsResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetWareHouseResponse
import realapps.live.gundasupermarket.homeModule.modalClass.RequestStockResponse
import realapps.live.gundasupermarket.stockRequestModule.modalClass.GetStockRequestsResponse
import realapps.live.gundasupermarket.zCommonFuntions.UtilFunctions
import javax.inject.Inject


class ApiHelperImplementation @Inject constructor(private val apiService: ApiInterface) :
    ApiHelper {

    override suspend fun getProductsList(): GetProductsResponse {
        return apiService.getProductList()
    }

    override suspend fun getWareHouseList(): GetWareHouseResponse {
        return apiService.getWareHouseList()
    }

    override suspend fun getStockRequestList(): GetStockRequestsResponse {
        return apiService.getStockRequests()
    }

    override suspend fun postStockRequest(productId: Int, wareHouseId: Int): RequestStockResponse {
        return apiService.postStockRequest(
            productId,
            UtilFunctions.getCurrentDateTime(),
            0,
            wareHouseId
        )
    }

    override suspend fun postAddProduct(photo: MultipartBody.Part): AddProductResponse {
        return apiService.postAddProduct(
            RequestBody.create("text/plain".toMediaTypeOrNull(), "Five Star"),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "23456789345678"),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "35"),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "2"),
            photo

        )
    }


}