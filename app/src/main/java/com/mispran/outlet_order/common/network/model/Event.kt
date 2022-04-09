package com.mispran.outlet_order.common.network.model

class Event<out R>(private val data: R) {

    var hasEventBeenHandled = false
        private set

    val content: R?
        get() = if (!hasEventBeenHandled) {
            hasEventBeenHandled = true
            data
        } else {
            null
        }

    val oldContent: R = data
}