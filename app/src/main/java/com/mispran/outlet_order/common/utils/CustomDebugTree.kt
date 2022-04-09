package com.mispran.outlet_order.common.utils

import timber.log.Timber
/*
* Custom Debug Tree for Timber
**/
class CustomDebugTree: Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return "LOG: (${element.fileName}:${element.lineNumber})#${element.methodName}"
    }
}