package com.example.facedetectionapp.domain.usecases

import android.graphics.Bitmap
import android.net.Uri
import com.example.facedetectionapp.domain.models.ImageFaceDetectionData
import com.example.facedetectionapp.domain.models.ImageUi
import com.example.facedetectionapp.domain.repository.IFaceDetectorDataRepository
import com.example.facedetectionapp.helpers.Constant
import javax.inject.Inject
import javax.inject.Named

class FaceDetectionDataUseCase @Inject constructor(
    @Named(Constant.FaceDetectorRepository)
    private val faceDetectorRepository: IFaceDetectorDataRepository
) {
    suspend operator fun invoke(imagesData: Bitmap, uri: Uri): ImageFaceDetectionData {
        return faceDetectorRepository.faceDetectionScoreData(imagesData, uri)
    }
}