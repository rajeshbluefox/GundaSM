package realapps.live.gundasupermarket

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import realapps.live.gundasupermarket.zCommonFuntions.CallIntent
import realapps.live.gundasupermarket.zSharedPreference.LoginData

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLoginStatusandNavigate()

//        initViewModels()
//        observers()
    }

    private fun checkLoginStatusandNavigate() {
        Handler(Looper.getMainLooper()).postDelayed({

            if (LoginData.getUserLoginStatus(this)) {
                CallIntent.goToHomeActivity(this, true, this)
            } else {
                CallIntent.goToLoginActivity(this, true, this)
            }

        }, 2000)
    }


}