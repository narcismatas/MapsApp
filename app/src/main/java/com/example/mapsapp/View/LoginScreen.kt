package com.example.mapsapp.View

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.mapsapp.dataStore.UserPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LogInScreen(navController: NavController, viewModel: MainViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var visiblePass by remember { mutableStateOf(false) }
    val showToast by viewModel.showToastUnknownUser.observeAsState(false)
    val loading by viewModel.showLoading.observeAsState(false)

    val nextScreen by viewModel.goToNext.observeAsState(false)
    if (nextScreen) {
        navController.navigate(Routes.HomeScreen.route)
    }
    var saveUser by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())

    if (storedUserData.value.isNotEmpty() &&
        storedUserData.value[0] != "" &&
        storedUserData.value[1] != ""){

        username = storedUserData.value[0]
        password = storedUserData.value[1]

        if (storedUserData.value[2] == "s" && !loading){
            viewModel.login(storedUserData.value[0], storedUserData.value[1], false)
        }
    }

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
                    Text(
                        text = "Welcome\nAgain", modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(y = 30.dp, x = 30.dp),
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 40.sp
                        )

                    )
                    if (showToast) {
                        Toast.makeText(
                            LocalContext.current,
                            "Unknown user",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        password = ""
                        viewModel.hideToast()
                        CoroutineScope(Dispatchers.IO).launch {
                            userPrefs.saveUserData("", "", "n")
                        }
                    }
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(128.dp)
                                .align(Alignment.Center)
                                .offset(y = (-64).dp),
                            color = Color.White,
                            trackColor = Color.DarkGray,
                            strokeWidth = 10.dp
                        )
                        Text(
                            text = "Loading...",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 40.sp,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .offset(y = 64.dp)

                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .wrapContentHeight()
                                .align(Alignment.Center)
                                .offset(y = (-50).dp),
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
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Person,
                                        contentDescription = "User"
                                    )
                                },
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
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Password,
                                        contentDescription = "User"
                                    )
                                },
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

                                    val description =
                                        if (visiblePass) "Hide password" else "Show password"

                                    IconButton(onClick = { visiblePass = !visiblePass }) {
                                        Icon(imageVector = image, description)
                                    }
                                }
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = saveUser,
                                    onCheckedChange = { saveUser = it },
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(0.dp)
                                )
                                Text(
                                    text = "Remember user",
                                    style = TextStyle(
                                        fontFamily = gilmer,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .align(Alignment.BottomCenter)
                                .offset(y = (-30).dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {


                            ElevatedButton(
                                onClick = {
                                    viewModel.login(username, password, false)
                                    if(saveUser){
                                        CoroutineScope(Dispatchers.IO).launch {
                                            userPrefs.saveUserData(username, password, "s")
                                        }
                                    } else {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            userPrefs.saveUserData("", "", "n")
                                        }
                                    }

                                },
                                modifier = Modifier.wrapContentSize(),
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
                                    text = "Log In",
                                    style = TextStyle(
                                        fontFamily = gilmer,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Black
                                    ),
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "New here? Then",
                                    style = TextStyle(
                                        fontFamily = gilmer,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    ),
                                    modifier = Modifier.padding(vertical = 5.dp)
                                )
                                ElevatedButton(
                                    onClick = { navController.navigate(Routes.RegisterScreen.route) },
                                    modifier = Modifier.wrapContentSize(),
                                    shape = RoundedCornerShape(35),
                                    colors = ButtonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White,
                                        disabledContentColor = Color.DarkGray,
                                        disabledContainerColor = Color.LightGray
                                    ),
                                    border = BorderStroke(width = 2.dp, color = Color.White)
                                ) {
                                    Text(
                                        text = "Register",
                                        style = TextStyle(
                                            fontFamily = gilmer,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Black
                                        ),
                                        modifier = Modifier.padding(
                                            vertical = 5.dp,
                                            horizontal = 3.dp
                                        )
                                    )
                                }
                            }

                        }
                    }


                }
            }

        }

}