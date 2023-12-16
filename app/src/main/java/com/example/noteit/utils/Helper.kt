package com.example.noteit.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.noteit.R

class Helper {

    companion object{
        fun hideKeyboard(view: View){
            try {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }catch (e: Exception){
                   e.printStackTrace()
            }
        }
        private var mProgressDialog: Dialog? = null

        fun showCustomProgressBar(context: Context){
            mProgressDialog = Dialog(context)
            mProgressDialog?.let {
                it.setContentView(R.layout.custom_dialog_progress)
                it.show()
            }
        }
        fun dismissProgressBar(){
            mProgressDialog?.let{
                it.dismiss()
            }
        }
    }
}