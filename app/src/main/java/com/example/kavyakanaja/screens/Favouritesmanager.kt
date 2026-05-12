package com.example.kavyakanaja

import android.content.Context

object FavouritesManager {

    private const val PREFS_NAME = "kavya_favourites"
    private const val KEY_FAV_IDS = "favourite_ids"

    // ── Load saved poem IDs from SharedPreferences ────────────────
    fun getFavouriteIds(context: Context): Set<Int> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getStringSet(KEY_FAV_IDS, emptySet()) ?: emptySet()
        return raw.mapNotNull { it.toIntOrNull() }.toSet()
    }

    // ── Save a poem ID ────────────────────────────────────────────
    fun addFavourite(context: Context, poemId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = getFavouriteIds(context).map { it.toString() }.toMutableSet()
        current.add(poemId.toString())
        prefs.edit().putStringSet(KEY_FAV_IDS, current).apply()
    }

    // ── Remove a poem ID ──────────────────────────────────────────
    fun removeFavourite(context: Context, poemId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = getFavouriteIds(context).map { it.toString() }.toMutableSet()
        current.remove(poemId.toString())
        prefs.edit().putStringSet(KEY_FAV_IDS, current).apply()
    }

    // ── Toggle helper — returns true if now favourited ────────────
    fun toggleFavourite(context: Context, poemId: Int): Boolean {
        return if (isFavourite(context, poemId)) {
            removeFavourite(context, poemId)
            false
        } else {
            addFavourite(context, poemId)
            true
        }
    }

    // ── Check if a poem is already favourited ─────────────────────
    fun isFavourite(context: Context, poemId: Int): Boolean {
        return getFavouriteIds(context).contains(poemId)
    }
}