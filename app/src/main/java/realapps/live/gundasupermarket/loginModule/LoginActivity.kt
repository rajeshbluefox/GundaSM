package realapps.live.gundasupermarket.loginModule

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import realapps.live.gundasupermarket.databinding.ActivityLoginBinding
import realapps.live.gundasupermarket.zCommonFuntions.CallIntent
import realapps.live.gundasupermarket.zCommonFuntions.UtilFunctions

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListeners()

    }

    fun setOnClickListeners()
    {
        binding.btLogin.setOnClickListener{
            checkFields()

        }
    }

    fun checkFields()
    {
        val mUserName=binding.etUserName.text.toString()
        val mPassword = binding.etPassword.text.toString()

        if(mUserName.isEmpty())
        {
            UtilFunctions.showToast(this,"Enter UserName")
            return
        }
        if(mPassword.isEmpty())
        {
            UtilFunctions.showToast(this,"Enter Password")
        }

        if (mUserName=="user1" && mPassword=="12345")
        {
            CallIntent.goToHomeActivity(this,true,this)
        }

        else{
            UtilFunctions.showToast(this,"Wrong Credentials")
        }
    }
}