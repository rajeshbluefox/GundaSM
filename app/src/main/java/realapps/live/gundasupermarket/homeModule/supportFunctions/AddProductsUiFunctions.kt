package realapps.live.gundasupermarket.homeModule.supportFunctions

import android.R
import android.content.Context
import android.view.View
import android.widget.AdapterView
import realapps.live.gundasupermarket.databinding.ActivityAddProductBinding
import realapps.live.gundasupermarket.homeModule.modalClass.Product
import realapps.live.gundasupermarket.homeModule.modalClass.WareHouseData
import realapps.live.gundasupermarket.zCommonFuntions.UtilFunctions


class AddProductsUiFunctions(
    val context: Context,
    private val binding: ActivityAddProductBinding,
    private val productDetails: (product: Product) -> Unit
) {


    init {

    }

//    fun showPB() {
//        binding.pbProcessing.visibility = View.VISIBLE
//
//    }
//
//    fun hidePB() {
//        binding.pbProcessing.visibility = View.GONE
//    }

    var isInValidBarCode = false

    fun checkFields() {
        val mBarCode = binding.etBarCode.text.toString()
        val mProductName = binding.etProductName.text.toString()
        val mProductPrice = binding.etProductPrice.text.toString()

        if (mBarCode.isEmpty()) {
            UtilFunctions.showToast(context, "Scan BarCode")
            return
        }

        if(isInValidBarCode)
        {
            UtilFunctions.showToast(context, "InValid BarCode")
            return
        }

        if (mProductName.isEmpty()) {
            UtilFunctions.showToast(context, "Enter ProductName")
            return
        }

        if (mProductPrice.isEmpty()) {
            UtilFunctions.showToast(context, "Enter ProductPrice")
            return
        }

        if (selectedWareHouseId == 0) {
            UtilFunctions.showToast(context, "Select WareHouse")
            return
        }

        val product = Product()
        product.ProductCode = mBarCode
        product.ProductName = mProductName
        product.ProductPrice = mProductPrice
        product.wareHouseId = selectedWareHouseId

        productDetails.invoke(product)

    }

    var selectedWareHouseId = 0

    fun initWareHouseSP(wareHouseList: List<WareHouseData>) {

        val wareHouseDefault = WareHouseData()
        wareHouseDefault.wareHouseID = 0
        wareHouseDefault.wareHouseName = "Select"

        val updatedWareHouseList: List<WareHouseData> = listOf(wareHouseDefault) + wareHouseList

        val adapter =
            WareHouseSPAdapter(context, R.layout.simple_spinner_item, updatedWareHouseList)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.spWareHouses.adapter = adapter


        // Set a listener for item selections if needed
        binding.spWareHouses.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle the selected item as needed
                    val selectedItem = parent.getItemAtPosition(position).toString()
                    // Do something with the selected item

                    selectedWareHouseId = position

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Handle the case where no item is selected
                }
            }
    }

    fun showErrorMessage(msg: String) {
        UtilFunctions.showToast(context, msg)
    }

    fun showBarCodeError(status: Boolean) {

        isInValidBarCode=status

        if (status)
            binding.barCodeError.visibility = View.VISIBLE
        else
            binding.barCodeError.visibility = View.GONE
    }

    fun showPB()
    {
        binding.btAdd.visibility=View.GONE
        binding.progressBar.visibility=View.VISIBLE
    }

    fun hidePB()
    {
        binding.btAdd.visibility=View.VISIBLE
        binding.progressBar.visibility=View.GONE
    }

}