package com.emreozcan.flightapp.util

import android.content.Context
import com.emreozcan.flightapp.util.Constants.Companion.APP_VERSION_CODE
import com.emreozcan.flightapp.util.Constants.Companion.FORCE_UPDATE_REQUIRED
import com.emreozcan.flightapp.util.Constants.Companion.LEAST_VERSION_CODE
import com.emreozcan.flightapp.util.Constants.Companion.STORE_URL
import com.emreozcan.flightapp.util.Constants.Companion.SUGGESTED_VERSION_CODE
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig


class ForceUpdateChecker(
    private var onUpdateNeedListener: OnUpdateNeedListener,
    private var context: Context
) {

    interface OnUpdateNeedListener {
        fun onUpdateNeed(updateUrl: String,isForce: Boolean)
    }

    companion object {
        fun with(context: Context): Builder {
            return Builder(context)
        }
    }

    fun check() {
        val remoteConfig = Firebase.remoteConfig
        val updateUrl = remoteConfig.getString(STORE_URL)
        val suggestedAppVersion = remoteConfig.getString(SUGGESTED_VERSION_CODE)
        val leastAppVersion = remoteConfig.getString(LEAST_VERSION_CODE)
        val isForced = remoteConfig.getBoolean(FORCE_UPDATE_REQUIRED)

        val string = APP_VERSION_CODE.toString() + suggestedAppVersion

        println(string)

        if (isForced) {
            if (APP_VERSION_CODE.toString() != suggestedAppVersion) {
                onUpdateNeedListener.onUpdateNeed(updateUrl,true)
            }

        }else if (APP_VERSION_CODE.toString() < leastAppVersion){
            onUpdateNeedListener.onUpdateNeed(updateUrl,true)
        }
        else if (APP_VERSION_CODE.toString() < suggestedAppVersion){
            onUpdateNeedListener.onUpdateNeed(updateUrl,false)
        }
    }
}

class Builder(
    private var context: Context
){
    private lateinit var onUpdateNeedListener: ForceUpdateChecker.OnUpdateNeedListener

    fun onUpdateNeeded(onUpdateNeedListener: ForceUpdateChecker.OnUpdateNeedListener): Builder{
        this.onUpdateNeedListener = onUpdateNeedListener
        return this
    }

    fun build() : ForceUpdateChecker{
        return ForceUpdateChecker(onUpdateNeedListener,context)
    }

    fun check(): ForceUpdateChecker{
        val forceUpdateChecker = build()
        forceUpdateChecker.check()

        return forceUpdateChecker
    }

}
























