package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivitySplashScreenBinding
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.CompanyModelResponse
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class SplashScreenActivity : AppCompatActivity() {

    companion object {
        private const val splashDuration = 3000
        const val CHECK_PROFILE_AUTH_KEY = "check_profile_auth_key"
    }

    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var dialog: Dialog
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        goHipePreferences = GoHipePreferences(this)
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
        val companyModel = goHipePreferences.getCompanyPreference()
        val engineerModel = goHipePreferences.getEngineerPreference()

        if (idComp != (-1).toLong() || idEng != (-1).toLong()) {
            when {
                companyModel.isLogin -> {
                    coroutineScope.launch {
                        withContext(Job() + Dispatchers.IO) {
                            try {
                                val result = service.getCompanyByID(idComp!!)
                                val list = result.database?.map {
                                    CompanyModelResponse(it.cpID, it.cpName, it.cpEmail, it.cpPhone, it.cpCompany, it.cpPosition, it.cpField, it.cpLocation, it.cpDesc,
                                            it.cpInsta, it.cpLinkedIn, it.cpAvatar)
                                }

                                Handler(mainLooper).postDelayed(
                                        {
                                            val sendIntent = Intent(context, CompanyMainContentActivity::class.java)
                                            sendIntent.putExtra(CHECK_PROFILE_AUTH_KEY, list?.get(0))
                                            startActivity(sendIntent)
                                            finish()
                                        }, splashDuration.toLong()
                                )
                            } catch (e: Throwable) {
                                companyModel.isLogin = false
                                companyModel.level = ""
                                goHipePreferences.setCompanyPreference(companyModel)

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
                                val result = service.getEngineerByID(idEng!!)
                                val list = result.database?.map {
                                    EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar)
                                }

                                Handler(mainLooper).postDelayed(
                                        {
                                            val sendIntent = Intent(context, EngineerMainContentActivity::class.java)
                                            sendIntent.putExtra(CHECK_PROFILE_AUTH_KEY, list?.get(0))
                                            startActivity(sendIntent)
                                            finish()
                                        }, splashDuration.toLong()
                                )
                            } catch (e: Throwable) {
                                engineerModel.isLogin = false
                                engineerModel.level = ""
                                goHipePreferences.setEngineerPreference(engineerModel)

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
                else -> {
                    Handler(mainLooper).postDelayed(
                        {
                            startActivity(Intent(context, MainScreenActivity::class.java))
                            finish()
                        }, splashDuration.toLong()
                    )
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