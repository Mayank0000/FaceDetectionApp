package com.example.facedetectionapp.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.example.facedetectionapp.domain.models.ImageFaceDetectionData
import com.example.facedetectionapp.domain.models.ImageUi

interface IFaceDetectorDataRepository {
    suspend fun faceDetectionScoreData(imagesData: Bitmap, uri: Uri): ImageFaceDetectionData
}