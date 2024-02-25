package com.androidsignalstrength

import android.telephony.TelephonyManager
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import android.content.Context
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.SignalStrength
import com.facebook.react.bridge.Promise

class AndroidSignalStrengthModule(private val reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  private var telephonyManager: TelephonyManager? = null
  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun getCurrentSignalStrength(promise: Promise) {
      telephonyManager = reactContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
      getCellSignalLevel(promise)
  }

  private fun getCellSignalLevel(promise: Promise) {
    // API level 23 and above
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      promise.resolve(telephonyManager?.signalStrength?.level)
    } else {
      getCellSignalLevelLegacy(promise)
    }
  }

  // For API level 22 and below
  private fun getCellSignalLevelLegacy (promise: Promise) {
    val phoneStateListener = object : PhoneStateListener() {
      override fun onSignalStrengthsChanged(signalStrength: SignalStrength) {
        super.onSignalStrengthsChanged(signalStrength)
        val signalLevel = getSignalLevelLegacy(signalStrength)
        promise.resolve(signalLevel)
        telephonyManager?.listen(this, PhoneStateListener.LISTEN_NONE)
      }
    }
    telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS)
  }

  private fun getSignalLevelLegacy(signalStrength: SignalStrength): Int {
    val signalStrengthValue = if (signalStrength.isGsm) {
      // For GSM networks
      signalStrength.gsmSignalStrength * 2 - 113
    } else {
      // For CDMA networks
      signalStrength.cdmaDbm
    }
    // Map the signal strength value to a level manually
    return when {
      signalStrengthValue >= -70 -> 4
      signalStrengthValue >= -85 -> 3
      signalStrengthValue >= -100 -> 2
      signalStrengthValue >= -110 -> 1
      else -> 0
    }
  }

  companion object {
    const val NAME = "AndroidSignalStrength"
  }
}
