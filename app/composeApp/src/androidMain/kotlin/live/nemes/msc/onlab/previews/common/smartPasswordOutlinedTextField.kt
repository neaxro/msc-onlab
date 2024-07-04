package live.nemes.msc.onlab.previews.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Password
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.composables.SmartOutlinedTextField
import ui.composables.SmartPasswordOutlinedTextField

@Preview(showBackground = true)
@Composable
fun smartPasswordOutlinedTextFieldPreview(){
    var password by remember {
        mutableStateOf<String>("password123")
    }

    Column(
        modifier = Modifier.padding(10.dp)
    ) {

        SmartPasswordOutlinedTextField(
            password = "",
            onPasswordChange = {
                password = it
            },
            label = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Password,
                    contentDescription = "Password"
                )
            },
            isError = false,
            readOnly = false,
            enabled = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
            ),
        )

        SmartPasswordOutlinedTextField(
            password = password,
            onPasswordChange = {
                password = it
            },
            label = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Password,
                    contentDescription = "Password"
                )
            },
            isError = false,
            readOnly = false,
            enabled = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
            ),
        )

        SmartPasswordOutlinedTextField(
            password = password,
            onPasswordChange = {
                password = it
            },
            label = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Rounded.Password,
                    contentDescription = "Password"
                )
            },
            isError = true,
            readOnly = false,
            enabled = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
            ),
        )
    }
}