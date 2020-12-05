package com.istekno.gohipeandroidapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.istekno.gohipeandroidapp.R
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreenActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )

        btn_mainact_login.setOnClickListener(this)
        btn_mainact_register.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_mainact_login -> {
                val intent = Intent(this, LoginRegisterActivity::class.java)
                intent.putExtra("Codename", 0)
                startActivity(intent)
            }
            R.id.btn_mainact_register -> {
                val intent = Intent(this, LoginRegisterActivity::class.java)
                intent.putExtra("Codename", 1)
                startActivity(intent)
            }
        }
    }
}