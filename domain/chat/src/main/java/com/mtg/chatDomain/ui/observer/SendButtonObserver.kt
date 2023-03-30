package com.mtg.chatDomain.ui.observer

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.mtg.chatDomain.R

class SendButtonObserver (private val button: ImageView) : TextWatcher {
    override fun onTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        if (charSequence.toString().trim().isNotEmpty()) {
            button.isEnabled = true
            button.setImageResource(R.drawable.baseline_send_24_green)
        } else {
            button.isEnabled = false
            button.setImageResource(R.drawable.baseline_send_24)
        }
    }

    override fun beforeTextChanged(charSequence: CharSequence?, i: Int, i1: Int, i2: Int) {}
    override fun afterTextChanged(editable: Editable) {}
}
