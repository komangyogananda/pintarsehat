package com.kulguy.pintarsehat.interfaces

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.kulguy.pintarsehat.fragments.LoadingDialogFragment

interface DialogInterface {

    val loadingDialog: LoadingDialogFragment

    fun showLoadingDialog(fragmentManager: FragmentManager){
        Log.w("Dialog", "FragmentInit")
        var fragmentTransaction = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag("dialog")
        if (prev != null){
            fragmentTransaction.remove(prev)
        }
        fragmentTransaction.addToBackStack(null)
        loadingDialog.show(fragmentTransaction, "dialog")
    }

    fun dismissLoadingDialog(fragmentManager: FragmentManager){
        loadingDialog.dismiss()
        var fragmentTransaction = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag("dialog")
        if (prev != null){
            fragmentTransaction.remove(prev)
        }
        Log.w("Dialog", "Dismissed")
    }
}