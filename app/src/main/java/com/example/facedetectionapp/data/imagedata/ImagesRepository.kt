package com.example.facedetectionapp.data.imagedata

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.facedetectionapp.data.imagedata.db.LocalTagsRepository
import com.example.facedetectionapp.domain.repository.IImagesRepository
import com.example.facedetectionapp.domain.models.ImagesData
import com.example.facedetectionapp.domain.models.TagData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImagesRepository @Inject constructor(
    private val localImageSourceRepository: ILocalImageSourceRepository,
    private val pageConfig: PagingConfig,
    private val localTagsRepository: LocalTagsRepository
) : IImagesRepository {

    override suspend fun loadImagesFromStorage(): Flow<PagingData<ImagesData>> {
        return Pager(
            config = pageConfig,
            pagingSourceFactory = { ImagesPagingSource(localImageSourceRepository) }
        ).flow
    }

    override suspend fun getAllTag():List<TagData> {
        return localTagsRepository.getAllTag()
    }

    override suspend fun insert(data: TagData) {
        localTagsRepository.insert(data)
    }

}