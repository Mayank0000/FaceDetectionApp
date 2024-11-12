package com.example.facedetectionapp.domain.usecases

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import com.example.facedetectionapp.domain.models.ImageUi
import com.example.facedetectionapp.domain.models.TagData
import com.example.facedetectionapp.helpers.valueEqual
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImageWithFacesUseCase @Inject constructor(
    private val getLocalImagesFromStorageUseCase: GetLocalImagesFromStorageUseCase,
    private val faceDetectionDataUseCase: FaceDetectionDataUseCase,
    private val convertBitmapUseCase: ConvertBitmapUseCase,
    private val getAllTagsUseCase: GetAllTagsUseCase
) {
    suspend operator fun invoke(): Flow<PagingData<ImageUi>> {
        val data = getLocalImagesFromStorageUseCase.invoke().map {
            it.map { imageData ->
                val localImage = convertBitmapUseCase.invoke(imageData)
                faceDetectionDataUseCase.invoke(localImage, imageData.uri)
            }.filter {
                it.rectF.isNotEmpty()
            }
        }
        return data.map {
            it.map { imageData ->
                val tagList = arrayListOf<TagData>()
                imageData.rectF.map { rectF ->
                    getAllTagsUseCase.invoke().map {
                        if (it.uri == imageData.uri && it.rectF.valueEqual(rectF))
                            tagList.add(it)
                        else {
                            tagList.add(
                                TagData(
                                    rectF = rectF,
                                    name = "",
                                    uri = imageData.uri
                                )
                            )
                        }
                    }.ifEmpty {
                        tagList.add(
                            TagData(
                                rectF = rectF,
                                name = "",
                                uri = imageData.uri
                            )
                        )
                    }
                }
                ImageUi(
                    uri = imageData.uri,
                    icon = imageData.icon,
                    rectF = tagList
                )
            }
        }
    }
}