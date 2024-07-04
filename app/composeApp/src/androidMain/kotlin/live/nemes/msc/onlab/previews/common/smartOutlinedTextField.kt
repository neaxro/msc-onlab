package live.nemes.msc.onlab.previews.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ui.composables.SmartOutlinedTextField

@Preview(showBackground = true)
@Composable
fun smartOutlinedTextFieldPreview(){
    var text by remember {
        mutableStateOf<String>("Axel")
    }

    Column(
        modifier = Modifier.padding(10.dp)
    ) {

        SmartOutlinedTextField(
            value = "",
            label = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Login"
                )
            },
            onValueChange = {
                text = it
            },
            isError = false,
            singleLine = true,
            maxLength = 10,
            readOnly = false,
            enabled = true,
        )

        SmartOutlinedTextField(
            value = text,
            label = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Login"
                )
            },
            onValueChange = {
                text = it
            },
            isError = false,
            singleLine = true,
            maxLength = 10,
            readOnly = false,
            enabled = true,
        )

        SmartOutlinedTextField(
            value = text,
            label = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Login"
                )
            },
            onValueChange = {
                text = it
            },
            isError = true,
            singleLine = true,
            maxLength = 10,
            readOnly = false,
            enabled = true,
        )
    }
}