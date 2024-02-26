package realapps.live.gundasupermarket.homeModule

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import realapps.live.gundasupermarket.databinding.ActivityHomeBinding
import realapps.live.gundasupermarket.homeModule.apiFunctions.HomeViewModel
import realapps.live.gundasupermarket.homeModule.modalClass.AllData
import realapps.live.gundasupermarket.homeModule.modalClass.Product
import realapps.live.gundasupermarket.homeModule.supportFunctions.RequestStockDialog
import realapps.live.gundasupermarket.homeModule.supportFunctions.UiFunctions
import realapps.live.gundasupermarket.zCommonFuntions.CallIntent
import realapps.live.gundasupermarket.zCommonFuntions.UtilFunctions


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var uiFunctions: UiFunctions

    private lateinit var requestStockDialog: RequestStockDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setContentView(binding.root)

        initViewModels()
        observers()
        onClickListeners()
    }

    private fun onClickListeners() {
        binding.btBarCode.setOnClickListener {
            scanBarcodeCustomLayout()
        }

        binding.btAddProduct.setOnClickListener {
            CallIntent.goToAddProductActivity(this, false, this)
        }

        binding.btRequests.setOnClickListener {
            CallIntent.goToStockRequestActivity(this,false,this)
        }
    }

    private fun initViewModels() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.reset()

        requestStockDialog = RequestStockDialog(layoutInflater, this, ::postRequest)

        uiFunctions = UiFunctions(this, binding)

        getProducts()
        editTextListener()
    }

    private fun postRequest(product: Product) {
//        UtilFunctions.showToast(this, "Sending Request")
        homeViewModel.postRequestStockResponse(product.ProductId,product.wareHouseId)
    }

    private fun getProducts() {
        uiFunctions.showPB()
        homeViewModel.getDoctors()
    }

    private var productList = ArrayList<Product>()

    private fun observers() {
        homeViewModel.getDoctorsResponse().observe(this) {
            if (it.code != 195) {

                if (it.code == 200) {
                    Log.e("Test", it.productsList.toString())
                    uiFunctions.hidePB()
                    productList = it.productsList

                    AllData.stockRequest=it.productsList

                    initProductRv(it.productsList)
                }

            }
        }

        homeViewModel.requestStockResponse().observe(this){
            if(it.code!=195)
            {
                if(it.code==200)
                {
                    UtilFunctions.showToast(this,"Request Sent Sucessfully")
                    requestStockDialog.dismissDialog()
                }
            }
        }
    }

    private fun initProductRv(doctorsList: ArrayList<Product>) {
        val productsAdapter = ProductsAdapter(doctorsList, this, ::onProductSelected)
//        binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(
//                context,
//                LinearLayoutManager.VERTICAL, false
//            )
//            adapter = productsAdapter
//        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                context,
                2,  // Number of items in each row
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = productsAdapter
        }


    }

    private fun onProductSelected(product: Product) {
//        UtilFunctions.showToast(this, "Selected")

        requestStockDialog.openRequestStockDialog(product)
    }

    ////////Search by Name

    private var isFilteredList = false

    private fun editTextListener() {

        binding.searchBar.doOnTextChanged { text, _, _, _ ->

            Log.e("Test", text.toString())

            if (text != null) {
                isFilteredList = if (text.isEmpty()) {
                    initProductRv(productList)
                    false
                } else {
                    filterItems(text.toString())
                    true
                }
            }
        }

    }

    private var filteredProducts: ArrayList<Product> = ArrayList()

    private fun filterItems(text: String) {

        filteredProducts.clear()

        for (localItems in productList) {
            if (localItems.ProductName.lowercase().startsWith(text)) {
                filteredProducts.add(localItems)
            }
        }

        initRecyclerViewFilteredList()
    }

    private fun initRecyclerViewFilteredList() {

        val productsAdapter = ProductsAdapter(filteredProducts, this, ::onProductSelected)
//        binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(
//                context,
//                LinearLayoutManager.VERTICAL, false
//            )
//            adapter = productsAdapter
//        }

        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                context,
                2,  // Number of items in each row
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = productsAdapter
        }


    }

    private fun scanBarcodeCustomLayout() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(true)
        barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            val originalIntent = result.originalIntent
            if (originalIntent == null) {
                Log.d("MainActivity", "Cancelled scan")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d(
                    "MainActivity",
                    "Cancelled scan due to missing camera permission"
                )
            }
        } else {
            Log.d("MainActivity", "Scanned ${result.contents}")

            findProductWithBarCode(result.contents)

        }
    }

    private fun findProductWithBarCode(barcode: String) {
        for (localItems in productList) {
            if (localItems.ProductCode == barcode) {
                requestStockDialog.openRequestStockDialog(localItems)
            }
        }
    }



}