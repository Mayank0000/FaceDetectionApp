package com.example.facedetectionapp.domain.usecases

import android.content.ContentResolver
import android.graphics.Bitmap
import com.example.facedetectionapp.domain.models.ImagesData
import com.example.facedetectionapp.helpers.convertImage
import com.example.facedetectionapp.helpers.getWindowHeight
import com.example.facedetectionapp.helpers.getWindowWidth
import javax.inject.Inject

class ConvertBitmapUseCase @Inject constructor(
    private val contentResolver: ContentResolver
) {
    suspend operator fun invoke(imagesData: ImagesData): Bitmap {
        return contentResolver.convertImage(imagesData.uri, getWindowWidth(), getWindowHeight() / 3)
    }
}