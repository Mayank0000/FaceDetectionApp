package com.example.facedetectionapp.data.facedetector

import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import com.example.facedetectionapp.domain.models.ImageFaceDetectionData
import com.example.facedetectionapp.domain.repository.IFaceDetectorDataRepository
import com.example.facedetectionapp.domain.models.ImageUi
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import javax.inject.Inject

class MediaPipeFaceDetectorDataData @Inject constructor(
    private val option: FaceDetector
) : IFaceDetectorDataRepository {

    override suspend fun faceDetectionScoreData(imagesData: Bitmap, uri: Uri): ImageFaceDetectionData {
        val result = option.detect(BitmapImageBuilder(imagesData).build())
        val list = arrayListOf<RectF>()
        result.detections().map {
            list.add(it.boundingBox())
        }
        return ImageFaceDetectionData(
            rectF = list,
            icon = imagesData,
            uri = uri
        )
    }
}