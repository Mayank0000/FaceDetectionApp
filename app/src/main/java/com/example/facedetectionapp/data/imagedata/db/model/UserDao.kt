package com.example.facedetectionapp.data.imagedata.db.model

import android.graphics.RectF
import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user where rectF=:rectF AND uri=:uri")
    fun getUser(rectF: String, uri: String): Flow<User>

    @Insert
    fun insert(users: User)

}