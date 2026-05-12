package com.example.kavyakanaja.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Onboarding helper — checks / marks first launch ──────────────
object OnboardingManager {
    private const val PREFS_NAME     = "kavya_onboarding"
    private const val KEY_COMPLETED  = "onboarding_completed"

    fun isCompleted(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_COMPLETED, false)
    }

    fun markCompleted(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putBoolean(KEY_COMPLETED, true).apply()
    }
}

// ── Data for each slide ───────────────────────────────────────────
data class OnboardingSlide(
    val emoji: String,
    val kannadaTitle: String,
    val englishTitle: String,
    val description: String,
    val highlight: String,          // short bold line at bottom
    val gradientColors: List<Color>
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val context = LocalContext.current

    val slides = listOf(
        OnboardingSlide(
            emoji = "📜",
            kannadaTitle = "ಕಾವ್ಯ ಕಣಜಕ್ಕೆ ಸ್ವಾಗತ",
            englishTitle = "Welcome to Kavya Kanaja",
            description = "Your daily companion for Kannada poetry.\n\nDiscover verses from legendary poets like Kuvempu, Basavanna, Bendre and more — with meanings, explanations and audio.",
            highlight = "Poetry Granary 🌾",
            gradientColors = listOf(Color(0xFF2C0A0A), Color(0xFF5C1A1A), Color(0xFF8B4513))
        ),
        OnboardingSlide(
            emoji = "🎧",
            kannadaTitle = "ಕೇಳಿ, ಕಲಿಯಿರಿ, ಬೆಳೆಯಿರಿ",
            englishTitle = "Listen, Learn & Grow",
            description = "Hear poems read aloud in Kannada or English.\n\nTap words to learn meanings. Take daily quizzes to test yourself. Earn XP and level up as you learn!",
            highlight = "Learn like Duolingo 🏆",
            gradientColors = listOf(Color(0xFF0A1A0A), Color(0xFF1A3A1A), Color(0xFF2D5A27))
        ),
        OnboardingSlide(
            emoji = "🔥",
            kannadaTitle = "ನಿತ್ಯ ಅಭ್ಯಾಸ ಮಾಡಿ",
            englishTitle = "Build a Daily Habit",
            description = "Open the app every day to build your streak.\n\nRead today's poem ✅  Complete a quiz ✅\n\nEarn badges, save favourites and track your progress!",
            highlight = "Start your streak today! 🔥",
            gradientColors = listOf(Color(0xFF0D1B2A), Color(0xFF1B2A3B), Color(0xFF243447))
        )
    )

    var currentSlide by remember { mutableIntStateOf(0) }
    val slide = slides[currentSlide]
    val isLastSlide = currentSlide == slides.size - 1

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = slide.gradientColors)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // ── Skip button — top right ───────────────────────────
            Box(modifier = Modifier.fillMaxWidth()) {
                if (!isLastSlide) {
                    TextButton(
                        onClick = {
                            OnboardingManager.markCompleted(context)
                            onFinished()
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "Skip",
                            fontSize = 14.sp,
                            color = Color(0xFFDEB887)
                        )
                    }
                }
            }

            // ── Main content ──────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.weight(1f),
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Big emoji
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color(0x33FFD700))
                            .border(2.dp, Color(0x66FFD700), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = slide.emoji,
                            fontSize = 64.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Kannada title
                Text(
                    text = slide.kannadaTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )

                // English title
                Text(
                    text = slide.englishTitle,
                    fontSize = 15.sp,
                    color = Color(0xFFDEB887),
                    textAlign = TextAlign.Center
                )

                // Description card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0x33FFFFFF)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = slide.description,
                        fontSize = 15.sp,
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        modifier = Modifier.padding(20.dp)
                    )
                }

                // Highlight pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x44FFD700))
                        .border(1.dp, Color(0x88FFD700), RoundedCornerShape(50))
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = slide.highlight,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ── Bottom: dots + button ─────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Dot indicators
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    slides.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentSlide) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == currentSlide) Color(0xFFFFD700)
                                    else Color(0x55FFD700)
                                )
                        )
                    }
                }

                // Next / Get Started button
                Button(
                    onClick = {
                        if (isLastSlide) {
                            OnboardingManager.markCompleted(context)
                            onFinished()
                        } else {
                            currentSlide++
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = if (isLastSlide) "🚀 ಪ್ರಾರಂಭಿಸೋಣ!  Get Started!" else "Next →",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A0A00)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}