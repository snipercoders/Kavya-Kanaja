package com.example.kavyakanaja

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object StreakManager {

    private const val PREFS_NAME   = "kavya_streak_prefs"
    private const val KEY_STREAK   = "streak_count"
    private const val KEY_LAST_DATE = "last_open_date"

    private fun todayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // ── Call this once on every app open (in MainActivity.onCreate) ──
    fun updateStreak(context: Context) {
        val prefs     = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today     = todayString()
        val lastDate  = prefs.getString(KEY_LAST_DATE, "") ?: ""
        val streak    = prefs.getInt(KEY_STREAK, 0)

        when {
            lastDate == today -> {
                // Already opened today — do nothing, keep streak
            }
            lastDate == yesterdayString() -> {
                // Opened yesterday — increment streak
                prefs.edit()
                    .putString(KEY_LAST_DATE, today)
                    .putInt(KEY_STREAK, streak + 1)
                    .apply()
            }
            else -> {
                // Missed a day or first time — reset to 1
                prefs.edit()
                    .putString(KEY_LAST_DATE, today)
                    .putInt(KEY_STREAK, 1)
                    .apply()
            }
        }
    }

    // ── Call this to read the streak count anywhere ───────────────
    fun getStreak(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_STREAK, 0)
    }

    private fun yesterdayString(): String {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DATE, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }
}