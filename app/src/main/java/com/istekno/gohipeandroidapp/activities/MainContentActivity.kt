package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityMainContentBinding
import com.istekno.gohipeandroidapp.fragments.company.CompanyAccountScreenFragment
import com.istekno.gohipeandroidapp.fragments.company.CompanyChatScreenFragment
import com.istekno.gohipeandroidapp.fragments.company.CompanyHomeScreenFragment
import com.istekno.gohipeandroidapp.fragments.company.CompanySearchScreenFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerChatScreenFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerAccountScreenFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerHomeScreenFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerSearchScreenFragment
import com.istekno.gohipeandroidapp.utility.Dialog

class MainContentActivity : AppCompatActivity() {

    companion object {
        const val LOGIN_AUTH_KEY = "login_auth_key"
        const val REGISTRATION_AUTH_KEY = "registration_auth_key"
        const val SPLASH_AUTH_KEY = "splash_auth_key"
    }

    private lateinit var binding: ActivityMainContentBinding
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_content)
        setSupportActionBar(binding.topAppBarMaincontentActivity)
        dialog = Dialog()

        connectionCheck(this)
        initFragment()
        changeFragmentScreen()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maincontent_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initFragment() {
        val authKeyLogin = intent.getIntExtra(LOGIN_AUTH_KEY, -1)
        val authKeyRegister = intent.getIntExtra(REGISTRATION_AUTH_KEY, -1)
        val authKeySplash = intent.getIntExtra(SPLASH_AUTH_KEY, -1)

        if (authKeyLogin == 0 || authKeyRegister == 0 || authKeySplash == 0) {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHomeScreenFragment(binding.topAppBarMaincontentActivity)).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyHomeScreenFragment(binding.topAppBarMaincontentActivity)).commit()
        }
    }

    private fun changeFragmentScreen() {
        val authKeyLogin = intent.getIntExtra(LOGIN_AUTH_KEY, -1)
        val authKeyRegister = intent.getIntExtra(REGISTRATION_AUTH_KEY, -1)
        val authKeySplash = intent.getIntExtra(SPLASH_AUTH_KEY, -1)

        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mn_item_maincontent_home -> {
                    if (authKeyLogin == 0 || authKeyRegister == 0 || authKeySplash == 0) {
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHomeScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyHomeScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    }
                    true
                }
                R.id.mn_item_maincontent_search -> {
                    if (authKeyLogin == 0 || authKeyRegister == 0 || authKeySplash == 0) {
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerSearchScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanySearchScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    }
                    true
                }
                R.id.mn_item_maincontent_chat -> {
                    if (authKeyLogin == 0 || authKeyRegister == 0 || authKeySplash == 0) {
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerChatScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyChatScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    }
                    true
                }
                R.id.mn_item_maincontent_account -> {
                    if (authKeyLogin == 0 || authKeyRegister == 0 || authKeySplash == 0) {
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerAccountScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, CompanyAccountScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    }
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