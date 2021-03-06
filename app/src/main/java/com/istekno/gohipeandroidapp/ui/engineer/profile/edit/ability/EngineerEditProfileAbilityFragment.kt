package com.istekno.gohipeandroidapp.ui.engineer.profile.edit.ability

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText
import com.istekno.gohipeandroidapp.R
import com.istekno.gohipeandroidapp.ui.activity.SettingScreenActivity
import com.istekno.gohipeandroidapp.databinding.FragmentEngineerEditProfileAbilityBinding
import com.istekno.gohipeandroidapp.ui.engineer.account.EngineerAccountScreenFragment
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.ApiClient
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.Ability
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.EngineerGetByIDResponse
import com.istekno.gohipeandroidapp.data.source.remote.retrofit.GoHipeApiService
import com.istekno.gohipeandroidapp.ui.Dialog
import com.istekno.gohipeandroidapp.utility.GoHipePreferences
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.*

class EngineerEditProfileAbilityFragment : Fragment() {

    companion object {
        const val FIELD_REQUIRED = "Field must not empty"
        const val EDIT_AB_KEY = "edit_ability_key"

        @Parcelize
        data class EditAbilityModel(val id: Long, val name: String?): Parcelable
    }

    private lateinit var binding: FragmentEngineerEditProfileAbilityBinding
    private lateinit var coroutineScope: CoroutineScope
    private lateinit var service: GoHipeApiService
    private lateinit var goHipePreferences: GoHipePreferences
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_engineer_edit_profile_ability, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        service = ApiClient.getApiClient(view.context)!!.create(GoHipeApiService::class.java)
        goHipePreferences = GoHipePreferences(view.context)
        dialog = Dialog()

        getAllAbility(view)
        binding.fabEditab.setOnClickListener {
            val sendIntent = Intent(context, SettingScreenActivity::class.java)
            sendIntent.putExtra(EngineerAccountScreenFragment.EDIT_PROFILE_AUTH_KEY, 10)
            startActivity(sendIntent)
        }
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            getAllAbility(view)
        }
    }

    private fun getAllAbility(view: View) {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().acID

            binding.cgEnaccfrgAbility.removeAllViews()

            binding.imageView.visibility = View.GONE
            binding.tvAbility.visibility = View.GONE
            binding.fabEditab.visibility = View.GONE
            binding.swipeRefresh.isRefreshing = false
            binding.pgEditabengfrg.visibility = View.VISIBLE
            val result = withContext(Dispatchers.IO) {
                try {
                    service.getEngineerByID(id!!.toLong())
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (result is EngineerGetByIDResponse) {

                if (result.database?.get(0)?.enAbilityList!!.isEmpty()) {
                    binding.imageView.visibility = View.VISIBLE
                    binding.tvAbility.visibility = View.VISIBLE
                }

                chipViewInit(view, result.database[0].enAbilityList!!)

                binding.fabEditab.visibility = View.VISIBLE
                binding.pgEditabengfrg.visibility = View.GONE
            }
        }
    }

    private fun addAbility(abName: String) {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().engID

            withContext(Dispatchers.IO) {
                try {
                    service.addABility(id!!.toLong(), abName)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateAbility(abID: Long, abName: String) {
        coroutineScope.launch {
            val id = goHipePreferences.getEngineerPreference().engID

            withContext(Dispatchers.IO) {
                try {
                    service.updateABility(abID, id!!.toLong(), abName)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun deleteAbility(abID: Long) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    service.deleteAbility(abID)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun chipViewInit(view: View, listAbilities: ArrayList<Ability>) {
        for (i in 0 until listAbilities.size) {
            val chip = Chip(view.context)
            chip.isCloseIconVisible = true
            chip.chipCornerRadius = 30F
            chip.chipBackgroundColor = resources.getColorStateList(R.color.theme_orange)
            chip.text = listAbilities[i].abName
            chip.setTextColor(resources.getColor(R.color.white))
            chip.setOnClickListener {
                val sendIntent = Intent(context, SettingScreenActivity::class.java)
                sendIntent.putExtra(EngineerAccountScreenFragment.EDIT_PROFILE_AUTH_KEY, 20)
                sendIntent.putExtra(EDIT_AB_KEY, EditAbilityModel(listAbilities[i].abID, listAbilities[i].abName))
                startActivity(sendIntent)
            }
            chip.setOnCloseIconClickListener {
                dialog.dialog(view.context, "Are you sure delete ${listAbilities[i].abName}") {
                    dialog.dialogCancel(view.context, "${listAbilities[i].abName} deleted!") {
                        deleteAbility(listAbilities[i].abID)
                    }
                }
            }
            binding.cgEnaccfrgAbility.addView(chip)
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun setFormDialog(view: View, type: Int, id: Long, name: String) {
        val dialog = AlertDialog.Builder(view.context)
        val customView = LayoutInflater.from(view.context).inflate(R.layout.fragment_engineer_addupdate_ability_dialog, null)
        val header = customView.findViewById<TextView>(R.id.tv_header)
        val btnAdd = customView.findViewById<MaterialButton>(R.id.btn_addab)
        val form = customView.findViewById<TextInputEditText>(R.id.et_addab_name)
        val cDialog = dialog.setView(customView).create()

        if (type == 1) {
            header.text = "Update ability"
            btnAdd.text = "Update"
            form.setText(name)
        }

        cDialog.show()
        btnAdd.setOnClickListener {
            val input = form.text.toString()

            if (input.isEmpty()) {
                showToast(view, FIELD_REQUIRED)
                return@setOnClickListener
            }

            if (type == 1) {

                updateAbility(id, input)
                Toast.makeText(view.context, "Success update ability", Toast.LENGTH_SHORT).show()
                cDialog.cancel()
                binding.swipeRefresh.isRefreshing = true
                binding.pgEditabengfrg.visibility = View.VISIBLE
                binding.cgEnaccfrgAbility.removeAllViews()
                getAllAbility(view)

            } else {

                addAbility(input)
                Toast.makeText(view.context, "Success add ability", Toast.LENGTH_SHORT).show()
                cDialog.cancel()
                binding.swipeRefresh.isRefreshing = true
                binding.pgEditabengfrg.visibility = View.VISIBLE
                binding.cgEnaccfrgAbility.removeAllViews()
                getAllAbility(view)
            }
        }
    }

    private fun showToast(view: View, msg: String) {
        Toast.makeText(view.context, msg, Toast.LENGTH_LONG).show()
    }

    private fun fragmentProperties(mFragment: Fragment) {
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.frame_container_setact, mFragment)
            commit()
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}