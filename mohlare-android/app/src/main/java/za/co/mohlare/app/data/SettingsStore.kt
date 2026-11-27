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

    private val languageKey = stringPreferencesKey("language")

    /** The language picked on the welcome screen, before the learner has an account. */
    val language: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[languageKey] ?: Languages.first()
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { it[languageKey] = language }
    }
}

val Languages = listOf("English", "Sepedi", "isiZulu", "Afrikaans")
