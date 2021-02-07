package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityProfileScreenBinding
import com.istekno.gohipeandroidapp.maincontent.company.CompanyDetailHireScreenFragment
import com.istekno.gohipeandroidapp.maincontent.company.CompanyDetailProfileScreenFragment
import com.istekno.gohipeandroidapp.maincontent.company.CompanyDetailProjectScreenFragment
import com.istekno.gohipeandroidapp.maincontent.engineer.hire.EngineerDetailHireScreenFragment
import com.istekno.gohipeandroidapp.maincontent.engineer.profile.EngineerDetailProfileScreenFragment
import com.istekno.gohipeandroidapp.maincontent.engineer.EngineerDetailProjectScreenFragment
import com.istekno.gohipeandroidapp.utility.Dialog


class ProfileScreenActivity : AppCompatActivity() {

    companion object {
        const val HOME_AUTH_KEY = "home_auth_key"
        const val PROJECT_AUTH_KEY = "project_auth_key"
        const val HIRE_AUTH_KEY = "hire_auth_key"
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
        val authKeyHire = intent.getIntExtra(HIRE_AUTH_KEY, -1)

        if (authKeyHome == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, CompanyDetailProfileScreenFragment("iSSOG Corp", "issog.id@gmail.com")).commit()
        } else if (authKeyHome == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, EngineerDetailProfileScreenFragment()).commit()
        }

        if (authKeyProject == 1) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, CompanyDetailProjectScreenFragment()).commit()
        } else if (authKeyProject == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, EngineerDetailProjectScreenFragment()).commit()
        }

        authHire(authKeyHire)
    }

    private fun connectionCheck(context: Context) {
        val connectManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val checkNetwork = connectManager.activeNetworkInfo

        if (checkNetwork == null || !checkNetwork.isConnected || !checkNetwork.isAvailable) {
            dialog.dialogCheckInternet(this, this)
        }
    }

    private fun authHire(status: Int) {
        when (status) {
            0 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, CompanyDetailHireScreenFragment(0)).commit()
            }
            1 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, CompanyDetailHireScreenFragment(1)).commit()
            }
            2 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, CompanyDetailHireScreenFragment(2)).commit()
            }
            3 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, CompanyDetailHireScreenFragment(3)).commit()
            }
            10 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, EngineerDetailHireScreenFragment(0)).commit()
            }
            11 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, EngineerDetailHireScreenFragment(1)).commit()
            }
            12 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, EngineerDetailHireScreenFragment(2)).commit()
            }
            13 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, EngineerDetailHireScreenFragment(3)).commit()
            }
            15 -> {
                supportFragmentManager.beginTransaction().replace(R.id.frame_container_profileact, EngineerDetailHireScreenFragment(5)).commit()
            }
        }
    }
}