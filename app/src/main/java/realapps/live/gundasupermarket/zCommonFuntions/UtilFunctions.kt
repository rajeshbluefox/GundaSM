package realapps.live.gundasupermarket.zCommonFuntions

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date

object UtilFunctions{
    fun showToast(context: Context,message: String)
    {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }
    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}