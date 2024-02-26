package realapps.live.gundasupermarket.homeModule.apiFunctions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import realapps.live.gundasupermarket.homeModule.modalClass.AddProductResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetProductsResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetWareHouseResponse
import realapps.live.gundasupermarket.homeModule.modalClass.RequestStockResponse
import realapps.live.gundasupermarket.zAPIEndPoints.ApiHelper
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    private var getProducts = GetProductsResponse()

    suspend fun getDoctors(): GetProductsResponse {
        try {
            withContext(Dispatchers.IO) {
                getProducts = apiHelper.getProductsList()
            }
        } catch (_: Exception) {
        }
        return getProducts
    }

    private var getWareHousesResponse = GetWareHouseResponse()

    suspend fun getWareHouses(): GetWareHouseResponse {
        try {
            withContext(Dispatchers.IO) {
                getWareHousesResponse = apiHelper.getWareHouseList()
            }
        } catch (_: Exception) {
        }
        return getWareHousesResponse
    }

    private var requestStockResponse = RequestStockResponse()

    suspend fun postStockRequest(productId: Int, wareHouseId: Int): RequestStockResponse {
        try {
            withContext(Dispatchers.IO) {
                requestStockResponse = apiHelper.postStockRequest(productId, wareHouseId)
            }
        } catch (_: Exception) {
        }
        return requestStockResponse
    }

    private var addProductResponse = AddProductResponse()

    suspend fun postAddProduct(photo: MultipartBody.Part): AddProductResponse {
        try {
            withContext(Dispatchers.IO) {
                addProductResponse = apiHelper.postAddProduct(photo)
            }
        } catch (_: Exception) {
        }
        return addProductResponse
    }

}