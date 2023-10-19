package com.example.stopwatch.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.stopwatch.MainActivity
import com.example.stopwatch.R
import com.example.stopwatch.utility.Utility
import com.example.stopwatch.viewModel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.lang.Error

class Service : Service() {

    // ViewModel
    private val vm: ViewModel by lazy { ViewModel() }

    // Utility
    private val util: Utility by lazy { Utility() }

    //NotificationCompat Builder
    private lateinit var builder: NotificationCompat.Builder

    // 시간 데이터를 저장할 변수
    private var currentTime: Long = 0

    companion object {
        private const val TAG = "SERVICE"

        // Notification
        private const val NOTI_ID = 1
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        createNotification()

        CoroutineScope(Dispatchers.Default).launch {
            try {
                // 동작 안하는 이유를 모르겠습니다....
                // 죄송합니다....
                vm.timeFlow().collect { time ->

                    currentTime = time
                    updateNotificationText()
                }
            } catch (err: Error) {
                Log.e(TAG, "byel: $err")
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun createNotification() {
        builder = NotificationCompat.Builder(this, "default")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Foreground Service")
        builder.setContentText("time : ")


        builder.color = Color.RED
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent) // 알림 클릭 시 이동

        // 알림 표시
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    "default",
                    "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        notificationManager.notify(NOTI_ID, builder.build()) // id : 정의해야하는 각 알림의 고유한 int값
        val notification = builder.build()
        startForeground(NOTI_ID, notification)
    }

    // 노티피케이션 텍스트를 업데이트
    fun updateNotificationText() {
        builder.setContentText("시간: ${util.formatTime(currentTime)}")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTI_ID, builder.build())
    }


}