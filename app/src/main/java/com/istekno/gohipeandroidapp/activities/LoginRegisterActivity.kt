package com.istekno.gohipeandroidapp.activities

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.databinding.ActivityLoginRegisterBinding
import com.istekno.gohipeandroidapp.maincontent.LoginScreenFragment
import com.istekno.gohipeandroidapp.maincontent.SelectRoleFragment
import com.istekno.gohipeandroidapp.utility.Dialog

class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginRegisterBinding
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_register)
        dialog = Dialog()
        supportActionBar?.hide()
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
        )


        connectionCheck(this)
        binding.topAppBarLogregact.setNavigationOnClickListener {
            onBackPressed()
        }

        bindFragment()
    }

    private fun bindFragment() {

        when (intent.getIntExtra("Codename Logreg", -1)) {
            0 -> {
                val fragment = supportFragmentManager.findFragmentByTag(LoginScreenFragment::class.java.simpleName)
                if (fragment !is LoginScreenFragment) {
                    supportFragmentManager.beginTransaction().add(R.id.frame_container_logregact, LoginScreenFragment()).commit()
                }
            }
            1 -> {
                val fragment = supportFragmentManager.findFragmentByTag(SelectRoleFragment::class.java.simpleName)
                if (fragment !is SelectRoleFragment) {
                    supportFragmentManager.beginTransaction().add(R.id.frame_container_logregact, SelectRoleFragment()).commit()
                }
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