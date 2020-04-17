package com.kulguy.pintarsehat.dialog

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import com.kulguy.pintarsehat.R

class LoadingDialog(private val activity: Activity) {
    private var dialog: AlertDialog? = null

    fun showDialog(){
        if (dialog == null || !dialog!!.isShowing) {
            Log.w("Dialog", "show")
            val builder = AlertDialog.Builder(activity)
            val layoutInflater = activity.layoutInflater
            builder.setView(layoutInflater.inflate(R.layout.dialog_loading, null))
            builder.setCancelable(false)
            dialog = builder.create()
            dialog?.show()
        }
    }

    fun dismissDialog(){
        dialog?.dismiss()
        Log.w("Dialog", "dismissed")
    }
}