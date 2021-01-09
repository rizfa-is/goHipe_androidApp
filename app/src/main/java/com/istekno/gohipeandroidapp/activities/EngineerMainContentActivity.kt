package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityEngineerMainContentBinding
import com.istekno.gohipeandroidapp.fragments.engineer.*
import com.istekno.gohipeandroidapp.models.EngineerModel
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerMainContentActivity : AppCompatActivity() {

    companion object {
        const val HIRE_ADD_AUTH_KEY = "hire_add_auth_key"
    }

    private lateinit var binding: ActivityEngineerMainContentBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerModel: EngineerModel
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_engineer_main_content)
        setSupportActionBar(binding.topAppBarMaincontentActivity)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(this)
        engineerModel = goHipePreferences.getEngineerPreference()
        dialog = Dialog()

        connectionCheck(this)
        getEngineerInfo()
        viewListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maincontent_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun viewListener() {
        binding.btnGotoAccount.setOnClickListener {
            val sendIntent = Intent(this, SettingScreenActivity::class.java)
            sendIntent.putExtra(EngineerAccountScreenFragment.EDIT_PROFILE_AUTH_KEY, 0)
            startActivity(sendIntent)
        }
        binding.btnLogout.setOnClickListener {
            if (goHipePreferences.getEngineerPreference().isLogin) {
                dialog.dialog(this, "Are you sure to logout ?") {
                    engineerModel.isLogin = false
                    engineerModel.level = ""
                    goHipePreferences.setEngineerPreference(engineerModel)

                    startActivity(Intent(this, MainScreenActivity::class.java))
                    finish()
                }
            }
        }
    }

    fun getEngineerInfo() {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().acID

            binding.pgMaincontent.visibility = View.VISIBLE
            withContext(Dispatchers.IO) {
                try {
                    val result1 = service.getEngineerByID(id!!.toLong())
                    val list = result1.database?.map {
                        EngineerModelResponse(it.enID, it.enName, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar)
                    }

                    runOnUiThread {

                        val data = list?.get(0)
                        var state = true
                        if (data?.enName.isNullOrEmpty() || data?.enJobTitle.isNullOrEmpty() || data?.enJobType.isNullOrEmpty() || data?.enLocation.isNullOrEmpty()
                                || data?.enDesc.isNullOrEmpty() || data?.enEmail.isNullOrEmpty() || data?.enIG.isNullOrEmpty()
                                || data?.enGithub.isNullOrEmpty() || data?.enGitlab.isNullOrEmpty() || data?.enAvatar.isNullOrEmpty()) {
                            binding.checkProfileFrame.visibility = View.VISIBLE
                            state = false
                        } else {
                            binding.frameContainerMaincontent.visibility = View.VISIBLE
                        }

                        initFragment(state)
                        binding.pgMaincontent.visibility = View.GONE

                        changeFragmentScreen(state)
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun initFragment(state: Boolean) {
        val hireAuthKey = intent.getIntExtra(HIRE_ADD_AUTH_KEY, -1)
        binding.bottomNavView.menu.findItem(R.id.mn_item_maincontent_project).isVisible = false

        if (hireAuthKey == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHiringScreenFragment(binding.topAppBarMaincontentActivity, binding.coEngineer,binding.checkProfileFrame, binding.bottomNavView,  state)).commit()

        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHomeScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.coEngineer, binding.checkProfileFrame, state)).commit()
        }
    }

    private fun changeFragmentScreen(state: Boolean) {

        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mn_item_maincontent_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHomeScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.coEngineer, binding.checkProfileFrame, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerSearchScreenFragment(binding.topAppBarMaincontentActivity, binding.coEngineer, binding.checkProfileFrame, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_hiring -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHiringScreenFragment(binding.topAppBarMaincontentActivity, binding.coEngineer, binding.checkProfileFrame, binding.bottomNavView, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_account -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerAccountScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.checkProfileFrame, binding.coEngineer, state)).commit()
                    true
                }
                else -> true
            }
        }
    }

    private fun connectionCheck(context: Context) {
        val connectManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val checkNetwork = connectManager.activeNetworkInfo

        if (checkNetwork == null || !checkNetwork.isConnected || !checkNetwork.isAvailable) {
            dialog.dialogCheckInternet(this, this)
        }
    }
}