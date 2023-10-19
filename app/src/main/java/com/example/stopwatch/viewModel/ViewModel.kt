package com.example.stopwatch.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stopwatch.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch


class ViewModel: ViewModel() {

    private val TAG = "ViewModel"

    private var isRunning: Boolean = false

    private var startTime: Long = 0L
    private var elapsedTime: Long = 0L

    private val timeLiveData = MutableLiveData<Long>()

    var serviceTime: Long = 0L


    fun startStopwatch() {
        if (!isRunning) {
            startTime = if (elapsedTime >= 0L) {
                System.currentTimeMillis() - elapsedTime
            } else {
                System.currentTimeMillis()
            }
        }
        serviceTime = startTime
        isRunning = true
    }

    fun stopStopwatch() {
        if (isRunning) {
            elapsedTime == System.currentTimeMillis() - startTime
            isRunning = false
        }
    }

    fun resetStopwatch() {
        isRunning = false
        startTime = 0L
        elapsedTime = 0L

    }

    fun pauseStopwatch() {
        if (isRunning) {
            elapsedTime = System.currentTimeMillis() - startTime
            isRunning = false
        }
    }

    fun timeFlow(): Flow<Long> = flow {
        while (true) {
            if(isRunning) {
                elapsedTime = System.currentTimeMillis() - startTime
                emit(elapsedTime)
                delay(100)
            }
        }
    }.flowOn(Dispatchers.Default)

}