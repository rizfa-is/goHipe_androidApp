package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityProfileScreenBinding
import com.istekno.gohipeandroidapp.fragments.company.CompanyDetailProfileScreenFragment
import com.istekno.gohipeandroidapp.fragments.company.CompanyDetailProjectScreenFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerDetailProfileScreenFragment
import com.istekno.gohipeandroidapp.utility.Dialog

class ProfileScreenActivity : AppCompatActivity() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
        const val PROJECT_AUTH_KEY = "project_auth_key"
    }

    private lateinit var binding: ActivityProfileScreenBinding
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_screen)
        supportActionBar?.hide()
        dialog = Dialog()

        connectionCheck(this)
        initFragment()
        binding.topAppBarProfileact.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initFragment() {
        val authKeyHome = intent.getIntExtra(HOME_AUTH_KEY, -1)
        val authKeyProject = intent.getIntExtra(PROJECT_AUTH_KEY, -1)

        if (authKeyHome == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, CompanyDetailProfileScreenFragment("iSSOG Corp", "issog.id@gmail.com")).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, EngineerDetailProfileScreenFragment("Monkey D Luffy", "monkeyD@gmail.com")).commit()
        }

        if (authKeyProject == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, CompanyDetailProjectScreenFragment()).commit()
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