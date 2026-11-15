package za.co.mohlare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import za.co.mohlare.app.data.SettingsStore
import za.co.mohlare.app.ui.theme.MohlareTheme
import za.co.mohlare.app.ui.theme.MohlareThemeId

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val settings = SettingsStore(applicationContext)

        setContent {
            val themeId by settings.themeId.collectAsState(initial = MohlareThemeId.HERITAGE)
            val scope = rememberCoroutineScope()
            MohlareTheme(themeId) {
                MohlareApp(
                    currentTheme = themeId,
                    onThemeChange = { id -> scope.launch { settings.setTheme(id) } },
                )
            }
        }
    }
}
