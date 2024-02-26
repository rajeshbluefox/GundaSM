package realapps.live.gundasupermarket.homeModule.supportFunctions

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import realapps.live.gundasupermarket.R
import realapps.live.gundasupermarket.homeModule.modalClass.Product


class RequestStockDialog(
    layoutInflater: LayoutInflater,
    context: Context,
    private val listener: (product: Product) -> Unit
) {

    private val mLayoutInflater: LayoutInflater
    private val mContext: Context

   private lateinit var  messageBoxInstance: AlertDialog

   private lateinit var btYes: TextView
   private lateinit var progressBar: ProgressBar

    init {
        mLayoutInflater = layoutInflater
        mContext = context
    }

    fun openRequestStockDialog(product: Product) {

        val view = mLayoutInflater.inflate(R.layout.alert_mark_doctor, null)
        val dialog = AlertDialog.Builder(mContext).setView(view)
        dialog.setCancelable(true)
        messageBoxInstance = dialog.show()

        btYes = view.findViewById(R.id.btYes)
        progressBar=view.findViewById(R.id.progressBar)

        val tvDoctorName=view.findViewById<TextView>(R.id.doctorName)
        val ivProductImage = view.findViewById<ImageView>(R.id.productImage)

        Glide.with(mContext)
            .load(product.Photo)
            .fitCenter()
            .into(ivProductImage)

        tvDoctorName.text=product.ProductName

        hidePB()


        btYes.setOnClickListener {
            listener.invoke(product)
            showPB()
//            messageBoxInstance.dismiss()
        }

    }

    fun dismissDialog()
    {
     messageBoxInstance.dismiss()
    }


    fun showPB()
    {
        progressBar.visibility= View.VISIBLE
        btYes.visibility=View.GONE
    }

    fun hidePB()
    {
        progressBar.visibility=View.GONE
        btYes.visibility=View.VISIBLE
    }



}