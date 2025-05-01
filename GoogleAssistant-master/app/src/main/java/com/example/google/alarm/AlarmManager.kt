
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
        // Parse time in format "6 AM", "6:30 PM", etc.
        val timeParts = timeString.uppercase().split(" ")
        val time = timeParts[0]
        val meridian = timeParts[1]
        
        val (hours, minutes) = if (time.contains(":")) {
            val parts = time.split(":")
            Pair(parts[0].toInt(), parts[1].toInt())
        } else {
            Pair(time.toInt(), 0)
        }
        
        // Convert to 24-hour format
        return when {
            meridian == "PM" && hours != 12 -> Pair(hours + 12, minutes)
            meridian == "AM" && hours == 12 -> Pair(0, minutes)
            else -> Pair(hours, minutes)
        }
    }
        val parts = timeString.split(":")
        return Pair(parts[0].toInt(), parts[1].toInt())
    }
}
