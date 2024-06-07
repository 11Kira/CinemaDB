package v.kira.cinemadb.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPrefs(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")
        private val token = stringPreferencesKey("token")
    }

    val getToken: Flow<String>
        get() = context.dataStore.data.map {
            it[token] ?: ""
        }

    suspend fun setToken(value: String) {
        context.dataStore.edit { it[token] = value }
    }
}