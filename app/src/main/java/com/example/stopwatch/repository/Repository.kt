package com.example.stopwatch.repository

import android.content.Context
import android.util.Log


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.FieldPosition


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "recode")

private val TAG = "Repository"

class Repository private constructor(context: Context) {



    private val dataStore = context.dataStore

    private val RECODE_LIST_KEY = stringPreferencesKey("recode_list")

    companion object {
        private var INSTANCE: Repository? = null

        fun initialize(context: Context) {
            INSTANCE = Repository(context)
        }

        fun get(): Repository {
            return INSTANCE ?: throw IllegalStateException("Repository Error")
        }
    }
    suspend fun clearRecodeList() {
        dataStore.edit { preferences ->
            preferences.remove(RECODE_LIST_KEY)
        }
    }


    // 기록저장
    suspend fun saveRecodeList(vararg time: String) {
        dataStore.edit { preferences ->
            val existingDataString = preferences[RECODE_LIST_KEY] ?: ""
            val newTimeData = (existingDataString + "," + time.joinToString(",")).removePrefix(",") // 콤마 제거 및 연결
            preferences[RECODE_LIST_KEY] = newTimeData
        }
    }

    suspend fun deleteRecodeList(position: Int) {
        val preferences = dataStore.data.first()
        val dataString = preferences[RECODE_LIST_KEY] ?: ""
        var timeList = dataString.split(",").toMutableList()
        timeList.removeAt(position)
        clearRecodeList()
        Log.e(TAG, "byel deleteRecodeList: $timeList" )
        dataStore.edit { preferences ->
            preferences[RECODE_LIST_KEY] = timeList.joinToString(",")
        }

    }


    suspend fun getRecodeList(): List<String> {
        val preferences = dataStore.data.first()
        val dataString = preferences[RECODE_LIST_KEY] ?: ""
        return dataString.split(",")
    }

}
