package com.example.facedetectionapp.data.imagedata

import com.example.facedetectionapp.domain.models.ImagesData

interface ILocalImageSourceRepository {
    suspend fun loadImagesFromStorage(
        prevIndex: Int,
        pageSize: Int
    ): List<ImagesData>
}