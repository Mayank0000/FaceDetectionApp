package com.example.facedetectionapp.domain.repository

import androidx.paging.PagingData
import com.example.facedetectionapp.data.imagedata.db.ILocalTagRepository
import com.example.facedetectionapp.domain.models.ImagesData
import kotlinx.coroutines.flow.Flow

interface IImagesRepository : ILocalTagRepository {
    suspend fun loadImagesFromStorage(): Flow<PagingData<ImagesData>>
}