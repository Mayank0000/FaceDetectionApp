package com.example.facedetectionapp.domain.models

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri

data class ImagesData(
    val uri: Uri
)

data class ImageFaceDetectionData(
    val rectF: List<RectF>,
    val icon: Bitmap,
    val uri: Uri
)

data class ImageUi(
    val rectF: List<TagData>,
    val icon: Bitmap,
    val uri: Uri
)

data class TagData(
    val rectF: RectF,
    val name: String,
    val uri: Uri
)
