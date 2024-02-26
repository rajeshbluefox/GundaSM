package realapps.live.gundasupermarket.zCommonFuntions

import android.app.Activity
import android.content.Context
import android.content.Intent
import realapps.live.gundasupermarket.MainActivity
import realapps.live.gundasupermarket.homeModule.AddProductActivity
import realapps.live.gundasupermarket.homeModule.HomeActivity
import realapps.live.gundasupermarket.stockRequestModule.StockRequestsActivity

object CallIntent {
    fun goToHomeActivity(context: Context, killMe: Boolean, activity: Activity) {
        val intent = Intent(context, HomeActivity::class.java)
        context.startActivity(intent)
        if (killMe) activity.finish()
    }

    fun goToAddProductActivity(context: Context, killMe: Boolean, activity: Activity) {
        val intent = Intent(context, AddProductActivity::class.java)
        context.startActivity(intent)
        if (killMe) activity.finish()
    }

    fun goToStockRequestActivity(context: Context, killMe: Boolean, activity: Activity) {
        val intent = Intent(context, StockRequestsActivity::class.java)
        context.startActivity(intent)
        if (killMe) activity.finish()
    }

}
