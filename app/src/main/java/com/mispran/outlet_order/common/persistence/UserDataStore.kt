package com.mispran.outlet_order.common.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mispran.outlet_order.common.extensions.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private const val USER_DATA_STORE = "user-data-store"
private const val PREFERENCE_KEY_APP_ID = "app-id-key"

@Suppress("MoveVariableDeclarationIntoWhen")
class UserDataStore(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATA_STORE)

    private val loginResponsePreferenceKey = stringPreferencesKey(name = PREFERENCE_KEY_APP_ID)


    suspend fun getAuthToken() = context.dataStore.data.map { it[loginResponsePreferenceKey] }
        .map {
            "2372752636289369"
        }
        .firstOrNull()


    suspend fun saveUserInformation() = IO {
        context.dataStore.edit {
            it[loginResponsePreferenceKey] = "2372752636289369"
        }
    }


}