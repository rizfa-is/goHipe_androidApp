package com.istekno.gohipeandroidapp.ui.activity

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivitySettingScreenBinding
import com.istekno.gohipeandroidapp.ui.Dialog
import com.istekno.gohipeandroidapp.ui.company.hire.CompanyAddHireScreenFragment
import com.istekno.gohipeandroidapp.ui.company.hire.CompanyEditHireScreenFragment
import com.istekno.gohipeandroidapp.ui.company.profile.CompanyEditProfileAccountFragment
import com.istekno.gohipeandroidapp.ui.company.project.CompanyAddProjectScreenFragment
import com.istekno.gohipeandroidapp.ui.company.project.CompanyEditProjectScreenFragment
import com.istekno.gohipeandroidapp.ui.company.setting.CompanyAccountSettingFragment
import com.istekno.gohipeandroidapp.ui.engineer.setting.EngineerAccountSettingFragment
import com.istekno.gohipeandroidapp.ui.engineer.profile.edit.ability.EngineerAddAbilityFragment
import com.istekno.gohipeandroidapp.ui.engineer.profile.edit.EngineerEditProfileFragment

class SettingScreenActivity : AppCompatActivity() {

    companion object {
        const val SETTING_AUTH_KEY = "setting_auth_key"
        const val EDIT_PROFILE_AUTH_KEY = "edit_profile_auth_key"
        const val PROJECT_AUTH_KEY = "project_auth_key"
        const val HIRE_AUTH_KEY = "hire_auth_key"
    }

    private lateinit var binding: ActivitySettingScreenBinding
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting_screen)
        dialog = Dialog()
        setSupportActionBar(binding.topAppBarSetact)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        connectionCheck(this)
        initSettingFragment()
    }

    private fun initSettingFragment() {
        val authKeySetting = intent.getIntExtra(SETTING_AUTH_KEY, -1)
        val authKeyEdit = intent.getIntExtra(EDIT_PROFILE_AUTH_KEY, -1)
        val authKeyProject = intent.getIntExtra(PROJECT_AUTH_KEY, -1)
        val authKeyHire = intent.getIntExtra(HIRE_AUTH_KEY, -1)

        if (authKeySetting == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, EngineerAccountSettingFragment(binding.topAppBarSetact)).commit()
        } else if (authKeySetting == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, CompanyAccountSettingFragment(binding.topAppBarSetact)).commit()
        }

        when (authKeyEdit) {
            1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, CompanyEditProfileAccountFragment(binding.topAppBarSetact)).commit()
            }
            0 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, EngineerEditProfileFragment(binding.topAppBarSetact)).commit()
            }
            in 10..20 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, EngineerAddAbilityFragment(binding.topAppBarSetact)).commit()
            }
        }

        if (authKeyProject == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, CompanyAddProjectScreenFragment(binding.topAppBarSetact)).commit()
        } else if (authKeyProject == 12) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, CompanyEditProjectScreenFragment(binding.topAppBarSetact)).commit()
        }

        if (authKeyHire == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, CompanyAddHireScreenFragment(binding.topAppBarSetact)).commit()
        } else if (authKeyHire == 20 ) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, CompanyEditHireScreenFragment(binding.topAppBarSetact)).commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun connectionCheck(context: Context) {
        val connectManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val checkNetwork = connectManager.activeNetworkInfo

        if (checkNetwork == null || !checkNetwork.isConnected || !checkNetwork.isAvailable) {
            dialog.dialogCheckInternet(this, this)
        }
    }
}