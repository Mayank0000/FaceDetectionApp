package com.example.facedetectionapp.data.imagedata

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.facedetectionapp.configuration.Config
import com.example.facedetectionapp.domain.models.ImagesData
import javax.inject.Inject

class ImagesPagingSource @Inject constructor(
    private val localImageSourceRepository: ILocalImageSourceRepository
) : PagingSource<Int, ImagesData>() {

    override fun getRefreshKey(state: PagingState<Int, ImagesData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImagesData> {
        return try {
            val page = params.key ?: -1
            val response =
                localImageSourceRepository.loadImagesFromStorage(page, Config.PREFETCH_ITEM_SIZE)
            LoadResult.Page(
                data = response,
                prevKey = if (page == -1) null else page.minus(response.size),
                nextKey = if (response.isEmpty()) null else page.plus(response.size),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}