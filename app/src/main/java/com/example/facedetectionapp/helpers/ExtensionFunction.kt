package com.example.facedetectionapp.helpers

import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun ContentResolver.convertImage(
    uri: Uri,
    width: Int,
    height: Int,
): Bitmap {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        val localBitmap = MediaStore.Images.Media.getBitmap(this, uri)
            .copy(Bitmap.Config.ARGB_8888, false)
        Bitmap.createScaledBitmap(
            localBitmap, width, height, false
        )
    } else {
        val source = ImageDecoder.createSource(this, uri)
        val localBitmap = ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, false)
        Bitmap.createScaledBitmap(
            localBitmap, width, height, false
        )
    }
}

fun getWindowHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun getWindowWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}


fun RectF.valueEqual(rectF: RectF) : Boolean{
    return this.top == rectF.top && this.right == rectF.right && this.left == rectF.left && this.bottom == rectF.bottom
}