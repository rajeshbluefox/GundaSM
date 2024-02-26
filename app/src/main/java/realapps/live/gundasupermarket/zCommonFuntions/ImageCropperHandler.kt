package realapps.live.gundasupermarket.zCommonFuntions

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ImageCropperHandler(
    private val context: Context,
    private val cropImage: ActivityResultLauncher<CropImageContractOptions>,
    private val customCropImage: ActivityResultLauncher<CropImageContractOptions>
) {

    private var outputUri: Uri? = null

    fun startCameraWithoutUri(includeCamera: Boolean, includeGallery: Boolean) {
        customCropImage.launch(
            CropImageContractOptions(
                uri = null,
                cropImageOptions = CropImageOptions(
                    imageSourceIncludeCamera = includeCamera,
                    imageSourceIncludeGallery = includeGallery,
                    fixAspectRatio = true,
                    aspectRatioY = 1,
                    aspectRatioX = 1
                ),
            )
        )
    }

    fun startCameraWithUri() {
        cropImage.launch(
            CropImageContractOptions(
                uri = outputUri,
                cropImageOptions = CropImageOptions(
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                    fixAspectRatio = true
                )
            )
        )
    }

    public fun handleCropImageResult(uri: String) {
        Log.e("Test", "Image Picked")
    }

    fun setupOutputUri() {
        if (outputUri == null) {
            val authorities = "${context.packageName}$AUTHORITY_SUFFIX"
            outputUri = FileProvider.getUriForFile(context, authorities, createImageFile())
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "$FILE_NAMING_PREFIX$timeStamp$FILE_NAMING_SUFFIX",
            FILE_FORMAT,
            storageDir
        )
    }

    fun showErrorMessage(message: String) {
//        Timber.tag("AIC-Sample").e("Camera error: $message")
        Toast.makeText(context, "Crop failed: $message", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val DATE_FORMAT = "yyyyMMdd_HHmmss"
        const val FILE_NAMING_PREFIX = "JPEG_"
        const val FILE_NAMING_SUFFIX = "_"
        const val FILE_FORMAT = ".jpg"
        const val AUTHORITY_SUFFIX = ".cropper.fileprovider"
    }



}