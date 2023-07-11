package com.mohamednader.weatherway.Utilities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.PorterDuff
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import android.widget.TextView
import com.mohamednader.weatherway.R

class CustomProgress {

    lateinit var progressBar: ProgressBar
    lateinit var dialog: Dialog

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var customProgress: CustomProgress? = null
        fun getInstance(): CustomProgress {
            return customProgress ?: synchronized(this) {
                val instance = CustomProgress()
                customProgress = instance
                instance
            }
        }
    }

    fun showDialog(context: Context, message: String, cancelable: Boolean) {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.prograss_bar_dialog)
        progressBar = dialog.findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.getIndeterminateDrawable().setColorFilter(
            context.resources.getColor(R.color.dark), PorterDuff.Mode.SRC_IN
        )
        val progressText: TextView = dialog.findViewById<TextView>(R.id.progress_text)
        progressText.text = "" + message
        progressText.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE
        progressBar.isIndeterminate = true
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(cancelable)
        dialog.show()
    }


    fun hideProgress() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

}