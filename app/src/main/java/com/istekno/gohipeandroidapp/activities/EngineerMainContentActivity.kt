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
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerMainContentActivity : AppCompatActivity() {

    companion object {
        const val ACC_UPDATE_AUTH_KEY = "acc_update_auth_key"
        const val HIRE_ADD_AUTH_KEY = "hire_add_auth_key"
    }

    private lateinit var binding: ActivityEngineerMainContentBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerModel: EngineerModel
    private lateinit var dialog: Dialog
    private var state = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_engineer_main_content)
        binding.bottomNavView.menu.findItem(R.id.mn_item_maincontent_project).isVisible = false
        setSupportActionBar(binding.topAppBarMaincontentActivity)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(this)
        engineerModel = goHipePreferences.getEngineerPreference()
        dialog = Dialog()

        connectionCheck(this)
        getEngineerInfo()
        initFragment(state)
        changeFragmentScreen(state)
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

            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerByID(id!!.toLong())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {
                val list = result.database?.map {
                    EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar)
                }

                val data = list?.get(0)
                if (data?.enName.isNullOrEmpty() || data?.enJobTitle.isNullOrEmpty() || data?.enJobType.isNullOrEmpty() || data?.enLocation.isNullOrEmpty()
                        || data?.enDesc.isNullOrEmpty() || data?.enEmail.isNullOrEmpty() || data?.enIG.isNullOrEmpty()
                        || data?.enGithub.isNullOrEmpty() || data?.enGitlab.isNullOrEmpty() || data?.enAvatar.isNullOrEmpty()) {
                    binding.checkProfileFrame.visibility = View.VISIBLE
                    state = false
                } else {
                    binding.frameContainerMaincontent.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initFragment(state: Boolean) {
        val hireAuthKey = intent.getIntExtra(HIRE_ADD_AUTH_KEY, -1)
        val updateAuthKey = intent.getIntExtra(ACC_UPDATE_AUTH_KEY, -1)

        if (hireAuthKey == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHiringScreenFragment(binding.topAppBarMaincontentActivity, binding.coEngineer,binding.checkProfileFrame, binding.bottomNavView,  state)).commit()
        } else if (updateAuthKey == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerAccountScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.checkProfileFrame, binding.coEngineer, state)).commit()
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