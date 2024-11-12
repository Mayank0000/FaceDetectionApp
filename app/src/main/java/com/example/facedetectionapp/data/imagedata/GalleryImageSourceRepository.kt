package com.example.facedetectionapp.data.imagedata

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.example.facedetectionapp.domain.models.ImagesData
import javax.inject.Inject


class GalleryImageSourceRepository @Inject constructor(
    private val contentResolver: ContentResolver
) : ILocalImageSourceRepository {

    private var cursor: Cursor? = null
    private var columnIndexId: Int = 0

    override suspend fun loadImagesFromStorage(
        prevIndex: Int,
        pageSize: Int
    ): List<ImagesData> {
        val listOfAllImages = mutableListOf<ImagesData>()
        try {
            cursor = contentResolver
                .query(
                    uri,
                    projection,
                    null,
                    null,
                    "$orderBy DESC"
                )
            cursor?.let { cursor ->
                cursor.moveToPosition(prevIndex)
                columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext() && cursor.position < (prevIndex + pageSize)) {
                    val contentUri = ContentUris.withAppendedId(uri, cursor.getLong(columnIndexId))
                    try {
                        listOfAllImages.add(ImagesData(contentUri))
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
                cursor.close()
            }
        } catch (e: Exception) {
            //send failure from here
        }
        return listOfAllImages
    }

    companion object {
        private val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        private val projection = arrayOf(MediaStore.Images.Media._ID)
        private const val orderBy = MediaStore.Images.Media.DATE_TAKEN
    }
}