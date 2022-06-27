package com.mihir.team360

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.view.Gravity
import android.view.LayoutInflater
import com.mihir.team360.R

class CustomProgressDialogue(context: Context) : Dialog(context) {
    init {
        val params = window!!.attributes
        params.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = params
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val view = LayoutInflater.from(context).inflate(R.layout.progress_bar_layout, null)
        setContentView(view)
        window!!.setBackgroundDrawable(context.getDrawable(R.drawable.progress_bg))
    }
}