package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import com.istekno.gohipeandroidapp.databinding.ActivitySplashScreenBinding
import com.istekno.gohipeandroidapp.models.CompanyModel
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.CompanyGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import kotlinx.coroutines.*
import okhttp3.Callback

class SplashScreenActivity : AppCompatActivity() {

    companion object {
        private const val splashDuration = 3000
    }

    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerModel: EngineerModel
    private lateinit var companyModel: CompanyModel
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        goHipePreferences = GoHipePreferences(this)
        engineerModel = goHipePreferences.getEngineerPreference()
        companyModel = goHipePreferences.getCompanyPreference()
        dialog = Dialog()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(GoHipeApiService::class.java)

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
            sessionCheck(this)
        }
    }

    private fun sessionCheck(context: Context) {
        val idComp = goHipePreferences.getCompanyPreference().acID
        val idEng = goHipePreferences.getEngineerPreference().acID

        if (idComp != (-1).toLong() || idEng != (-1).toLong()) {
            when {
                companyModel.isLogin -> {
                    coroutineScope.launch {
                        withContext(Job() + Dispatchers.IO) {
                            try {
                                service.getCompanyByID(idComp!!)
                                Handler(mainLooper).postDelayed(
                                        {
                                            val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                                            startActivity(sendIntent)
                                            finish()
                                        }, splashDuration.toLong()
                                )
                            } catch (e: Throwable) {
                                Handler(mainLooper).postDelayed(
                                        {
                                            startActivity(Intent(context, MainScreenActivity::class.java))
                                            finish()
                                        }, splashDuration.toLong()
                                )
                            }
                        }
                    }
                }
                engineerModel.isLogin -> {
                    coroutineScope.launch {
                        withContext(Job() + Dispatchers.IO) {
                            try {
                                service.getEngineerByID(idEng!!)
                                Handler(mainLooper).postDelayed(
                                        {
                                            val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
                                            startActivity(sendIntent)
                                            finish()
                                        }, splashDuration.toLong()
                                )
                            } catch (e: Throwable) {
                                Handler(mainLooper).postDelayed(
                                        {
                                            startActivity(Intent(context, MainScreenActivity::class.java))
                                            finish()
                                        }, splashDuration.toLong()
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Handler(mainLooper).postDelayed(
                    {
                        startActivity(Intent(context, IntroScreenActivity::class.java))
                        finish()
                    }, splashDuration.toLong()
            )
        }
    }
}