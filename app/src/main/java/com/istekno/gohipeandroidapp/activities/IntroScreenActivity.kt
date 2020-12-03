package com.istekno.gohipeandroidapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.istekno.gohipeandroidapp.R

class IntroScreenActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
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
        setImmersiveMode()
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
}