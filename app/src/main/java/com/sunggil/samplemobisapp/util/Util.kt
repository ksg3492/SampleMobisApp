package com.sunggil.samplemobisapp.util

import android.content.Context
import android.provider.Settings
import android.util.Base64.DEFAULT
import android.util.Log
import com.sunggil.samplemobisapp.data.model.melon.MelonStatus
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*

class Util {
    companion object {

        fun checkPCID(context : Context) {
            val installation = File(context.filesDir, "INSTALLATION");

            try {
                if (!installation.exists()) {
                    writeInstallationFile(installation);
                }

                MelonStatus.pcid = readInstallationFile(installation)
            } catch (e : Exception) { }
        }

        fun checkAppVersion(context : Context) {
            try {
                MelonStatus.appVersion = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (e : Exception) { }
        }

        fun checkDeviceId(context : Context) {
            MelonStatus.deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun checkPhoneModel() {
            MelonStatus.phoneModel = android.os.Build.MODEL
        }

        fun checkDeviceInfos(context : Context) {
            checkPCID(context)
            checkAppVersion(context)
            checkDeviceId(context)
            checkPhoneModel()
        }

        @Throws(IOException::class)
        fun readInstallationFile(installation : File) : String {
            val f = RandomAccessFile(installation, "r")
            val bytes = ByteArray(f.length() as Int)
            f.readFully(bytes)
            f.close()
            return String(bytes)
        }

        @Throws(IOException::class)
        fun writeInstallationFile(installation : File) {
            val out = FileOutputStream(installation)
            val id = UUID.randomUUID().toString()
            out.write(id.toByteArray())
            out.close()
        }

        fun base64Encoded(str : String) : String {
            val targetBytes = str.toByteArray()
            return android.util.Base64.encodeToString(targetBytes, 0)
        }

        fun mapToStringParams(map : HashMap<String, String>?) : String {
            var urlParams = ""
            map?.let {
                urlParams += "?"
                for (key in map.keys) {
                    urlParams += "${key}=${map[key]}&"
                }

                urlParams = urlParams.substring(0, urlParams.length - 1)
            }

            return urlParams
        }
    }
}