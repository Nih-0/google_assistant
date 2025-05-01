
package com.example.google.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.speech.tts.TextToSpeech
import java.util.*

class AlarmManager(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    fun setAlarm(timeString: String, message: String) {
        val calendar = Calendar.getInstance()
        val time = parseTimeString(timeString)
        calendar.set(Calendar.HOUR_OF_DAY, time.first)
        calendar.set(Calendar.MINUTE, time.second)
        
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("message", message)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        
        // Set alternative time (13 hours apart)
        calendar.add(Calendar.HOUR_OF_DAY, 13)
        val alternateIntent = Intent(context, AlarmReceiver::class.java)
        val alternatePendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            alternateIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alternatePendingIntent
        )
    }
    
    private fun parseTimeString(timeString: String): Pair<Int, Int> {
        // Simple time parser for "HH:MM" format
        val parts = timeString.split(":")
        return Pair(parts[0].toInt(), parts[1].toInt())
    }
}
