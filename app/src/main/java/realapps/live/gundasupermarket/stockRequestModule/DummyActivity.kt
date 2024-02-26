package realapps.live.gundasupermarket.stockRequestModule

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import realapps.live.gundasupermarket.databinding.ActivityDummyBinding
import realapps.live.gundasupermarket.homeModule.apiFunctions.HomeViewModel
import realapps.live.gundasupermarket.zCommonFuntions.UtilFunctions
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class DummyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDummyBinding

    private lateinit var homeViewModel: HomeViewModel

    private val CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 100

    private val PICK_IMAGE_REQUEST = 1

    lateinit var imageUri: Uri
    var imagePath = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDummyBinding.inflate(layoutInflater)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        setContentView(binding.root)

        onClickListeners()
    }

    private fun onClickListeners() {
        binding.btAddPhoto.setOnClickListener {
            requestCameraAndStoragePermissions()
        }

        binding.btAdd.setOnClickListener {
//            addProductsUiFunctions.checkFields()

        }
    }

    fun postImage(imagePart: MultipartBody.Part)
    {
        UtilFunctions.showToast(this,"Sending")
        homeViewModel.postAddProduct(imagePart)
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

            openCamera()

//            intentChooser()
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

                openCamera()

//                intentChooser()

            } else {
                UtilFunctions.showToast(this, "Permission not granted")
                // Handle the case where permissions are not granted
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST)

    }

    private fun intentChooser()
    {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val chooser = Intent.createChooser(galleryIntent, "Pick Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        startActivityForResult(chooser, PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {


            // Check if the data is null or not
            if (data.data != null) {
                // Gallery intent
                val selectedImageUri: Uri = data.data!!
                // Handle the selected image from the gallery
                handleGalleryImage(selectedImageUri)
            } else {
                // Camera intent
                val imageBitmap = data.extras?.get("data") as Bitmap?
                // Handle the captured image from the camera
                handleCameraImage(imageBitmap)
            }



        }
    }

    private fun handleGalleryImage(selectedImageUri: Uri) {
        // Handle the selected image from the gallery
    }

    private fun handleCameraImage(imageBitmap: Bitmap?) {
        // Handle the captured image from the camera

        binding.productImage.setImageBitmap(imageBitmap)
        UtilFunctions.showToast(applicationContext,"Ares")


//        val imageBitmap = data.extras?.get("data") as Bitmap?

        imageBitmap?.let { it ->
            val imageFile = createImageFileFromBitmap(it)
            val imagePart = imageFile?.let { createImageMultipart(it) }

            if (imagePart != null) {
                UtilFunctions.showToast(applicationContext,"Mtp")

                postImage(imagePart)
            }
        }
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


    private fun createImageFileFromBitmap(bitmap: Bitmap): File? {
        val file = File(externalCacheDir, "image.jpg")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun createImageMultipart(file: File): MultipartBody.Part {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }


}