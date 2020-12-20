package com.istekno.gohipeandroidapp.utility

import android.app.AlertDialog
import android.content.Context

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
}