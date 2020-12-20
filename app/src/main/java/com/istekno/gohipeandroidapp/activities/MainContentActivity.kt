package com.istekno.gohipeandroidapp.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityMainContentBinding
import com.istekno.gohipeandroidapp.fragments.ChatFragment
import com.istekno.gohipeandroidapp.fragments.EngineerAccountScreenFragment
import com.istekno.gohipeandroidapp.fragments.HomeFragment
import com.istekno.gohipeandroidapp.fragments.SearchFragment

class MainContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_content)
        setSupportActionBar(binding.topAppBarMaincontentActivity)

        supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, HomeFragment(binding.topAppBarMaincontentActivity)).commit()
        changeFragmentScreen()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_maincontent_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun changeFragmentScreen() {
        binding.bottomNavView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mn_item_maincontent_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, HomeFragment(binding.topAppBarMaincontentActivity)).commit()
                    true
                }
                R.id.mn_item_maincontent_search -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, SearchFragment(binding.topAppBarMaincontentActivity)).commit()
                    true
                }
                R.id.mn_item_maincontent_chat -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_container_maincontent, ChatFragment(binding.topAppBarMaincontentActivity)).commit()
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
}