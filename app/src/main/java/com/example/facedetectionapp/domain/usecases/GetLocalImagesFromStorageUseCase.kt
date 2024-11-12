package com.example.facedetectionapp.domain.usecases

import androidx.paging.PagingData
import com.example.facedetectionapp.domain.models.ImagesData
import com.example.facedetectionapp.domain.repository.IImagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalImagesFromStorageUseCase @Inject constructor(
    private val imagesRepository: IImagesRepository
) {
    suspend operator fun invoke(): Flow<PagingData<ImagesData>> {
        return imagesRepository.loadImagesFromStorage()
    }
}