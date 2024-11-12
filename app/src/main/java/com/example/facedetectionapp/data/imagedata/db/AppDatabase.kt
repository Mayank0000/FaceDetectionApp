package com.example.facedetectionapp.data.imagedata.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.facedetectionapp.data.imagedata.db.model.User
import com.example.facedetectionapp.data.imagedata.db.model.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}