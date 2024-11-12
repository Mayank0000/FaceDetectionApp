package com.example.facedetectionapp.presentation

import android.graphics.RectF
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.facedetectionapp.domain.models.ImageUi
import com.example.facedetectionapp.domain.models.TagData
import com.example.facedetectionapp.domain.usecases.GetImageWithFacesUseCase
import com.example.facedetectionapp.domain.usecases.InsertTagUseCase
import com.example.facedetectionapp.presentation.models.ProgressBarState
import com.example.facedetectionapp.presentation.models.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getImageWithFacesUseCase: GetImageWithFacesUseCase,
    private val insertTagUseCase: InsertTagUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState())
    val uiState = _uiState as StateFlow<UiState>

    private val _showDialog: MutableStateFlow<UiEvents.ShowDialogBox> =
        MutableStateFlow(UiEvents.ShowDialogBox())
    val showDialog = _showDialog as StateFlow<UiEvents.ShowDialogBox>

    fun getData() {
        _uiState.value =
            _uiState.value.copy(progressBarState = ProgressBarState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            getImageWithFacesUseCase.invoke().cachedIn(viewModelScope).collectLatest {
                _uiState.value =
                    uiState.value.copy(
                        progressBarState = ProgressBarState.Idle,
                        list = MutableStateFlow(it)
                    )
            }
        }
    }


    fun clickButton(event: UiEvents) {
        when (event) {
            is UiEvents.UserTag -> {
                viewModelScope.async(Dispatchers.IO) {
                    insertTagUseCase.invoke(
                        TagData(
                            name = event.name,
                            rectF = event.rectF,
                            uri = event.image.uri
                        )
                    )
                }.invokeOnCompletion {
                    getData()
                }
            }

            is UiEvents.ShowDialogBox -> {
                _showDialog.value = UiEvents.ShowDialogBox(
                    bool = (event.bool),
                    name = event.name,
                    image = event.image
                )
            }
        }
    }
}


sealed class UiEvents {
    data class UserTag(
        val name: String,
        val rectF: RectF,
        val image: ImageUi
    ) : UiEvents()

    data class ShowDialogBox(
        val bool: MutableState<Boolean> = mutableStateOf(false),
        val name: RectF? = null,
        val image: ImageUi? = null
    ) : UiEvents()
}

