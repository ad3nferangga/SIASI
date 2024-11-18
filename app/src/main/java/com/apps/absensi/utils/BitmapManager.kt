package com.apps.absensi.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Created by apps Rivaldi on 17-10-2021
 * Youtube Channel : https://bit.ly/2PJMowZ
 * Github : https://github.com/appsRivaldi
 * Twitter : https://twitter.com/appsrvldi_
 * Instagram : https://www.instagram.com/appsdvls_
 * LinkedIn : https://www.linkedin.com/in/apps-rivaldi
 */

object BitmapManager {

    fun base64ToBitmap(base64: String): Bitmap {
        val decodedString = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}