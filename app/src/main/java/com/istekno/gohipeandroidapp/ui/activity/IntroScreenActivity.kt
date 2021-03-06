package com.istekno.gohipeandroidapp.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.Dialog

class IntroScreenActivity : AppIntro() {

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        dialog = Dialog()

        connectionCheck(this)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )

        addSlide(AppIntroFragment.newInstance(
                title = "Material Design",
                description = "Realize Unimagine Interfaces",
                imageDrawable = R.drawable.ilustrate_intro1,
                backgroundDrawable = R.color.theme_green,
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE
        ))
        addSlide(AppIntroFragment.newInstance(
                title = "Advanced Developing",
                description = "Build Modern App & Updated Features",
                imageDrawable = R.drawable.ilustrate_intro2,
                backgroundDrawable = R.color.theme_green,
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE
        ))
        addSlide(AppIntroFragment.newInstance(
                title = "Let's Go Beyond",
                description = "Professional Team leading Powerful Apps",
                imageDrawable = R.drawable.ilustrate_intro3,
                backgroundDrawable = R.color.theme_green,
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE
        ))

        setTransformer(AppIntroPageTransformerType.Parallax(
                titleParallaxFactor = 7.0,
                imageParallaxFactor = -1.0,
                descriptionParallaxFactor = 20.0
        ))
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        startActivity(Intent(this, MainScreenActivity::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        startActivity(Intent(this, MainScreenActivity::class.java))
        finish()
    }

    private fun connectionCheck(context: Context) {
        val connectManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val checkNetwork = connectManager.activeNetworkInfo

        if (checkNetwork == null || !checkNetwork.isConnected || !checkNetwork.isAvailable) {
            dialog.dialogCheckInternet(this, this)
        }
    }
}