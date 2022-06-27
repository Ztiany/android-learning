package com.ztiany.androidx.jetpack.datastore

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.ztiany.androidx.kotlin.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "DataStoreActivity"

class DataStoreActivity : AppCompatActivity() {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_store_activity)
    }

    fun saveData(view: View) {
        lifecycleScope.launch {
            dataStore.edit {
                it[intPreferencesKey("IntA")] = 12
            }
        }
    }

    fun getData(view: View) {
        dataStore.data.map {
            it[intPreferencesKey("IntA")] ?: 0
        }
            .onEach {
                Log.d(TAG, "IntA = $it")
            }
            .launchIn(lifecycleScope)
    }

}