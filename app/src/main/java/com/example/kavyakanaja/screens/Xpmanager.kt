package com.example.kavyakanaja

import android.content.Context

object XPManager {

    private const val PREFS_NAME = "kavya_xp_prefs"
    private const val KEY_XP     = "total_xp"

    const val XP_PER_CORRECT   = 10
    const val XP_PER_POEM_READ = 5

    fun getXP(context: Context): Int {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_XP, 0)
    }

    fun addXP(context: Context, amount: Int): Int {
        val prefs  = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val newXP  = prefs.getInt(KEY_XP, 0) + amount
        prefs.edit().putInt(KEY_XP, newXP).apply()
        return newXP
    }

    // ── Level thresholds ──────────────────────────────────────────
    fun getLevel(xp: Int): String = when {
        xp >= 500 -> "🏅 Kavi"
        xp >= 200 -> "📜 Poet"
        xp >= 80  -> "📚 Scholar"
        else      -> "🌱 Beginner"
    }

    fun getNextLevelXP(xp: Int): Int = when {
        xp >= 500 -> 500
        xp >= 200 -> 500
        xp >= 80  -> 200
        else      -> 80
    }

    fun getLevelColor(xp: Int): androidx.compose.ui.graphics.Color = when {
        xp >= 500 -> androidx.compose.ui.graphics.Color(0xFFFFD700)  // gold
        xp >= 200 -> androidx.compose.ui.graphics.Color(0xFF9C27B0)  // purple
        xp >= 80  -> androidx.compose.ui.graphics.Color(0xFF2196F3)  // blue
        else      -> androidx.compose.ui.graphics.Color(0xFF4CAF50)  // green
    }
}