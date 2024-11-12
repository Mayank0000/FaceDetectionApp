package com.example.facedetectionapp.data.imagedata.db

import android.graphics.RectF
import androidx.core.net.toUri
import com.example.facedetectionapp.data.imagedata.db.model.User
import com.example.facedetectionapp.domain.models.TagData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalTagsRepository @Inject constructor(
    private val appDatabase: AppDatabase
) : ILocalTagRepository {

    override suspend fun getAllTag(): List<TagData> {
        return withContext(Dispatchers.IO) {
            val list = arrayListOf<TagData>()
            appDatabase.userDao().getAll()
                .map {
                    val rectItem = RectF()
                    val rectF = it.rectF
                    val items = rectF.split(",")
                    rectItem.apply {
                        left = items[0].toFloat()
                        top = items[1].toFloat()
                        right = items[2].toFloat()
                        bottom = items[3].toFloat()
                    }
                    list.add(
                        TagData(
                            name = it.name,
                            uri = it.uri.toUri(),
                            rectF = rectItem
                        )
                    )
                }
            list
        }
    }

    override suspend fun insert(data: TagData) {
        withContext(Dispatchers.IO) {
            appDatabase.userDao().insert(
                User(
                    name = data.name,
                    uri = data.uri.toString(),
                    rectF = data.rectF.getConvertedRectF()
                )
            )
        }
    }

    private fun RectF.getConvertedRectF(): String {
        return "${left},${top},${right},${bottom}"
    }
}