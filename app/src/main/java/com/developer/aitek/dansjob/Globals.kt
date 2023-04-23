package com.developer.aitek.dansjob

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


var tempID: String = ""
var tempDeviceID: String = ""

@SuppressLint("PackageManagerGetSignatures")
fun printHashKey(pContext: Context) {
    try {
        val info: PackageInfo = pContext.packageManager
            .getPackageInfo(pContext.packageName, PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            val hashKey: String = String(Base64.encode(md.digest(), 0))
            Log.i("WKWKWKW", "printHashKey() Hash Key: $hashKey")
        }
    } catch (e: NoSuchAlgorithmException) {
        Log.e("WKWKWKW", "printHashKey()", e)
    } catch (e: Exception) {
        Log.e("WKWKWKW", "printHashKey()", e)
    }
}