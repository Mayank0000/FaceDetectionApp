package com.example.facedetectionapp.domain.usecases

import com.example.facedetectionapp.domain.models.TagData
import com.example.facedetectionapp.domain.repository.IImagesRepository
import javax.inject.Inject

class GetAllTagsUseCase@Inject constructor(
    private val imagesRepository: IImagesRepository
) {
    suspend operator fun invoke(): List<TagData> {
        return imagesRepository.getAllTag()
    }
}