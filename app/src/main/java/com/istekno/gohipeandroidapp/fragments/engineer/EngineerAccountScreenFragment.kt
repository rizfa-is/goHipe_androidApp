package com.istekno.gohipeandroidapp.fragments.engineer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.istekno.gohipeandroidapp.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.remote.ApiClient
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.activities.SettingScreenActivity
import com.istekno.gohipeandroidapp.adapter.EngineerProfileViewPagerAdapter
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerAccountScreenBinding
import com.istekno.gohipeandroidapp.retrofit.Ability
import com.istekno.gohipeandroidapp.retrofit.EngineerModelResponse
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.coroutines.*

class EngineerAccountScreenFragment(private val toolbar: MaterialToolbar, private val bottomNavigationView: BottomNavigationView,
                                    private val rl: RelativeLayout, private val co: CoordinatorLayout, private val state: Boolean) : Fragment() {

    companion object {
        const val SETTING_AUTH_KEY = "setting_auth_key"
        const val EDIT_PROFILE_AUTH_KEY = "edit_profile_auth_key"
        const val EDIT_PROFILE_AUTH_KEY2 = "edit_profile_auth_key2"

        const val imageLink = "http://107.22.89.131:7000/image/"
    }

    private lateinit var binding: FragmentEngineerAccountScreenBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private var engineerDetail = listOf<EngineerModelResponse>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setToolbar()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_account_screen, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)

        getAllEngineer(view)
        viewListener(view)
        setViewPager()
    }

    private fun getAllEngineer(view: View) {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().acID

            binding.pgAccengfrg.visibility = View.VISIBLE
            binding.swipeRefresh.isRefreshing = false
            binding.svEngaccfrg.visibility = View.GONE
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerByID(id!!.toLong())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {
                val list = result.database?.map {
                    EngineerModelResponse(it.enID, it.enName, it.enPhone, it.enJobTitle, it.enJobType, it.enLocation, it.enDesc, it.enEmail, it.enIG, it.enGithub, it.enGitlab, it.enAvatar)
                }

                binding.model = result.database!![0]
                Glide.with(view.context).load(imageLink + result.database[0].enAvatar).into(binding.imgEnaccfrgAvatar)
                chipViewInit(view, result.database[0].enAbilityList!!)
                if (list != null) {
                    engineerDetail = list
                }

                binding.svEngaccfrg.visibility = View.VISIBLE
                binding.pgAccengfrg.visibility = View.GONE
            }
        }
    }

    private fun viewListener(view: View) {
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
        binding.btnEnaccfrgEditprofile.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(EDIT_PROFILE_AUTH_KEY, 0)
            sendIntent.putExtra(EDIT_PROFILE_AUTH_KEY2, engineerDetail[0])
            startActivity(sendIntent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            binding.cgEnaccfrgAbility.removeAllViews()
            getAllEngineer(view)
        }
    }

    private fun setViewPager() {
        val enginerAccountPagerAdapter = EngineerProfileViewPagerAdapter(childFragmentManager)
        binding.vpEngprofiact.adapter = enginerAccountPagerAdapter
        binding.tlEngprofiact.setupWithViewPager(binding.vpEngprofiact)
    }

    private fun chipViewInit(view: View, listAbilities: ArrayList<Ability>) {
        for (i in 0 until listAbilities.size) {
            val chip = Chip(view.context)
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbilities[i].abName
            chip.setTextColor(resources.getColor(R.color.white))

            binding.cgEnaccfrgAbility.addView(chip)
        }
    }

    private fun setToolbar() {
        if (state) {
            co.visibility = View.VISIBLE
            rl.visibility = View.GONE
        } else {
            co.visibility = View.GONE
            rl.visibility = View.VISIBLE
        }

        bottomNavigationView.visibility = View.VISIBLE
        bottomNavigationView.menu.findItem(R.id.mn_item_maincontent_account).isChecked = true
        toolbar.title = "My Account"
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_setting).isVisible = true
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_favorite).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_chat).isVisible = false
        toolbar.menu.findItem(R.id.mn_maincontent_toolbar_notification).isVisible = false
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}