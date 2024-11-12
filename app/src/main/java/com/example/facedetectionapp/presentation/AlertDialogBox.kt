package com.example.facedetectionapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyAlertDialog(shouldShowDialog: UiEvents.ShowDialogBox, mainViewModel: MainViewModel) {
    if (shouldShowDialog.bool.value) { // 2
        val textFieldValue =
            remember { mutableStateOf("") }

        AlertDialog( // 3
            onDismissRequest = { // 4
                shouldShowDialog.bool.value = false
            },
            // 5

            title = { Text(text = "Please enter the name") },
            shape = RoundedCornerShape(12.dp),
            text = {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    OutlinedTextField(
                        enabled = true,
                        value = textFieldValue.value,
                        label = { Text("Test") },
                        onValueChange = {

                            textFieldValue.value = it
                        }
                    )
                }
            },
            confirmButton = { // 6
                Button(
                    onClick = {
                        mainViewModel.clickButton(
                            UiEvents.UserTag(
                                name = textFieldValue.value,
                                rectF = shouldShowDialog.name!!,
                                shouldShowDialog.image!!
                            )
                        )
                        shouldShowDialog.bool.value = false
                    }
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White
                    )
                }
            }
        )
    }
}