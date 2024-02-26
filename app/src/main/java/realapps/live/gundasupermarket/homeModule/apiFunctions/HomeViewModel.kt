package realapps.live.gundasupermarket.homeModule.apiFunctions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import realapps.live.gundasupermarket.homeModule.modalClass.AddProductResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetProductsResponse
import realapps.live.gundasupermarket.homeModule.modalClass.GetWareHouseResponse
import realapps.live.gundasupermarket.homeModule.modalClass.RequestStockResponse
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private var getProductsResponse = MutableLiveData<GetProductsResponse>()
    private var getWareHouseResponse = MutableLiveData<GetWareHouseResponse>()

    fun getDoctors() {
        viewModelScope.launch {
            getProductsResponse.postValue(homeRepository.getDoctors())
        }
    }

    fun getDoctorsResponse(): LiveData<GetProductsResponse> {
        return getProductsResponse
    }

    fun getWareHouses() {
        viewModelScope.launch {
            getWareHouseResponse.postValue(homeRepository.getWareHouses())
        }
    }

    fun getWareHousesResponse(): LiveData<GetWareHouseResponse> {
        return getWareHouseResponse
    }

    fun reset()
    {
        getProductsResponse = MutableLiveData<GetProductsResponse>()
        getWareHouseResponse = MutableLiveData<GetWareHouseResponse>()
    }

    private var requestStockResponse = MutableLiveData<RequestStockResponse>()

    fun postRequestStockResponse(productId: Int, wareHouseId: Int) {
        viewModelScope.launch {
            requestStockResponse.postValue(homeRepository.postStockRequest(productId, wareHouseId))
        }
    }

    fun requestStockResponse(): LiveData<RequestStockResponse>
    {
        return requestStockResponse
    }

    private var addProductResponse = MutableLiveData<AddProductResponse>()

    fun postAddProduct(photo: MultipartBody.Part)
    {
        viewModelScope.launch {
            addProductResponse.postValue(homeRepository.postAddProduct(photo))
        }
    }

    fun addProductResponse(): LiveData<AddProductResponse>
    {
        return addProductResponse
    }



}