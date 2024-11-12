package com.example.facedetectionapp.data.imagedata.db

import com.example.facedetectionapp.domain.models.TagData

interface ILocalTagRepository {
    suspend fun getAllTag(): List<TagData>
    suspend fun insert(data: TagData)
}