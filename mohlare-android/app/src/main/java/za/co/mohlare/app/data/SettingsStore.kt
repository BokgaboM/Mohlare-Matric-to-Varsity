package za.co.mohlare.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import za.co.mohlare.app.ui.theme.MohlareThemeId

private val Context.dataStore by preferencesDataStore(name = "mohlare_settings")

/** Saves small settings on the phone, such as the chosen theme. */
class SettingsStore(private val context: Context) {

    private val themeKey = stringPreferencesKey("theme_id")

    val themeId: Flow<MohlareThemeId> = context.dataStore.data.map { prefs ->
        prefs[themeKey]
            ?.let { saved -> MohlareThemeId.entries.firstOrNull { it.name == saved } }
            ?: MohlareThemeId.HERITAGE
    }

    suspend fun setTheme(id: MohlareThemeId) {
        context.dataStore.edit { it[themeKey] = id.name }
    }
}
