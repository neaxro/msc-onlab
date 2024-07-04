package ui.composables

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartPasswordOutlinedTextField(
    password: String,
    onPasswordChange: (String) -> Unit = {},
    label: @Composable() (() -> Unit)?,
    isError: Boolean = false,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    supportingText: @Composable() (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
){
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
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
        supportingText = supportingText,
        modifier = modifier
    )
}