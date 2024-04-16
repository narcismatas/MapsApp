package com.example.mapsapp.View

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
import androidx.compose.material3.ElevatedButton
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
    if (nextScreen){
        navController.navigate(Routes.HomeScreen.route)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f)
                .align(Alignment.Center)
                .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(10))
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
                text = "Create\nAccount",
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
                    shape = RoundedCornerShape(20)
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
                    shape = RoundedCornerShape(20)
                )
                if (usernameRep != "" && usernameRep != username) {
                    Text(
                        text = "eMails don't match!",
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        ),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                } else {
                    Spacer(modifier = Modifier.height(30.dp))
                }
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
                        val description = if (visiblePass) "Hide password" else "Show password"

                        IconButton(onClick = {visiblePass = !visiblePass}){
                            Icon(imageVector  = image, description)
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
                        val description = if (visiblePassRep) "Hide password" else "Show password"

                        IconButton(onClick = {visiblePassRep = !visiblePassRep}){
                            Icon(imageVector  = image, description)
                        }
                    }
                )
            }


            ElevatedButton(
                onClick = {
                    if (username == usernameRep && password == passwordRep) viewModel.register(
                        username,
                        password
                    )
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
