package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.databases.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.ActivitySplashScreenBinding
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.utility.Dialog

class SplashScreenActivity : AppCompatActivity() {

    companion object {
        const val SPLASH_AUTH_KEY = "splash_auth_key"
    }

    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerModel: EngineerModel
    private lateinit var companyModel: CompanyModel
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        goHipePreferences = GoHipePreferences(this)
        engineerModel = goHipePreferences.getEngineerPreference()
        companyModel = goHipePreferences.getCompanyPreference()
        dialog = Dialog()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )

        connectionCheck(this)
    }

    private fun connectionCheck(context: Context) {
        val connectManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val checkNetwork = connectManager.activeNetworkInfo

        if (checkNetwork == null || !checkNetwork.isConnected || !checkNetwork.isAvailable) {
            dialog.dialogCheckInternet(this, this)
        } else {
            Handler(mainLooper).postDelayed(
                {
                    if (engineerModel.isLogin) {
                        val sendIntent = Intent(this, MainContentActivity::class.java)
                        sendIntent.putExtra(SPLASH_AUTH_KEY, 0)
                        startActivity(sendIntent)
                        finish()
                    } else if (companyModel.isLogin) {
                        val sendIntent = Intent(this, MainContentActivity::class.java)
                        sendIntent.putExtra(SPLASH_AUTH_KEY, 1)
                        startActivity(sendIntent)
                        finish()
                    } else {
                        startActivity(Intent(this, IntroScreenActivity::class.java))
                        finish()
                    }
                }, 4000
            )

            Toast.makeText(context, "Internet connection OK", Toast.LENGTH_LONG).show()
        }
    }
}