package realapps.live.gundasupermarket.stockRequestModule.apiFunctions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import realapps.live.gundasupermarket.stockRequestModule.modalClass.GetStockRequestsResponse
import javax.inject.Inject


@HiltViewModel
class StockRequestViewModel @Inject constructor(
    private val stockRequestRepository: StockRequestRepository
) : ViewModel() {


    private var getStockRequestsResponse = MutableLiveData<GetStockRequestsResponse>()

    fun getStockRequests() {
        viewModelScope.launch {
            getStockRequestsResponse.postValue(stockRequestRepository.getStockRequests())
        }
    }

    fun getStockRequestsResponse(): LiveData<GetStockRequestsResponse> {
        return getStockRequestsResponse
    }
}
