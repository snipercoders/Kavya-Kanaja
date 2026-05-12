

package com.example.kavyakanaja.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.FavouritesManager
import com.example.kavyakanaja.GoalManager
import com.example.kavyakanaja.PoemRepository
import com.example.kavyakanaja.StreakManager
import com.example.kavyakanaja.XPManager
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.Canvas

@Composable
fun PoemOfDayScreen() {
    val context = LocalContext.current
    val poem = remember { PoemRepository.getPoemOfDay(context) }

    val streak   = remember { StreakManager.getStreak(context) }
    val totalXP  = remember { XPManager.getXP(context) }

    // ── Daily goal state ──────────────────────────────────────────
    var isPoemRead by remember { mutableStateOf(GoalManager.isPoemReadToday(context)) }
    var isQuizDone by remember { mutableStateOf(GoalManager.isQuizDoneToday(context)) }
    val goalsComplete = (if (isPoemRead) 1 else 0) + (if (isQuizDone) 1 else 0)
    val goalProgress  = goalsComplete / 2f

    var isFavourited by remember {
        mutableStateOf(
            if (poem != null) FavouritesManager.isFavourite(context, poem.id) else false
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2C0A0A), Color(0xFF5C1A1A), Color(0xFF8B4513))
                )
            )
    ) {
        if (poem == null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color(0xFFFFD700))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Loading poems...", color = Color.White)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // ── Title + Streak + XP row ───────────────────────
                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = "ಕಾವ್ಯ ಕಣಜ",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Poetry Granary",
                            fontSize = 14.sp,
                            color = Color(0xFFDEB887),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    // 🔥 Streak badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(50))
                            .background(if (streak >= 7) Color(0xFFFF6B00) else Color(0x44FFD700))
                            .border(
                                1.5.dp,
                                if (streak >= 7) Color(0xFFFFD700) else Color(0x88FFD700),
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "🔥", fontSize = 16.sp)
                            Text(
                                text = "$streak",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }

                Text(
                    text = when {
                        streak == 0 -> "Start your streak today!"
                        streak == 1 -> "🔥 1 day streak — keep going!"
                        streak < 7  -> "🔥 $streak day streak!"
                        streak < 30 -> "🔥 $streak day streak — on fire!"
                        else        -> "🔥 $streak days — legendary!"
                    },
                    fontSize = 12.sp,
                    color = Color(0xFFDEB887),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ── Daily Goal Ring Card ──────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left — goal checklist
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "📅 Daily Goals",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (isPoemRead) "✅" else "⬜",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Read today's poem",
                                    fontSize = 12.sp,
                                    color = if (isPoemRead) Color(0xFF4CAF50)
                                    else Color(0xFFDEB887)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (isQuizDone) "✅" else "⬜",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Complete a quiz",
                                    fontSize = 12.sp,
                                    color = if (isQuizDone) Color(0xFF4CAF50)
                                    else Color(0xFFDEB887)
                                )
                            }
                            Text(
                                text = "$goalsComplete / 2 done",
                                fontSize = 11.sp,
                                color = Color(0xFFDEB887)
                            )
                        }

                        // Right — circular ring
                        Box(
                            modifier = Modifier.size(70.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Canvas(modifier = Modifier.size(70.dp)) {
                                val strokeWidth = 8.dp.toPx()
                                val radius = (size.minDimension - strokeWidth) / 2
                                // Background ring
                                drawCircle(
                                    color = Color(0x33FFD700),
                                    radius = radius,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                                // Progress arc
                                if (goalProgress > 0f) {
                                    drawArc(
                                        color = if (goalProgress >= 1f) Color(0xFF4CAF50)
                                        else Color(0xFFFFD700),
                                        startAngle = -90f,
                                        sweepAngle = 360f * goalProgress,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                    )
                                }
                            }
                            Text(
                                text = if (goalProgress >= 1f) "🎯" else "$goalsComplete/2",
                                fontSize = if (goalProgress >= 1f) 22.sp else 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (goalProgress >= 1f) Color(0xFF4CAF50)
                                else Color(0xFFFFD700)
                            )
                        }
                    }
                }

                // ── XP Level badge ────────────────────────────────
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x33FFD700))
                        .border(1.dp, Color(0x66FFD700), RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "⚡ $totalXP XP  •  ${XPManager.getLevel(totalXP)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = XPManager.getLevelColor(totalXP)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8F0)),
                    shape = RoundedCornerShape(4.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✦ ಇಂದಿನ ಕವನ ✦",
                            fontSize = 12.sp,
                            color = Color(0xFF8B4513),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Poem of the Day",
                            fontSize = 10.sp,
                            color = Color(0xFFB8860B),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Poem Card ─────────────────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = poem.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                            IconButton(
                                onClick = {
                                    isFavourited = FavouritesManager.toggleFavourite(context, poem.id)
                                },
                                modifier = Modifier.align(Alignment.CenterEnd)
                            ) {
                                Icon(
                                    imageVector = if (isFavourited) Icons.Filled.Favorite
                                    else Icons.Filled.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isFavourited) Color(0xFFFF4444)
                                    else Color(0xFFDEB887),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "— ${poem.poet}",
                            fontSize = 14.sp,
                            color = Color(0xFFDEB887),
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                        if (isFavourited) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "❤️ Saved to Favourites",
                                fontSize = 11.sp,
                                color = Color(0xFFFF4444),
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider(color = Color(0x66FFD700), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = poem.verse,
                            fontSize = 18.sp,
                            color = Color(0xFFFFFAF0),
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }

                // Mark poem as read when user reaches this point
                LaunchedEffect(Unit) {
                    if (!isPoemRead) {
                        GoalManager.markPoemRead(context)
                        isPoemRead = true
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ── Share Button ──────────────────────────────────
                Button(
                    onClick = {
                        val shareText = buildString {
                            appendLine("✦ ಇಂದಿನ ಕವನ | Poem of the Day ✦")
                            appendLine()
                            appendLine("${poem.title} — ${poem.poet}")
                            appendLine()
                            appendLine(poem.verse)
                            appendLine()
                            appendLine("📖 Meaning:")
                            appendLine(poem.meaning)
                            appendLine()
                            appendLine("Shared via ಕಾವ್ಯ ಕಣಜ (Kavya Kanaja)")
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Poem via..."))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth().height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF2C0A0A),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "ಹಂಚಿಕೊಳ್ಳಿ  |  Share Poem",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C0A0A)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("📖 Meaning", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(poem.meaning, fontSize = 15.sp, color = Color(0xFFFFFFFF),
                            lineHeight = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("💡 Bhavartha", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(poem.bhavartha, fontSize = 15.sp, color = Color(0xFFFFFFFF),
                            lineHeight = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}