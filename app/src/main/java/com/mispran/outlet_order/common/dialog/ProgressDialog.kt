package com.mispran.outlet_order.common.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.github.ybq.android.spinkit.sprite.Sprite
import com.mispran.outlet_order.databinding.DialogCustomLoaderBinding

class ProgressDialog(builder: Builder) : Dialog(builder.context) {

    private var _binding: DialogCustomLoaderBinding? = null
    private val binding
        get() = _binding!!

    init {
        _binding = DialogCustomLoaderBinding.inflate(LayoutInflater.from(builder.context))
        builder.progressColor?.let { color ->
            binding.spinner.setColor(color)
        }

        if (!builder.cancelable) {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }

        builder.style?.let { style ->
            binding.spinner.setIndeterminateDrawable(style)
        }

        setCancelable(false)
        this.setContentView(binding.root)
    }

    class Builder(val context: Context) {

        var progressColor :Int? = null
        var cancelable: Boolean = false
        var style: Sprite? = null

        fun setProgressColor(color: Int): Builder {
            progressColor = color
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        fun setStyle(style: Sprite): Builder {
            this.style = style
            return this
        }

        fun build(): ProgressDialog {
            return ProgressDialog(this)
        }
    }
}