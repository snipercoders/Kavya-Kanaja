package com.example.kavyakanaja

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Read streak for personalised message
        val streak = StreakManager.getStreak(context)

        // Streak-aware title and message
        val title = when {
            streak == 0  -> "🌸 ಕಾವ್ಯ ಕಣಜ"
            streak < 3   -> "🌸 ಕಾವ್ಯ ಕಣಜ — Day $streak"
            streak < 7   -> "🔥 $streak Day Streak!"
            streak < 30  -> "🔥 $streak Days — On Fire!"
            else         -> "🏆 $streak Days — Legendary!"
        }

        val message = when {
            streak == 0  -> "ಇಂದಿನ ಕವನ ನಿಮಗಾಗಿ ಕಾಯುತ್ತಿದೆ! Today's poem is waiting for you!"
            streak < 3   -> "ನಿಮ್ಮ ಕ್ರಮ ಮುಂದುವರೆಸಿ! Keep your streak going — read today's poem!"
            streak < 7   -> "Don't break your $streak day streak! 🔥 Today's poem is ready."
            streak < 30  -> "You're on fire with $streak days! 🔥 Come read today's Kannada verse."
            else         -> "🏆 $streak day legend! Your daily Kannada poem awaits."
        }

        // Tap notification → open app to Poem of Day
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "poem")
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        manager.notify(NotificationHelper.NOTIFICATION_ID, notification)
    }
}