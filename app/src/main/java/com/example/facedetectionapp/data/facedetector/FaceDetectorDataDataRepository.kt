package com.example.facedetectionapp.data.facedetector

import android.graphics.Bitmap
import android.net.Uri
import com.example.facedetectionapp.domain.models.ImageFaceDetectionData
import com.example.facedetectionapp.domain.repository.IFaceDetectorDataRepository
import com.example.facedetectionapp.domain.models.ImageUi
import com.example.facedetectionapp.helpers.Constant
import javax.inject.Inject
import javax.inject.Named

class FaceDetectorDataDataRepository @Inject constructor(
    @Named(Constant.MediaPipeFaceDetector)
    private val mediaPipeFaceDetector: IFaceDetectorDataRepository
) : IFaceDetectorDataRepository {

    override suspend fun faceDetectionScoreData(imagesData: Bitmap,  uri: Uri): ImageFaceDetectionData {
        return mediaPipeFaceDetector.faceDetectionScoreData(imagesData, uri)
    }
}