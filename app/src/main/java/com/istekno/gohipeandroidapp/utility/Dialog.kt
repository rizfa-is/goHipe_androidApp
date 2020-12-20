package com.istekno.gohipeandroidapp.utility

import android.app.AlertDialog
import android.content.Context

class Dialog {
    fun dialog(context: Context?, message: String) {
        val dialog = AlertDialog.Builder(context).apply {
            setTitle("Notice")
            setMessage(message)
            setPositiveButton("OK") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
        }
        dialog.show()
    }
}