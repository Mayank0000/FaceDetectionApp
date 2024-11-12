package com.example.facedetectionapp.data.imagedata.db.di

import android.app.Application
import androidx.room.Room
import com.example.facedetectionapp.configuration.Config
import com.example.facedetectionapp.data.imagedata.db.AppDatabase
import com.example.facedetectionapp.data.imagedata.db.ILocalTagRepository
import com.example.facedetectionapp.data.imagedata.db.LocalTagsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDatabaseModule {

    @Provides
    @Singleton
    fun buildRoomDB(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            Config.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun getAllTagsRepository(
        appDatabase: AppDatabase
    ): ILocalTagRepository {
        return LocalTagsRepository(
            appDatabase = appDatabase
        )
    }


}