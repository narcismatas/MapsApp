package com.example.mapsapp.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapsapp.ViewModel.MainViewModel
import com.example.mapsapp.ViewModel.gilmer

@Composable
fun RegisterScreen(navController: NavController, viewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var usernameRep by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordRep by remember { mutableStateOf("") }
    var visiblePass by remember { mutableStateOf(false) }
    var visiblePassRep by remember { mutableStateOf(false) }
    val nextScreen by viewModel.goToNext.observeAsState(false)
    if (nextScreen) {
        navController.navigate(Routes.HomeScreen.route)
    }
    var mailFormatToast by remember { mutableStateOf(false) }
    var passwordLengthToast by remember { mutableStateOf(false) }
    var mailMatchToast by remember { mutableStateOf(false) }
    var passMatchToast by remember { mutableStateOf(false) }
    val existentUserToast by viewModel.showToastExistentUser.observeAsState(false)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6c757d))
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 40.dp)
                .fillMaxSize()
                .align(Alignment.Center),
            shape = RoundedCornerShape(10),
            colors = CardColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Black,
                disabledContentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                FloatingActionButton(
                    onClick = { navController.navigateUp() },
                    contentColor = Color.Black,
                    containerColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = 30.dp, x = 30.dp),
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Create an\nAccount",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(y = 30.dp, x = (-30).dp),
                    style = TextStyle(
                        fontFamily = gilmer,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 40.sp
                    ),
                    textAlign = TextAlign.End
                )
                val loading by viewModel.showLoading.observeAsState(false)
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(128.dp)
                            .align(Alignment.Center),
                        color = Color.White,
                        trackColor = Color.DarkGray,
                        strokeWidth = 10.dp
                    )

                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .wrapContentHeight()
                            .align(Alignment.Center),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "eMail",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            shape = RoundedCornerShape(20),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Text(
                            text = "Repeat eMail",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        OutlinedTextField(
                            value = usernameRep,
                            onValueChange = { usernameRep = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            shape = RoundedCornerShape(20),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "Password",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            shape = RoundedCornerShape(20),
                            visualTransformation = if (visiblePass) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                val image = if (visiblePass)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                // Please provide localized description for accessibility services
                                val description =
                                    if (visiblePass) "Hide password" else "Show password"

                                IconButton(onClick = { visiblePass = !visiblePass }) {
                                    Icon(imageVector = image, description)
                                }
                            }
                        )
                        Text(
                            text = "Repeat Password",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                        OutlinedTextField(
                            value = passwordRep,
                            onValueChange = { passwordRep = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            shape = RoundedCornerShape(20),
                            visualTransformation = if (visiblePassRep) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                val image = if (visiblePassRep)
                                    Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff

                                // Please provide localized description for accessibility services
                                val description =
                                    if (visiblePassRep) "Hide password" else "Show password"

                                IconButton(onClick = { visiblePassRep = !visiblePassRep }) {
                                    Icon(imageVector = image, description)
                                }
                            }
                        )
                    }
                    if (mailMatchToast) {
                        Toast.makeText(
                            LocalContext.current,
                            "eMails don't match!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        mailMatchToast = false
                    }
                    if (mailFormatToast) {
                        Toast.makeText(
                            LocalContext.current,
                            "Wrong eMail format!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        mailFormatToast = false
                    }
                    if (passMatchToast) {
                        Toast.makeText(
                            LocalContext.current,
                            "Passwords don't match!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        passMatchToast = false
                    }
                    if (passwordLengthToast) {
                        Toast.makeText(
                            LocalContext.current,
                            "Password needs to be at least 6 characters long",
                            Toast.LENGTH_SHORT
                        ).show()
                        passwordLengthToast = false
                    }
                    if (existentUserToast) {
                        Toast.makeText(
                            LocalContext.current,
                            "That mail is already in use",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.hideToast()
                    }

                    ElevatedButton(
                        onClick = {
                            val regex = Regex("@.*\\..*")
                            if (username == usernameRep) {
                                if (regex.containsMatchIn(username)) {
                                    if (password == passwordRep) {
                                        if (password.length > 5) {
                                            viewModel.register(username, password)
                                        } else {
                                            passwordLengthToast = true
                                        }
                                    } else {
                                        passMatchToast = true
                                    }
                                } else {
                                    mailFormatToast = true
                                }
                            } else {
                                mailMatchToast = true
                            }
                        },
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.BottomCenter)
                            .offset(y = (-30).dp),
                        shape = RoundedCornerShape(35),
                        colors = ButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            disabledContentColor = Color.LightGray,
                            disabledContainerColor = Color.DarkGray
                        ),
                        enabled = username != "" && password != ""
                    ) {
                        Text(
                            text = "Register",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            ),
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }
                }

            }
        }
    }

}
