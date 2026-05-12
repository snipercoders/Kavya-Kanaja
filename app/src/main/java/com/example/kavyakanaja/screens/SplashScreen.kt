package com.example.kavyakanaja.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.StreakManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val streak  = remember { StreakManager.getStreak(context) }

    // ── Animation values ──────────────────────────────────────────
    val logoScale   = remember { Animatable(0.4f) }
    val logoAlpha   = remember { Animatable(0f) }
    val titleAlpha  = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val streakAlpha = remember { Animatable(0f) }
    val dividerAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Step 1 — logo scales up + fades in
        launch {
            logoScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            logoAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(700)
            )
        }

        delay(500)

        // Step 2 — title fades in
        titleAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )

        delay(200)

        // Step 3 — divider fades in
        dividerAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(400)
        )

        delay(100)

        // Step 4 — tagline fades in
        taglineAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )

        delay(200)

        // Step 5 — streak badge fades in
        streakAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(400)
        )

        // Step 6 — wait then navigate
        delay(900)
        onFinished()
    }

    // ── UI ────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF120500),
                        Color(0xFF2C0A0A),
                        Color(0xFF1A0500)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Logo circle ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF5C2A00),
                                Color(0xFF2C0A0A)
                            )
                        )
                    )
                    .border(
                        width = 2.5.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFD700),
                                Color(0xFFB8860B),
                                Color(0xFFFFD700)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "📜",
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── App name ──────────────────────────────────────────
            Text(
                text = "ಕಾವ್ಯ ಕಣಜ",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(titleAlpha.value)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ── Divider ───────────────────────────────────────────
            Box(
                modifier = Modifier
                    .alpha(dividerAlpha.value)
                    .width(120.dp)
                    .height(1.5.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFFFD700),
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Tagline ───────────────────────────────────────────
            Text(
                text = "Poetry Granary",
                fontSize = 16.sp,
                color = Color(0xFFDEB887),
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value)
            )

            Text(
                text = "ಕನ್ನಡ ಕಾವ್ಯದ ಖಜಾನೆ",
                fontSize = 13.sp,
                color = Color(0xFFDEB887).copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha.value)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ── Streak badge ──────────────────────────────────────
            Box(modifier = Modifier.alpha(streakAlpha.value)) {
                if (streak > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                if (streak >= 7) Color(0xFF5C2800)
                                else Color(0x33FFD700)
                            )
                            .border(
                                1.dp,
                                if (streak >= 7) Color(0xFFFF6B00)
                                else Color(0x66FFD700),
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🔥", fontSize = 18.sp)
                            Text(
                                text = when {
                                    streak == 1 -> "1 day streak!"
                                    streak < 7  -> "$streak day streak!"
                                    streak < 30 -> "$streak days — on fire!"
                                    else        -> "$streak days — legendary!"
                                },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (streak >= 7) Color(0xFFFF6B00)
                                else Color(0xFFFFD700)
                            )
                        }
                    }
                } else {
                    // First time user — welcome message
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0x33FFD700))
                            .border(1.dp, Color(0x66FFD700), RoundedCornerShape(50))
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "🌸 Welcome to Kavya Kanaja!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFFD700)
                        )
                    }
                }
            }
        }

        // ── Bottom tagline ────────────────────────────────────────
        Text(
            text = "ಕನ್ನಡ ನಾಡಿನ ಕಾವ್ಯ ಸಿರಿ",
            fontSize = 11.sp,
            color = Color(0x55DEB887),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(streakAlpha.value)
        )
    }
}