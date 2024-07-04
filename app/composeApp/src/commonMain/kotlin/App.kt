
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.screens.loginScreen.loginScreen
import ui.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme{
        loginScreen()
    }
}