package com.transformers.test.contracts

interface AppLogger {
    fun logD(tag: String, message: String)

    fun logE(tag: String, message: String)

    fun logV(tag: String, message: String)

    fun logI(tag: String, message: String)
}