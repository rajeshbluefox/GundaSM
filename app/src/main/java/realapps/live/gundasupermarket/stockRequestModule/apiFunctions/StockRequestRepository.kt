package realapps.live.gundasupermarket.stockRequestModule.apiFunctions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import realapps.live.gundasupermarket.stockRequestModule.modalClass.GetStockRequestsResponse
import realapps.live.gundasupermarket.zAPIEndPoints.ApiHelper
import javax.inject.Inject


class StockRequestRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    private var getStockRequestsResponse = GetStockRequestsResponse()

    suspend fun getStockRequests(): GetStockRequestsResponse {
        try {
            withContext(Dispatchers.IO) {
                getStockRequestsResponse = apiHelper.getStockRequestList()
            }
        } catch (_: Exception) {
        }
        return getStockRequestsResponse
    }

}