package com.istekno.gohipeandroidapp.utility

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.istekno.gohipeandroidapp.R

class Dialog {
    fun dialog(context: Context?, message: String, listAction: () -> Unit) {
        val dialog = AlertDialog.Builder(context).apply {
            setTitle("Notice")
            setMessage(message)
            setCancelable(false)
            setPositiveButton("OK") { dialogInterface, i ->
                listAction()
            }
        }
        dialog.show()
    }

    fun dialogCancel(context: Context?, message: String) {
        val dialog = AlertDialog.Builder(context).apply {
            setTitle("Notice")
            setMessage(message)
            setCancelable(false)
            setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        }
        dialog.show()
    }

    fun dialogCheckInternet(context: Context?, activity: Activity) {
        val dialog = AlertDialog.Builder(context).apply {
            setTitle("Network Info")
            setMessage("No internet connection\nCheck your internet connectivity and try again")
            setIcon(R.drawable.ic_no_internet)
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
                activity.finishAffinity()
            }
        }
        dialog.show()
    }
}