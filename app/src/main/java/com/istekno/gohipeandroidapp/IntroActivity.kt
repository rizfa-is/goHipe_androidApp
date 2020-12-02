package com.istekno.gohipeandroidapp

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntroFragment.newInstance(
                title = "Material Design",
                description = "Realize Unimagine Interfaces",
                imageDrawable = R.drawable.ilustration_intro1,
                backgroundDrawable = R.color.theme_green,
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE
        ))
        addSlide(AppIntroFragment.newInstance(
                title = "Advanced Developing",
                description = "Build Modern App & Updated Features",
                imageDrawable = R.drawable.ilustration_intro2,
                backgroundDrawable = R.color.theme_green,
                titleColor = Color.WHITE,
                descriptionColor = Color.WHITE
        ))
        addSlide(AppIntroFragment.newInstance(
                title = "Let's Go Beyond",
                description = "Professional Team leading Powerful Apps",
                imageDrawable = R.drawable.ilustration_intro3,
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
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        finish()
    }
}