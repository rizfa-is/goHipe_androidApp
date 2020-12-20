package com.istekno.gohipeandroidapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityMainScreenBinding

class MainScreenActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_screen)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )

        binding.btnMainactLogin.setOnClickListener(this)
        binding.btnMainactRegister.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_mainact_login -> {
                val intent = Intent(this, LoginRegisterActivity::class.java)
                intent.putExtra("Codename Logreg", 0)
                startActivity(intent)
            }
            R.id.btn_mainact_register -> {
                val intent = Intent(this, LoginRegisterActivity::class.java)
                intent.putExtra("Codename Logreg", 1)
                startActivity(intent)
            }
        }
    }
}