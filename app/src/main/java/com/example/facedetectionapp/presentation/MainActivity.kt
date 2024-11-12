package com.example.facedetectionapp.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.facedetectionapp.R
import com.example.facedetectionapp.permission.ExternalStoragePermission
import com.example.facedetectionapp.permission.Permission
import com.example.facedetectionapp.permission.PermissionManager
import com.example.facedetectionapp.presentation.models.ProgressBarState
import com.example.facedetectionapp.presentation.theme.FaceDetectionAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val progressState = mainViewModel.uiState.collectAsState()
            val showDialog = mainViewModel.showDialog.collectAsState()
            FaceDetectionAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MyAlertDialog(showDialog.value, mainViewModel)
                    when (progressState.value.progressBarState) {
                        is ProgressBarState.Idle -> {
                            val data = progressState.value.list.collectAsLazyPagingItems()
                            val listState = rememberLazyListState()
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                state = listState
                            ) {
                                this.item {
                                    for (i in 0 until data.itemCount) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Yellow),
                                        ) {
                                            AsyncImage(
                                                model = data[i]?.icon,
                                                contentDescription = stringResource(id = R.string.app_name),
                                            )
                                            val textMeasurer = rememberTextMeasurer()
                                            data[i]?.rectF?.forEach { rectF ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(width = 800.dp, height = 250.dp)
                                                        .drawBehind {
                                                            drawRect(
                                                                style = Stroke(
                                                                    width = 4f,
                                                                    pathEffect = PathEffect.dashPathEffect(
                                                                        intervals = values,
                                                                        phase = 4f
                                                                    )
                                                                ),
                                                                color = Color.Red,
                                                                size = Size(
                                                                    rectF.rectF.width(),
                                                                    rectF.rectF.height()
                                                                ),
                                                                topLeft = Offset(
                                                                    rectF.rectF.left,
                                                                    rectF.rectF.top
                                                                )
                                                            )

                                                            drawText(
                                                                textMeasurer = textMeasurer,
                                                                text = rectF.name,
                                                                style = TextStyle(
                                                                    fontSize = 24.sp,
                                                                    color = Color.Yellow
                                                                ),
                                                                topLeft = Offset(
                                                                    rectF.rectF.left,
                                                                    rectF.rectF.top
                                                                )
                                                            )
                                                        }
                                                        .pointerInput(Unit) {
                                                            detectTapGestures(
                                                                onTap = { tapOffset ->
                                                                    data[i]?.rectF?.forEach { rectF ->
                                                                        if (rectF.rectF.top < tapOffset.y && rectF.rectF.bottom > tapOffset.y
                                                                            && rectF.rectF.left < tapOffset.x && rectF.rectF.right > tapOffset.x
                                                                        ) {
                                                                            mainViewModel.clickButton(
                                                                                UiEvents.ShowDialogBox(
                                                                                    mutableStateOf(
                                                                                        true
                                                                                    ),
                                                                                    rectF.rectF,
                                                                                    data[i]
                                                                                )
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            )
                                                        }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        ProgressBarState.Loading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .progressSemantics()
                                        .size(60.dp),
                                    color = Color.Blue,
                                    strokeWidth = 6.dp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        PermissionManager.from(this, ExternalStoragePermission)
            .permissionDeniedMessage(getString(R.string.gallery_permission_denied_permanently))
            .permissionPermanentlyDeniedMessage(getString(R.string.permission_description_permanently))
            .observe(object : PermissionManager.IPermissionResult {
                override fun permissionGranted(permission: Permission) {
                    mainViewModel.getData()
                }
            })
            .request()
    }

    companion object {
        private val values = floatArrayOf(15.0f, 10.0f)
    }
}