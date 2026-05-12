package com.example.kavyakanaja

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PoemRepository {
    private var poems: List<Poem> = emptyList()

    fun loadPoems(context: Context): List<Poem> {
        if (poems.isNotEmpty()) return poems
        try {
            val json = context.assets.open("poems.json")
                .bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Poem>>() {}.type
            // Ensures poems is never null even if JSON parsing fails
            poems = Gson().fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            poems = emptyList()
        }
        return poems
    }

    fun getPoemOfDay(context: Context): Poem? {
        val allPoems = loadPoems(context)
        if (allPoems.isEmpty()) return null

        // Pick a poem based on the day of the year
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val index = (dayOfYear - 1) % allPoems.size
        return allPoems[index]
    }
}