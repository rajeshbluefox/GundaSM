package realapps.live.gundasupermarket.homeModule


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageContract
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import realapps.live.gundasupermarket.databinding.ActivityAddProductBinding
import realapps.live.gundasupermarket.homeModule.apiFunctions.HomeViewModel
import realapps.live.gundasupermarket.homeModule.modalClass.AllData
import realapps.live.gundasupermarket.homeModule.modalClass.Product
import realapps.live.gundasupermarket.homeModule.modalClass.WareHouseData
import realapps.live.gundasupermarket.homeModule.supportFunctions.AddProductsUiFunctions
import realapps.live.gundasupermarket.zCommonFuntions.ImageCropperHandler
import realapps.live.gundasupermarket.zCommonFuntions.UtilFunctions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var addProductsUiFunctions: AddProductsUiFunctions

    private val CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 100

    private val PICK_IMAGE_REQUEST = 1

    private val CAMERA_PICK_IMAGE_REQUEST = 2


    lateinit var imageUri: Uri
    var imagePath = ""

    var imageSelectedFalg = false
    var imageSizeExceeded = false

    //Image Picker for AddWork Photos
    private val imageCropperHandler = ImageCropperHandler(
        this,
        registerForActivityResult(CropImageContract()) { /* handle result */
                result ->
            when {
                result.isSuccessful -> {
                    result.uriContent?.let {
                        Log.e("Test", "One")

                        setImage(it)
                    }
                }

                result is CropImage.CancelledResult -> addProductsUiFunctions.showErrorMessage("cropping image was cancelled by the user")
                else -> addProductsUiFunctions.showErrorMessage("cropping image failed")
            }
        },
        registerForActivityResult(CropImageContract()) { /* handle result */
            if (it !is CropImage.CancelledResult) {
                it.uriContent?.let { it1 ->
                    Log.e("Test", "Two")

                    setImage(it1)
                }
            }
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setContentView(binding.root)

        initViews()
        onClickListeners()

        observers()
    }

    private fun initViews() {
        addProductsUiFunctions = AddProductsUiFunctions(this, binding, ::postAddProduct)

        addProductsUiFunctions.hidePB()
        //Remove
//        addProductsUiFunctions.initWareHouseSP(dummySpinner())

        getWareHouses()
    }

    private fun getWareHouses() {
        homeViewModel.getWareHouses()
    }


    fun observers() {
        homeViewModel.getWareHousesResponse().observe(this) {
            if (it.code != 195) {
                if (it.code == 200) {
                    addProductsUiFunctions.initWareHouseSP(it.wareHouseList)

                }
            }
        }

        homeViewModel.addProductResponse().observe(this) {

            Log.e("Test", it.code)
            Log.e("Test", it.message)

            UtilFunctions.showToast(applicationContext, "Added Sucessfully")

            addProductsUiFunctions.hidePB()

//            if(it.code!=195)
//            {
//                if(it.code==200)
//                {
//                }
//            }
        }
    }

    private fun dummySpinner(): List<WareHouseData> {
        val wareHouseData1 = WareHouseData()

        wareHouseData1.wareHouseID = 1
        wareHouseData1.wareHouseName = "Packing Room"

        val wareHouseData2 = WareHouseData()

        wareHouseData2.wareHouseID = 2
        wareHouseData2.wareHouseName = "Rice GoDown"

        val wareHouseData3 = WareHouseData()

        wareHouseData3.wareHouseID = 3
        wareHouseData3.wareHouseName = "Packing Room"

        val wareHouseList: MutableList<WareHouseData> = mutableListOf()

        wareHouseList.add(wareHouseData1)
        wareHouseList.add(wareHouseData2)
        wareHouseList.add(wareHouseData3)

        return wareHouseList
    }

    private fun onClickListeners() {

        binding.btScan.setOnClickListener {
            scanBarcodeCustomLayout()
        }

        binding.btAddPhoto.setOnClickListener {
            requestCameraAndStoragePermissions()
        }

        binding.btBack.setOnClickListener {
            finish()
        }

        binding.btAdd.setOnClickListener {
            addProductsUiFunctions.checkFields()
        }
    }

    private fun requestCameraAndStoragePermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest,
                CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE
            )
        } else {

            intentChooser()
//            openGallery()
            // Both permissions are already granted
//            imageCropperHandler.startCameraWithoutUri(includeCamera = true, includeGallery = true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Both permissions are granted

//                openGallery()
                intentChooser()

//                imageCropperHandler.startCameraWithoutUri(
//                    includeCamera = true,
//                    includeGallery = true
//                )
            } else {
                UtilFunctions.showToast(this, "Permission not granted")
                // Handle the case where permissions are not granted
            }
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_PICK_IMAGE_REQUEST)

    }

    private fun intentChooser()
    {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1) // Rear camera
        cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0) // Front camera
        cameraIntent.putExtra("crop", "true")
        cameraIntent.putExtra("aspectX", 1)
        cameraIntent.putExtra("aspectY", 1)
        cameraIntent.putExtra("outputX", 300) // Adjust the output size as needed
        cameraIntent.putExtra("outputY", 300)

        val chooser = Intent.createChooser(galleryIntent, "Pick Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        startActivityForResult(chooser, PICK_IMAGE_REQUEST)

//        resultLauncher.launch(chooser)
    }


    private fun getRealPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(projection[0])
        val picturePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return picturePath ?: ""
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            data?.data?.let { uri ->
                // Handle the selected image URI
//                selectedImageUri = uri


                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap?

                if (imageBitmap != null) {
                    binding.productImage.setImageBitmap(imageBitmap)
                    binding.productImage.visibility = View.VISIBLE
                    Log.d("ImageCapture", "Image set to ImageView successfully")
                } else {
                    Log.e("ImageCapture", "ImageBitmap is null")
                }

                //start
//                val data: Intent? = result.data
//                val imageBitmap = data?.extras?.get("data") as Bitmap?
//
//                // Set the captured image to the ImageView
//                imageBitmap?.let {
//                    binding.productImage.setImageBitmap(it)
//                }
//
//                binding.productImage.visibility = View.VISIBLE
//
//                UtilFunctions.showToast(applicationContext,"IRec")


//                imageUri = uri
//
//                imageSelectedFalg=true
//
//                Glide.with(this)
//                    .load(uri)
//                    .fitCenter()
//                    .into(binding.productImage)
//
//                binding.productImage.visibility = View.VISIBLE
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImage: Uri = data.data!!

                binding.productImage.setImageBitmap(data.extras?.get("data") as Bitmap)


            UtilFunctions.showToast(applicationContext,"Ares")

//            imageUri = selectedImage
            imagePath = getRealPathFromUri(selectedImage)

//            imageSelectedFalg=true
//            checkImageSize()

//            Glide.with(this)
//                .load(selectedImage)
//                .fitCenter()
//                .into(binding.productImage)
//
            binding.productImage.visibility = View.VISIBLE
        }
    }

    fun setImage(selectedImage: Uri) {

        imageUri = selectedImage
//        imagePath = getRealPathFromUri(selectedImage)

//        checkImageSize()

        Glide.with(this)
            .load(selectedImage)
            .fitCenter()
            .into(binding.productImage)

        binding.productImage.visibility = View.VISIBLE
    }

    private fun checkImageSize() {
        var bmp: Bitmap? = null
        try {
            bmp = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val baos = ByteArrayOutputStream()
        bmp?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val originalSize = baos.size() ?: 0

        imageSizeExceeded = false

        if (originalSize > 2 * 1024 * 1024) { // Check if size is greater than 5MB

            UtilFunctions.showToast(this, "Image size exceeds 5MB")
            imageSizeExceeded = true

//            binding.photoErrorTV.visibility = View.VISIBLE
        }

//        binding.photoErrorTV.visibility = View.GONE
        imageSelectedFalg = true
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

            binding.etBarCode.setText(result.contents)
            findProduct(result.contents)

        }
    }

    private fun postAddProduct(product: Product) {

        if (imageSelectedFalg) {
            if (imageSizeExceeded)
                UtilFunctions.showToast(this, "Image Size Exceeding 5MB")
            else {
                UtilFunctions.showToast(this, "Adding")
                addProductsUiFunctions.showPB()
                homeViewModel.postAddProduct(getImageFromByteArrayNew())
            }
        } else
            UtilFunctions.showToast(this, "Image Not Selected")

    }

    private fun getImageFromByteArrayNew(): MultipartBody.Part {
        var bmp: Bitmap? = null
        try {
            bmp = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val baos = ByteArrayOutputStream()
        bmp?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val originalSize = baos.size() ?: 0

        if (originalSize > 100 * 1024) { // Check if size is greater than 100 KB
            var quality = 100
            var compressedSize = originalSize

            while (compressedSize > 100 * 1024 && quality > 0) {
                baos.reset() // Reset the output stream
                bmp?.compress(Bitmap.CompressFormat.JPEG, quality, baos)
                compressedSize = baos.size() ?: 0
                quality -= 5 // Decrease the quality by 5 in each iteration
            }
        }

        val fileInBytes: ByteArray = baos.toByteArray()

        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), fileInBytes)
        val file = File(imagePath)

        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }


    fun findProduct(barCode: String) {
        for (product in AllData.stockRequest) {
            if (product.ProductCode == barCode) {
                addProductsUiFunctions.showBarCodeError(true)
                return
            }
        }

        addProductsUiFunctions.showBarCodeError(false)
    }


}