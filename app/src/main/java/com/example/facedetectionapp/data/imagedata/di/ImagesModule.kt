package com.example.facedetectionapp.data.imagedata.di

import android.content.ContentResolver
import androidx.paging.PagingConfig
import com.example.facedetectionapp.configuration.Config
import com.example.facedetectionapp.data.imagedata.ILocalImageSourceRepository
import com.example.facedetectionapp.data.imagedata.ImagesRepository
import com.example.facedetectionapp.data.imagedata.GalleryImageSourceRepository
import com.example.facedetectionapp.data.imagedata.db.LocalTagsRepository
import com.example.facedetectionapp.domain.repository.IImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ImagesModule {

    @Provides
    fun provideFaceDetectionRepository(
        localImageSourceRepository: ILocalImageSourceRepository,
        pagingConfig: PagingConfig,
        localTagsRepository: LocalTagsRepository
    ): IImagesRepository {
        return ImagesRepository(localImageSourceRepository, pagingConfig, localTagsRepository)
    }

    @Provides
    fun getPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = Config.PREFETCH_PAGE_SIZE,
            enablePlaceholders = false
        )
    }

    @Provides
    fun provideLocalImageSourceRepository(
        contentResolver: ContentResolver
    ): ILocalImageSourceRepository {
        return GalleryImageSourceRepository(contentResolver)
    }
}