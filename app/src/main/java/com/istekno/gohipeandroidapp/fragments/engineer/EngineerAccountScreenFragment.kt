package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.istekno.gohipeandroidapp.retrofit.EngineerResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.adapter.EngineerProfileViewPagerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAccountScreenBinding
import com.istekno.gohipeandroidapp.retrofit.EngineerModelRequest
import kotlinx.coroutines.*

class EngineerAccountScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView, private val co: CoordinatorLayout) : Fragment() {

    companion object {
        const val SETTING_AUTH_KEY = "setting_auth_key"
        const val EDIT_PROFILE_AUTH_KEY = "edit_profile_auth_key"

        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var binding: FragmentEngineerAccountScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar(toolbar, co)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_account_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient()!!.create(GoHipeApiService::class.java)
        getAllEngineer(view)

        toolbarListener()
        buttonListener()
        setViewPager()

    }

    fun getAllEngineer(view: View) {
        coroutineScope.launch {
            Log.d("android2", "Start: ${Thread.currentThread().name}")

            val result = withContext(Dispatchers.IO) {
                Log.d("android2", "CallApi: ${Thread.currentThread().name}")
                try {
                    service.getAllEngineer()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerResponse) {
                Log.d("android2", result.toString())
                val list = result.database?.map{
                    EngineerModelRequest(it.enID, it.enName, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar, it.enAbilityList, it.enPortfolioList, it.enExperienceList)
                }

                binding.model = list[0]
                Glide.with(view.context).load(imageLink + list[0].enAvatar).into(binding.imgEnaccfrgAvatar)
                chipViewInit(view, list[0].ability)
            }
        }
    }

    private fun toolbarListener() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.mn_maincontent_toolbar_favorite -> {
                    fragmentManager?.beginTransaction()?.replace(R.id.frame_container_maincontent, EngineerFavoriteScreenFragment(toolbar, bottomNavigationView))?.addToBackStack(null)?.commit()
                }
                R.id.mn_maincontent_toolbar_setting -> {
                    val sendIntent = Intent(context, SettingScreenActivity::class.java)
                    sendIntent.putExtra(SETTING_AUTH_KEY, 0)
                    startActivity(sendIntent)
                }
            }
            false
        }
    }

    private fun buttonListener() {
        binding.btnEnaccfrgEditprofile.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(EDIT_PROFILE_AUTH_KEY, 0)
            startActivity(sendIntent)
        }
    }

    private fun setViewPager() {
        val enginerAccountPagerAdapter = EngineerProfileViewPagerAdapter(childFragmentManager)
        binding.vpEngprofiact.adapter = enginerAccountPagerAdapter
        binding.tlEngprofiact.setupWithViewPager(binding.vpEngprofiact)
    }

    private fun chipViewInit(view: View, listAbilities: ArrayList<EngineerResponse.Ability>) {
        for (i in 0 until listAbilities.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbilities[i].abName
            chip.setTextColor(resources.getColor(R.color.white))

            binding.cgEnaccfrgAbility.addView(chip)
        }
    }

    private fun setToolbar(toolbar: MaterialToolbar, co: CoordinatorLayout) {
        bottomNavigationView.visibility = View.VISIBLE
        co.visibility = View.VISIBLE
        toolbar.title = "My Account"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = true
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = true
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}