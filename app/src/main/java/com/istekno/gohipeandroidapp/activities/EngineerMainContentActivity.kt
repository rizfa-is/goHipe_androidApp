package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityEngineerMainContentBinding
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerAccountScreenFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerChatScreenFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerHomeScreenFragment
import com.istekno.gohipeandroidapp.fragments.engineer.EngineerSearchScreenFragment
import com.istekno.gohipeandroidapp.utility.Dialog

class EngineerMainContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEngineerMainContentBinding
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_engineer_main_content)
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
        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHomeScreenFragment(binding.topAppBarMaincontentActivity)).commit()
    }

    private fun changeFragmentScreen() {

        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mn_item_maincontent_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerHomeScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    true
                }
                R.id.mn_item_maincontent_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerSearchScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    true
                }
                R.id.mn_item_maincontent_chat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerChatScreenFragment(binding.topAppBarMaincontentActivity)).commit()
                    true
                }
                R.id.mn_item_maincontent_account -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, EngineerAccountScreenFragment(binding.topAppBarMaincontentActivity)).commit()
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