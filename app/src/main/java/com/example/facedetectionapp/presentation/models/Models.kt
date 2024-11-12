package com.example.facedetectionapp.presentation.models

import androidx.paging.PagingData
import com.example.facedetectionapp.domain.models.ImageUi
import kotlinx.coroutines.flow.MutableStateFlow

data class UiState(
    val progressBarState: ProgressBarState = ProgressBarState.Loading,
    val list: MutableStateFlow<PagingData<ImageUi>> = MutableStateFlow(PagingData.empty())
)

sealed class ProgressBarState {
    object Loading : ProgressBarState()
    object Idle : ProgressBarState()
}