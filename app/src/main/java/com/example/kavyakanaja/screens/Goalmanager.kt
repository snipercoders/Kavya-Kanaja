package com.example.kavyakanaja

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GoalManager {

    private const val PREFS_NAME      = "kavya_goal_prefs"
    private const val KEY_POEM_DATE   = "goal_poem_date"
    private const val KEY_QUIZ_DATE   = "goal_quiz_date"

    private fun todayString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // ── Call when user reads/scrolls poem of the day ─────────────
    fun markPoemRead(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_POEM_DATE, todayString()).apply()
    }

    // ── Call when user completes a quiz ──────────────────────────
    fun markQuizDone(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_QUIZ_DATE, todayString()).apply()
    }

    fun isPoemReadToday(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_POEM_DATE, "") == todayString()
    }

    fun isQuizDoneToday(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_QUIZ_DATE, "") == todayString()
    }

    // 0, 1, or 2 goals completed today
    fun goalsCompletedToday(context: Context): Int {
        var count = 0
        if (isPoemReadToday(context)) count++
        if (isQuizDoneToday(context)) count++
        return count
    }
}