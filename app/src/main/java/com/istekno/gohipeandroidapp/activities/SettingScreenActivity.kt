package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivitySettingScreenBinding
import com.istekno.gohipeandroidapp.fragments.company.CompanyAccountSettingFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerAccountSettingFragment
import com.istekno.gohipeandroidapp.utility.Dialog

class SettingScreenActivity : AppCompatActivity() {

    companion object {
        const val SETTING_AUTH_KEY = "setting_auth_key"
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

        if (authKeySetting == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, EngineerAccountSettingFragment()).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_setact, CompanyAccountSettingFragment()).commit()
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