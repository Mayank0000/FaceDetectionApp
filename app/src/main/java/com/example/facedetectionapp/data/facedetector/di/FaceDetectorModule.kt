package com.example.facedetectionapp.data.facedetector.di

import android.app.Application
import com.example.facedetectionapp.configuration.Config
import com.example.facedetectionapp.data.facedetector.FaceDetectorDataDataRepository
import com.example.facedetectionapp.data.facedetector.MediaPipeFaceDetectorDataData
import com.example.facedetectionapp.domain.repository.IFaceDetectorDataRepository
import com.example.facedetectionapp.helpers.Constant
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
class FaceDetectorModule {

    @Provides
    @Named(Constant.FaceDetectorRepository)
    fun provideFaceDetectionRepository(
        @Named(Constant.MediaPipeFaceDetector) mediaPipeFaceDetector: IFaceDetectorDataRepository
    ): IFaceDetectorDataRepository {
        return FaceDetectorDataDataRepository(mediaPipeFaceDetector)
    }

    @Provides
    fun getFaceDetector(
        application: Application
    ): FaceDetector {
        val baseOption = BaseOptions.builder()
            .setModelAssetPath(Config.MODEL_NAME)
            .build()
        val optionsBuilder =
            FaceDetector.FaceDetectorOptions
                .builder()
                .setBaseOptions(baseOption)
                .setMinDetectionConfidence(Config.DETECTION_CONFIDENCE)
                .setRunningMode(RunningMode.IMAGE)
                .build()
        return FaceDetector.createFromOptions(application, optionsBuilder)
    }

    @Provides
    @Named(Constant.MediaPipeFaceDetector)
    fun provideMediaPipeFaceDetector(
        option: FaceDetector
    ): IFaceDetectorDataRepository {
        return MediaPipeFaceDetectorDataData(option)
    }
}