package com.example.stopwatch.utility

class Utility {

    fun formatTime(timeMillis: Long): String {
        val totalSeconds = timeMillis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val milliseconds = timeMillis % 1000

        val minutesStr = minutes.toString().padStart(2, '0')
        val secondsStr = seconds.toString().padStart(2, '0')
        val millisecondsStr = milliseconds.toString().padStart(3, '0')
        return "$minutesStr:$secondsStr:$millisecondsStr"
    }
}