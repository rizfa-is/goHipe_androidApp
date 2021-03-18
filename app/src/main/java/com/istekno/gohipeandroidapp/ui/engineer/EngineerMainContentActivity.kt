package com.istekno.gohipeandroidapp.ui.engineer

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.activity.MainScreenActivity
import com.istekno.gohipeandroidapp.ui.activity.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.ActivityEngineerMainContentBinding
import com.istekno.gohipeandroidapp.ui.engineer.account.EngineerAccountScreenFragment
import com.istekno.gohipeandroidapp.ui.engineer.hire.EngineerHiringScreenFragment
import com.istekno.gohipeandroidapp.ui.engineer.home.EngineerHomeScreenFragment
import com.istekno.gohipeandroidapp.ui.engineer.search.EngineerSearchScreenFragment
import com.istekno.gohipeandroidapp.models.EngineerPreferenceModel
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.ui.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class EngineerMainContentActivity : AppCompatActivity() {

    companion object {
        const val ACC_UPDATE_AUTH_KEY = "acc_update_auth_key"
        const val HIRE_ADD_AUTH_KEY = "hire_add_auth_key"
        const val EMPTY_DATA_AUTH_KEY = "empty_data_auth_key"
        const val CHECK_PROFILE_AUTH_KEY = "check_profile_auth_key"
    }

    private lateinit var binding: ActivityEngineerMainContentBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var engineerPreferenceModel: EngineerPreferenceModel
    private lateinit var dialog: Dialog
    private lateinit var engineer: EngineerModelResponse
    private var state = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_engineer_main_content)
        binding.bottomNavView.menu.findItem(R.id.mn_item_maincontent_project).isVisible = false
        setSupportActionBar(binding.topAppBarMaincontentActivity)

        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(this)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(this)
        engineerPreferenceModel = goHipePreferences.getEngineerPreference()
        dialog = Dialog()

        connectionCheck(this)
        checkProfile()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maincontent_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun checkProfile() {
        val data = intent.getParcelableExtra<EngineerModelResponse>(CHECK_PROFILE_AUTH_KEY)

        if (data != null) {
            if (data.enJobTitle.isNullOrEmpty() || data.enJobType.isNullOrEmpty() || data.enLocation.isNullOrEmpty()
                || data.enDesc.isNullOrEmpty() || data.enIG.isNullOrEmpty() || data.enGithub.isNullOrEmpty()
                || data.enGitlab.isNullOrEmpty()) {

                binding.rlCheckProfileFrame.visibility = View.VISIBLE
                binding.frameContainerMaincontent.visibility = View.GONE
                state = false
                engineer = data

                initFragment(state)
                changeFragmentScreen(state)
                viewListener()
            } else {
                binding.frameContainerMaincontent.visibility = View.VISIBLE
                initFragment(state)
                changeFragmentScreen(state)
            }
        } else {
            binding.frameContainerMaincontent.visibility = View.VISIBLE
            initFragment(state)
            changeFragmentScreen(state)
        }
    }

    private fun viewListener() {
        binding.btnGotoAccount.setOnClickListener {
            val sendIntent = Intent(this, SettingScreenActivity::class.java)
            sendIntent.putExtra(EngineerAccountScreenFragment.EDIT_PROFILE_AUTH_KEY, 0)
            sendIntent.putExtra(EMPTY_DATA_AUTH_KEY, engineer)
            startActivity(sendIntent)
        }
        binding.btnLogout.setOnClickListener {
            if (goHipePreferences.getEngineerPreference().isLogin) {
                dialog.dialog(this, "Are you sure to logout ?") {
                    engineerPreferenceModel.isLogin = false
                    engineerPreferenceModel.level = ""
                    goHipePreferences.setEngineerPreference(engineerPreferenceModel)

                    startActivity(Intent(this, MainScreenActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun initFragment(state: Boolean) {
        val hireAuthKey = intent.getIntExtra(HIRE_ADD_AUTH_KEY, -1)
        val updateAuthKey = intent.getIntExtra(ACC_UPDATE_AUTH_KEY, -1)

        when {
            hireAuthKey == 1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHiringScreenFragment(binding.topAppBarMaincontentActivity, binding.coEngineer,binding.rlCheckProfileFrame, binding.bottomNavView,  state)).commit()
            }
            updateAuthKey == 0 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerAccountScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.rlCheckProfileFrame, binding.coEngineer, state)).commit()
            }
            else -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHomeScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.coEngineer, binding.rlCheckProfileFrame, state)).commit()
            }
        }
    }

    private fun changeFragmentScreen(state: Boolean) {

        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mn_item_maincontent_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHomeScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.coEngineer, binding.rlCheckProfileFrame, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerSearchScreenFragment(binding.coEngineer, binding.rlCheckProfileFrame, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_hiring -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHiringScreenFragment(binding.topAppBarMaincontentActivity, binding.coEngineer, binding.rlCheckProfileFrame, binding.bottomNavView, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_account -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerAccountScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.rlCheckProfileFrame, binding.coEngineer, state)).commit()
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

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}