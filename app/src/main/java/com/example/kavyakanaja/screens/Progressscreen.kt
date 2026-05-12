//package com.example.kavyakanaja.screens
//
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.Lock
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.kavyakanaja.FavouritesManager
//import com.example.kavyakanaja.GoalManager
//import com.example.kavyakanaja.PoemRepository
//import com.example.kavyakanaja.StreakManager
//import com.example.kavyakanaja.XPManager
//
//// ── Achievement badge data ────────────────────────────────────────
//data class Badge(
//    val emoji: String,
//    val title: String,
//    val description: String,
//    val isUnlocked: Boolean
//)
//
//@Composable
//fun ProgressScreen() {
//    val context   = LocalContext.current
//    val allPoems  = remember { PoemRepository.loadPoems(context) }
//
//    // ── Live stats ────────────────────────────────────────────────
//    val streak        = remember { StreakManager.getStreak(context) }
//    val totalXP       = remember { XPManager.getXP(context) }
//    val level         = remember { XPManager.getLevel(totalXP) }
//    val levelColor    = remember { XPManager.getLevelColor(totalXP) }
//    val nextLevelXP   = remember { XPManager.getNextLevelXP(totalXP) }
//    val favouriteCount = remember { FavouritesManager.getFavouriteIds(context).size }
//    val isPoemRead    = remember { GoalManager.isPoemReadToday(context) }
//    val isQuizDone    = remember { GoalManager.isQuizDoneToday(context) }
//    val goalsToday    = (if (isPoemRead) 1 else 0) + (if (isQuizDone) 1 else 0)
//    val goalProgress  = goalsToday / 2f
//
//    // XP progress toward next level
//    val currentLevelXP = when {
//        totalXP >= 500 -> 500
//        totalXP >= 200 -> 200
//        totalXP >= 80  -> 80
//        else           -> 0
//    }
//    val xpProgress = if (nextLevelXP == currentLevelXP) 1f
//    else (totalXP - currentLevelXP).toFloat() / (nextLevelXP - currentLevelXP).toFloat()
//
//    // ── Badges — unlocked based on real data ──────────────────────
//    val badges = listOf(
//        Badge("🌱", "First Step",      "Open the app for the first time",   streak >= 1),
//        Badge("📖", "Poem Reader",     "Read your first Poem of the Day",   isPoemRead),
//        Badge("🧠", "Quiz Taker",      "Complete your first quiz",           isQuizDone),
//        Badge("❤️", "Poem Lover",      "Save 3 poems to favourites",         favouriteCount >= 3),
//        Badge("🔥", "3-Day Streak",    "Maintain a 3-day streak",           streak >= 3),
//        Badge("⚡", "Scholar",         "Earn 80 XP",                         totalXP >= 80),
//        Badge("📜", "Poet",            "Earn 200 XP",                        totalXP >= 200),
//        Badge("🔥", "Week Warrior",    "Maintain a 7-day streak",           streak >= 7),
//        Badge("💛", "Super Fan",       "Save 5 poems to favourites",         favouriteCount >= 5),
//        Badge("🏅", "Kavi",            "Earn 500 XP — Grand Poet!",          totalXP >= 500),
//        Badge("🏆", "Month Master",    "Maintain a 30-day streak",          streak >= 30),
//        Badge("🌟", "Kavya Master",    "Complete all goals for 7 days",      streak >= 7 && totalXP >= 200),
//    )
//
//    val unlockedCount = badges.count { it.isUnlocked }
//
//    // ── UI ────────────────────────────────────────────────────────
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF0A0A1A),
//                        Color(0xFF141428),
//                        Color(0xFF1E1E3A)
//                    )
//                )
//            )
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .verticalScroll(rememberScrollState())
//                .padding(20.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(20.dp))
//
//            Text(
//                text = "ನನ್ನ ಪ್ರಗತಿ",
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFFFFD700),
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = "My Progress",
//                fontSize = 13.sp,
//                color = Color(0xFFDEB887),
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ── Top stats row: Streak | XP | Favourites ───────────
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(10.dp)
//            ) {
//                // Streak card
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    emoji = "🔥",
//                    value = "$streak",
//                    label = "Day Streak",
//                    color = Color(0xFFFF6B00)
//                )
//                // XP card
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    emoji = "⚡",
//                    value = "$totalXP",
//                    label = "Total XP",
//                    color = Color(0xFFFFD700)
//                )
//                // Favourites card
//                StatCard(
//                    modifier = Modifier.weight(1f),
//                    emoji = "❤️",
//                    value = "$favouriteCount",
//                    label = "Saved",
//                    color = Color(0xFFFF4444)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(14.dp))
//
//            // ── Level & XP progress bar ───────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Column(modifier = Modifier.padding(20.dp)) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = level,
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = levelColor
//                        )
//                        Text(
//                            text = "$totalXP / $nextLevelXP XP",
//                            fontSize = 13.sp,
//                            color = Color(0xFFDEB887)
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(10.dp))
//                    LinearProgressIndicator(
//                        progress = { xpProgress.coerceIn(0f, 1f) },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(12.dp)
//                            .clip(RoundedCornerShape(50)),
//                        color = levelColor,
//                        trackColor = Color(0x33FFD700)
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    val xpNeeded = nextLevelXP - totalXP
//                    Text(
//                        text = if (xpProgress >= 1f) "🏆 Max level reached!"
//                        else "⚡ $xpNeeded XP to next level",
//                        fontSize = 12.sp,
//                        color = Color(0xFFDEB887),
//                        fontStyle = FontStyle.Italic
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(14.dp))
//
//            // ── Today's Goals ring ────────────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 20.dp, vertical = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                        Text(
//                            text = "📅 Today's Goals",
//                            fontSize = 15.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFFFFD700)
//                        )
//                        GoalRow(
//                            done = isPoemRead,
//                            label = "Read Poem of the Day"
//                        )
//                        GoalRow(
//                            done = isQuizDone,
//                            label = "Complete a Quiz"
//                        )
//                        Text(
//                            text = if (goalsToday == 2) "🎯 All goals done!" else "$goalsToday / 2 completed",
//                            fontSize = 12.sp,
//                            color = if (goalsToday == 2) Color(0xFF4CAF50) else Color(0xFFDEB887),
//                            fontWeight = if (goalsToday == 2) FontWeight.Bold else FontWeight.Normal
//                        )
//                    }
//
//                    // Circular progress ring
//                    Box(
//                        modifier = Modifier.size(80.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Canvas(modifier = Modifier.size(80.dp)) {
//                            val strokeWidth = 9.dp.toPx()
//                            val radius = (size.minDimension - strokeWidth) / 2
//                            drawCircle(
//                                color = Color(0x33FFD700),
//                                radius = radius,
//                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
//                            )
//                            if (goalProgress > 0f) {
//                                drawArc(
//                                    color = if (goalProgress >= 1f) Color(0xFF4CAF50)
//                                    else Color(0xFFFFD700),
//                                    startAngle = -90f,
//                                    sweepAngle = 360f * goalProgress,
//                                    useCenter = false,
//                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
//                                )
//                            }
//                        }
//                        Text(
//                            text = if (goalProgress >= 1f) "🎯" else "$goalsToday/2",
//                            fontSize = if (goalProgress >= 1f) 26.sp else 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (goalProgress >= 1f) Color(0xFF4CAF50)
//                            else Color(0xFFFFD700)
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(14.dp))
//
//            // ── Streak motivation card ────────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(
//                    containerColor = if (streak >= 7) Color(0x44FF6B00) else Color(0x22FF6B00)
//                ),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(18.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(14.dp)
//                ) {
//                    Text(text = "🔥", fontSize = 40.sp)
//                    Column {
//                        Text(
//                            text = "$streak Day${if (streak != 1) "s" else ""} Streak",
//                            fontSize = 18.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFFFFD700)
//                        )
//                        Text(
//                            text = when {
//                                streak == 0 -> "Open the app daily to build your streak!"
//                                streak < 3  -> "Great start! Come back tomorrow 🌟"
//                                streak < 7  -> "Awesome! ${7 - streak} days to Week Warrior badge!"
//                                streak < 30 -> "You're on fire! ${30 - streak} days to Month Master!"
//                                else        -> "🏆 Legendary! You're a true Kavya Master!"
//                            },
//                            fontSize = 13.sp,
//                            color = Color(0xFFDEB887),
//                            lineHeight = 20.sp
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ── Badges section ────────────────────────────────────
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "🏅 Badges",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFFFFD700)
//                )
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(Color(0x33FFD700))
//                        .padding(horizontal = 12.dp, vertical = 4.dp)
//                ) {
//                    Text(
//                        text = "$unlockedCount / ${badges.size} unlocked",
//                        fontSize = 12.sp,
//                        color = Color(0xFFDEB887)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // Badge grid — 2 per row
//            badges.chunked(2).forEach { rowBadges ->
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 5.dp),
//                    horizontalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    rowBadges.forEach { badge ->
//                        BadgeCard(
//                            badge = badge,
//                            modifier = Modifier.weight(1f)
//                        )
//                    }
//                    // Fill empty slot if odd number
//                    if (rowBadges.size == 1) {
//                        Spacer(modifier = Modifier.weight(1f))
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}
//
//// ── Reusable stat card ────────────────────────────────────────────
//@Composable
//private fun StatCard(
//    modifier: Modifier = Modifier,
//    emoji: String,
//    value: String,
//    label: String,
//    color: Color
//) {
//    Card(
//        modifier = modifier,
//        colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
//        shape = RoundedCornerShape(14.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            Text(text = emoji, fontSize = 24.sp)
//            Text(
//                text = value,
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold,
//                color = color
//            )
//            Text(
//                text = label,
//                fontSize = 11.sp,
//                color = Color(0xFFDEB887),
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//// ── Reusable goal row ─────────────────────────────────────────────
//@Composable
//private fun GoalRow(done: Boolean, label: String) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        Icon(
//            imageVector = if (done) Icons.Filled.CheckCircle else Icons.Filled.Lock,
//            contentDescription = null,
//            tint = if (done) Color(0xFF4CAF50) else Color(0x55DEB887),
//            modifier = Modifier.size(16.dp)
//        )
//        Text(
//            text = label,
//            fontSize = 13.sp,
//            color = if (done) Color(0xFF4CAF50) else Color(0xFFDEB887)
//        )
//    }
//}
//
//// ── Badge card ────────────────────────────────────────────────────
//@Composable
//private fun BadgeCard(badge: Badge, modifier: Modifier = Modifier) {
//    Card(
//        modifier = modifier.alpha(if (badge.isUnlocked) 1f else 0.45f),
//        colors = CardDefaults.cardColors(
//            containerColor = if (badge.isUnlocked) Color(0x44FFD700) else Color(0x22FFFFFF)
//        ),
//        shape = RoundedCornerShape(14.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(14.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(6.dp)
//        ) {
//            Box(contentAlignment = Alignment.BottomEnd) {
//                Text(
//                    text = badge.emoji,
//                    fontSize = 32.sp,
//                    textAlign = TextAlign.Center
//                )
//                if (badge.isUnlocked) {
//                    Box(
//                        modifier = Modifier
//                            .size(16.dp)
//                            .clip(CircleShape)
//                            .background(Color(0xFF4CAF50)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text("✓", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
//                    }
//                } else {
//                    Icon(
//                        Icons.Filled.Lock,
//                        contentDescription = "Locked",
//                        tint = Color(0x88FFFFFF),
//                        modifier = Modifier.size(14.dp)
//                    )
//                }
//            }
//            Text(
//                text = badge.title,
//                fontSize = 13.sp,
//                fontWeight = FontWeight.Bold,
//                color = if (badge.isUnlocked) Color(0xFFFFD700) else Color(0x88FFFFFF),
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = badge.description,
//                fontSize = 11.sp,
//                color = if (badge.isUnlocked) Color(0xFFDEB887) else Color(0x66FFFFFF),
//                textAlign = TextAlign.Center,
//                lineHeight = 16.sp
//            )
//        }
//    }
//}








package com.example.kavyakanaja.screens

import android.app.TimePickerDialog
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.example.kavyakanaja.NotificationHelper
import com.example.kavyakanaja.StreakManager
import com.example.kavyakanaja.XPManager

// ── Achievement badge data ────────────────────────────────────────
data class Badge(
    val emoji: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean
)

@Composable
fun ProgressScreen() {
    val context = LocalContext.current

    // ── Live stats ────────────────────────────────────────────────
    val streak         = remember { StreakManager.getStreak(context) }
    val totalXP        = remember { XPManager.getXP(context) }
    val level          = remember { XPManager.getLevel(totalXP) }
    val levelColor     = remember { XPManager.getLevelColor(totalXP) }
    val nextLevelXP    = remember { XPManager.getNextLevelXP(totalXP) }
    val favouriteCount = remember { FavouritesManager.getFavouriteIds(context).size }
    val isPoemRead     = remember { GoalManager.isPoemReadToday(context) }
    val isQuizDone     = remember { GoalManager.isQuizDoneToday(context) }
    val goalsToday     = (if (isPoemRead) 1 else 0) + (if (isQuizDone) 1 else 0)
    val goalProgress   = goalsToday / 2f

    // XP progress toward next level
    val currentLevelXP = when {
        totalXP >= 500 -> 500
        totalXP >= 200 -> 200
        totalXP >= 80  -> 80
        else           -> 0
    }
    val xpProgress = if (nextLevelXP == currentLevelXP) 1f
    else (totalXP - currentLevelXP).toFloat() / (nextLevelXP - currentLevelXP).toFloat()

    // ── Notification state — FIXED: explicit <Boolean> type ──────
    var notifEnabled by remember {
        mutableStateOf<Boolean>(NotificationHelper.isEnabled(context))
    }
    var notifHour by remember {
        mutableIntStateOf(NotificationHelper.getSavedHour(context))
    }
    var notifMinute by remember {
        mutableIntStateOf(NotificationHelper.getSavedMinute(context))
    }

    // Permission launcher for Android 13+
    val notifPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            NotificationHelper.scheduleDailyNotification(context, notifHour, notifMinute)
            notifEnabled = true
        }
    }

    // ── Badges ────────────────────────────────────────────────────
    val badges = listOf(
        Badge("🌱", "First Step",   "Open the app for the first time",  streak >= 1),
        Badge("📖", "Poem Reader",  "Read your first Poem of the Day",  isPoemRead),
        Badge("🧠", "Quiz Taker",   "Complete your first quiz",          isQuizDone),
        Badge("❤️", "Poem Lover",   "Save 3 poems to favourites",        favouriteCount >= 3),
        Badge("🔥", "3-Day Streak", "Maintain a 3-day streak",          streak >= 3),
        Badge("⚡", "Scholar",      "Earn 80 XP",                        totalXP >= 80),
        Badge("📜", "Poet",         "Earn 200 XP",                       totalXP >= 200),
        Badge("🔥", "Week Warrior", "Maintain a 7-day streak",          streak >= 7),
        Badge("💛", "Super Fan",    "Save 5 poems to favourites",        favouriteCount >= 5),
        Badge("🏅", "Kavi",         "Earn 500 XP — Grand Poet!",         totalXP >= 500),
        Badge("🏆", "Month Master", "Maintain a 30-day streak",         streak >= 30),
        Badge("🌟", "Kavya Master", "7-day streak + 200 XP",            streak >= 7 && totalXP >= 200),
    )
    val unlockedCount = badges.count { it.isUnlocked }

    // ── UI ────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0A1A),
                        Color(0xFF141428),
                        Color(0xFF1E1E3A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "ನನ್ನ ಪ್ರಗತಿ",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Text(
                text = "My Progress",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Top stats row ─────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(Modifier.weight(1f), "🔥", "$streak", "Day Streak", Color(0xFFFF6B00))
                StatCard(Modifier.weight(1f), "⚡", "$totalXP", "Total XP",   Color(0xFFFFD700))
                StatCard(Modifier.weight(1f), "❤️", "$favouriteCount", "Saved", Color(0xFFFF4444))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Level & XP bar ────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(level, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = levelColor)
                        Text("$totalXP / $nextLevelXP XP", fontSize = 13.sp, color = Color(0xFFDEB887))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { xpProgress.coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().height(12.dp).clip(RoundedCornerShape(50)),
                        color = levelColor,
                        trackColor = Color(0x33FFD700)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (xpProgress >= 1f) "🏆 Max level reached!"
                        else "⚡ ${nextLevelXP - totalXP} XP to next level",
                        fontSize = 12.sp,
                        color = Color(0xFFDEB887),
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Today's Goals ring ────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("📅 Today's Goals", fontSize = 15.sp,
                            fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                        GoalRow(isPoemRead, "Read Poem of the Day")
                        GoalRow(isQuizDone, "Complete a Quiz")
                        Text(
                            text = if (goalsToday == 2) "🎯 All goals done!" else "$goalsToday / 2 completed",
                            fontSize = 12.sp,
                            color = if (goalsToday == 2) Color(0xFF4CAF50) else Color(0xFFDEB887),
                            fontWeight = if (goalsToday == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.size(80.dp)) {
                            val sw = 9.dp.toPx()
                            val r  = (size.minDimension - sw) / 2
                            drawCircle(Color(0x33FFD700), r, style = Stroke(sw, cap = StrokeCap.Round))
                            if (goalProgress > 0f) drawArc(
                                color = if (goalProgress >= 1f) Color(0xFF4CAF50) else Color(0xFFFFD700),
                                startAngle = -90f, sweepAngle = 360f * goalProgress,
                                useCenter = false, style = Stroke(sw, cap = StrokeCap.Round)
                            )
                        }
                        Text(
                            text = if (goalProgress >= 1f) "🎯" else "$goalsToday/2",
                            fontSize = if (goalProgress >= 1f) 26.sp else 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (goalProgress >= 1f) Color(0xFF4CAF50) else Color(0xFFFFD700)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── Streak motivation card ────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (streak >= 7) Color(0x44FF6B00) else Color(0x22FF6B00)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("🔥", fontSize = 40.sp)
                    Column {
                        Text(
                            "$streak Day${if (streak != 1) "s" else ""} Streak",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFD700)
                        )
                        Text(
                            text = when {
                                streak == 0 -> "Open the app daily to build your streak!"
                                streak < 3  -> "Great start! Come back tomorrow 🌟"
                                streak < 7  -> "Awesome! ${7 - streak} days to Week Warrior badge!"
                                streak < 30 -> "You're on fire! ${30 - streak} days to Month Master!"
                                else        -> "🏆 Legendary! You're a true Kavya Master!"
                            },
                            fontSize = 13.sp, color = Color(0xFFDEB887), lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── 🔔 Daily Reminder Section ─────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔔 Daily Reminder",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
                Switch(
                    checked = notifEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notifPermissionLauncher.launch(
                                    android.Manifest.permission.POST_NOTIFICATIONS
                                )
                            } else {
                                NotificationHelper.scheduleDailyNotification(
                                    context, notifHour, notifMinute
                                )
                                notifEnabled = true
                            }
                        } else {
                            NotificationHelper.cancelNotification(context)
                            notifEnabled = false
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor   = Color(0xFF1A0A00),
                        checkedTrackColor   = Color(0xFFFFD700),
                        uncheckedThumbColor = Color(0xFFDEB887),
                        uncheckedTrackColor = Color(0x33FFD700)
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (notifEnabled) Color(0x44FFD700) else Color(0x22FFFFFF)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (notifEnabled) Icons.Filled.Notifications
                                else Icons.Filled.NotificationsOff,
                                contentDescription = null,
                                tint = if (notifEnabled) Color(0xFFFFD700) else Color(0x55DEB887),
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = if (notifEnabled) "Reminder is ON" else "Reminder is OFF",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (notifEnabled) Color(0xFFFFD700) else Color(0x88FFFFFF)
                                )
                                val amPm = if (notifHour < 12) "AM" else "PM"
                                val displayHour = when {
                                    notifHour == 0 -> 12
                                    notifHour > 12 -> notifHour - 12
                                    else           -> notifHour
                                }
                                Text(
                                    text = "%d:%02d %s every day".format(displayHour, notifMinute, amPm),
                                    fontSize = 13.sp,
                                    color = Color(0xFFDEB887)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        notifHour   = hour
                                        notifMinute = minute
                                        if (notifEnabled) {
                                            NotificationHelper.scheduleDailyNotification(
                                                context, hour, minute
                                            )
                                        }
                                    },
                                    notifHour,
                                    notifMinute,
                                    false
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Change Time",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1A0A00)
                            )
                        }
                    }

                    if (notifEnabled) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "📬 You'll receive a daily poem reminder with your streak count and a motivational message!",
                            fontSize = 12.sp,
                            color = Color(0xFFDEB887),
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Badges section ────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🏅 Badges", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x33FFD700))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("$unlockedCount / ${badges.size} unlocked",
                        fontSize = 12.sp, color = Color(0xFFDEB887))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            badges.chunked(2).forEach { rowBadges ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowBadges.forEach { badge ->
                        BadgeCard(badge = badge, modifier = Modifier.weight(1f))
                    }
                    if (rowBadges.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Reusable composables ──────────────────────────────────────────
@Composable
private fun StatCard(modifier: Modifier, emoji: String, value: String, label: String, color: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(emoji, fontSize = 24.sp)
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = color)
            Text(label, fontSize = 11.sp, color = Color(0xFFDEB887), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun GoalRow(done: Boolean, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = if (done) Icons.Filled.CheckCircle else Icons.Filled.Lock,
            contentDescription = null,
            tint = if (done) Color(0xFF4CAF50) else Color(0x55DEB887),
            modifier = Modifier.size(16.dp)
        )
        Text(label, fontSize = 13.sp, color = if (done) Color(0xFF4CAF50) else Color(0xFFDEB887))
    }
}

@Composable
private fun BadgeCard(badge: Badge, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.alpha(if (badge.isUnlocked) 1f else 0.45f),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.isUnlocked) Color(0x44FFD700) else Color(0x22FFFFFF)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Text(badge.emoji, fontSize = 32.sp, textAlign = TextAlign.Center)
                if (badge.isUnlocked) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Icon(
                        Icons.Filled.Lock, "Locked",
                        tint = Color(0x88FFFFFF),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Text(
                badge.title, fontSize = 13.sp, fontWeight = FontWeight.Bold,
                color = if (badge.isUnlocked) Color(0xFFFFD700) else Color(0x88FFFFFF),
                textAlign = TextAlign.Center
            )
            Text(
                badge.description, fontSize = 11.sp,
                color = if (badge.isUnlocked) Color(0xFFDEB887) else Color(0x66FFFFFF),
                textAlign = TextAlign.Center, lineHeight = 16.sp
            )
        }
    }
}