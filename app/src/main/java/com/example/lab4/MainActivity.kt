package com.example.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab4.ui.theme.Lab4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Lab4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    RequestInformation()
                    EnterCredentials()
                }
            }
        }
    }
}

@Composable
fun RequestInformation() {
    val isShowingDialog = remember {mutableStateOf(true)}
    val agreeCollectData = remember{mutableStateOf(false) }

    if(isShowingDialog.value) {
        AlertDialog(
            onDismissRequest = {
                isShowingDialog.value = false
               },
            title = { Text(text = "Personal Information") },
            text = { Text("Do you consent to have your personal information collected and stored by this device?") },       //This below causes a recomposition
            confirmButton = {
                Button(
                    onClick = {
                        isShowingDialog.value = false
                        agreeCollectData.value = true
                      },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff5cdb5c))
                    ) { Text("Accept") }
            },
            dismissButton = {
                Button(
                    onClick = {
                        isShowingDialog.value = false
                        agreeCollectData.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xffff0021))
                ) { Text("Decline") }
            }
        )
    }
}

@Composable
fun EnterCredentials() {
    val firstName = remember {mutableStateOf("") }
    val lastName = remember {mutableStateOf("") }
    val address = remember {mutableStateOf("") }

    Column {
        TextField(
            label = {Text("First Name")},
            value = firstName.value,
            onValueChange = {v -> firstName.value = v},
        )
        TextField(
            label = {Text("Last Name")},
            value = lastName.value,
            onValueChange = {v -> lastName.value = v},
        )
        TextField(
            label = {Text("Address")},
            value = address.value,
            onValueChange = {v -> address.value = v},
        )
    }
}