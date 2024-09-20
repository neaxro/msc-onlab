package com.example.msc_onlab.ui.feature.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign

@Composable
fun SmartPasswordOutlinedTextField(
    password: String,
    onPasswordChange: (String) -> Unit = {},
    label: @Composable() (() -> Unit)?,
    isError: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    maxLength: Int = 30,
    supportingText: @Composable() (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
){
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { newPassword ->
            if(newPassword.length <= maxLength) {
                onPasswordChange(newPassword)
            }
        },
        label = label,
        singleLine = true,
        trailingIcon = {
            val image = if(passwordVisible){
                Icons.Rounded.Visibility
            } else{
                Icons.Rounded.VisibilityOff
            }

            val description = if(passwordVisible) {
                "Hide Password"
            } else {
                "Show Password"
            }

            IconButton(onClick = {
                passwordVisible = !passwordVisible
            }) {
                Icon(imageVector = image, description)
            }
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = isError,
        readOnly = readOnly,
        enabled = enabled,
        supportingText = {
            if(supportingText != null){
                supportingText()
            }
            else{
                Text(
                    text = "${password.length}/$maxLength",
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = modifier
    )
}
