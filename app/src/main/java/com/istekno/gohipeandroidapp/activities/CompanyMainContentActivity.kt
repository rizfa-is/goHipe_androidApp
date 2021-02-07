package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityCompanyMainContentBinding
import com.istekno.gohipeandroidapp.maincontent.company.*
import com.istekno.gohipeandroidapp.maincontent.engineer.account.EngineerAccountScreenFragment
import com.istekno.gohipeandroidapp.models.CompanyPreferenceModel
import com.istekno.gohipeandroidapp.retrofit.CompanyModelResponse
import com.istekno.gohipeandroidapp.utility.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences

class CompanyMainContentActivity : AppCompatActivity() {

    companion object {
        const val HIRE_ADD_AUTH_KEY = "hire_add_auth_key"
        const val PROJECT_ADD_AUTH_KEY = "project_add_auth_key"
        const val ACC_UPDATE_AUTH_KEY = "acc_update_auth_key"
        const val EMPTY_DATA_AUTH_KEY = "empty_data_auth_key"
        const val CHECK_PROFILE_AUTH_KEY = "check_profile_auth_key"
    }

    private lateinit var binding: ActivityCompanyMainContentBinding
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var companyPreferenceModel: CompanyPreferenceModel
    private lateinit var dialog: Dialog
    private lateinit var company: CompanyModelResponse
    private var state = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_main_content)
        setSupportActionBar(binding.topAppBarMaincontentActivity)

        goHipePreferences = GoHipePreferences(this)
        companyPreferenceModel = goHipePreferences.getCompanyPreference()
        dialog = Dialog()

        connectionCheck(this)
        checkProfile()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maincontent_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun checkProfile() {
        val data = intent.getParcelableExtra<CompanyModelResponse>(CHECK_PROFILE_AUTH_KEY)

        if (data != null) {
            if (data.cpField.isNullOrEmpty() || data.cpLocation.isNullOrEmpty() || data.cpDesc.isNullOrEmpty()
                || data.cpInsta.isNullOrEmpty() || data.cpLinkedIn.isNullOrEmpty()) {

                binding.rlCheckProfileFrame.visibility = View.VISIBLE
                binding.frameContainerMaincontent.visibility = View.GONE
                state = false
                company = data

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
            sendIntent.putExtra(EngineerAccountScreenFragment.EDIT_PROFILE_AUTH_KEY, 1)
            sendIntent.putExtra(EMPTY_DATA_AUTH_KEY, company)
            startActivity(sendIntent)
        }
        binding.btnLogout.setOnClickListener {
            if (goHipePreferences.getCompanyPreference().isLogin) {
                dialog.dialog(this, "Are you sure to logout ?") {
                    companyPreferenceModel.isLogin = false
                    companyPreferenceModel.level = ""
                    goHipePreferences.setCompanyPreference(companyPreferenceModel)

                    startActivity(Intent(this, MainScreenActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun initFragment(state: Boolean) {
        val hireAuthKey = intent.getIntExtra(HIRE_ADD_AUTH_KEY, -1)
        val projectAuthKey = intent.getIntExtra(PROJECT_ADD_AUTH_KEY, -1)
        val accAuthKey = intent.getIntExtra(ACC_UPDATE_AUTH_KEY, -1)
        val rl = binding.rlCheckProfileFrame

        when {
            hireAuthKey == 1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyHiringScreenFragment(binding.topAppBarMaincontentActivity, binding.coCompany, binding.bottomNavView, rl, state)).commit()
            }
            projectAuthKey == 1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyProjectScreenFragment(binding.topAppBarMaincontentActivity, binding.coCompany, binding.bottomNavView, rl, state)).commit()
            }
            accAuthKey == 1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyAccountScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.coCompany, rl, state)).commit()
            }
            else -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyHomeScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.coCompany, rl, state)).commit()
            }
        }
    }

    private fun changeFragmentScreen(state: Boolean) {
        val rl = binding.rlCheckProfileFrame

        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mn_item_maincontent_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyHomeScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.coCompany, rl, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanySearchScreenFragment(binding.coCompany, rl, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_hiring -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyHiringScreenFragment(binding.topAppBarMaincontentActivity, binding.coCompany, binding.bottomNavView, rl, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_project -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyProjectScreenFragment(binding.topAppBarMaincontentActivity, binding.coCompany, binding.bottomNavView, rl, state)).commit()
                    true
                }
                R.id.mn_item_maincontent_account -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyAccountScreenFragment(binding.topAppBarMaincontentActivity, binding.bottomNavView, binding.coCompany, rl, state)).commit()
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