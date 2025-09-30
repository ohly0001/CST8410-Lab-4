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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.lab4.ui.theme.Lab4Theme
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Lab4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Startup()
                }
            }
        }
    }
}

@Composable
fun Startup() {
    val isShowingDialog = remember { mutableStateOf(true) }
    val agreeCollectData = remember{ mutableStateOf(false) }

    val context = LocalContext.current
    val mainKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    val sharedPreferences = EncryptedSharedPreferences.create(
        "CST8410-Lab4-Data" ,
        mainKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    val firstName = remember {mutableStateOf(sharedPreferences.getString("FIRST_NAME", "")) }
    val lastName = remember {mutableStateOf(sharedPreferences.getString("LAST_NAME", "")) }
    val address = remember {mutableStateOf(sharedPreferences.getString("ADDRESS", "")) }

    if (isShowingDialog.value) {
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

    Column {
        firstName.value?.let {
            TextField(
                label = {Text("First Name")},
                value = it,
                onValueChange = {v -> run {
                    firstName.value = v
                    if (agreeCollectData.value) {
                        sharedPreferences.edit()
                        {
                            putString("FIRST_NAME", v)
                            apply()
                        }
                    }
                }},
            )
        }
        lastName.value?.let {
            TextField(
                label = {Text("Last Name")},
                value = it,
                onValueChange = {v -> run {
                    lastName.value = v
                    if (agreeCollectData.value) {
                        sharedPreferences.edit()
                        {
                            putString("LAST_NAME", v)
                            apply()
                        }
                    }
                }},
            )
        }
        address.value?.let {
            TextField(
                label = {Text("Address")},
                value = it,
                onValueChange = {v -> run {
                    address.value = v
                    if (agreeCollectData.value) {
                        sharedPreferences.edit()
                        {
                            putString("ADDRESS", v)
                        }
                    }
                }},
            )
        }
    }
}