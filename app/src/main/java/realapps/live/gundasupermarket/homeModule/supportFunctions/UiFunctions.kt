package realapps.live.gundasupermarket.homeModule.supportFunctions

import android.content.Context
import android.view.View
import realapps.live.gundasupermarket.databinding.ActivityHomeBinding

class UiFunctions(
    val context: Context,
    private val binding: ActivityHomeBinding
) {


    init {

    }

    fun showPB() {
        binding.pbProcessing.visibility = View.VISIBLE

    }

    fun hidePB() {
        binding.pbProcessing.visibility = View.GONE
    }

}