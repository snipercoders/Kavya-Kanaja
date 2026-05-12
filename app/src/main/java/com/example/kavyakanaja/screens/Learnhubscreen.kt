package com.example.kavyakanaja.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.FavouritesManager
import com.example.kavyakanaja.XPManager

@Composable
fun LearnHubScreen(
    onNavigate: (String) -> Unit   // passes route string back to MainActivity
) {
    val context    = LocalContext.current
    val totalXP    = remember { XPManager.getXP(context) }
    val savedCount = remember { FavouritesManager.getFavouriteIds(context).size }

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
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "ಕಲಿಕಾ ಕೇಂದ್ರ",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Learning Hub",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // XP badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0x33FFD700))
                    .border(1.dp, Color(0x66FFD700), RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "⚡ $totalXP XP  •  ${XPManager.getLevel(totalXP)}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = XPManager.getLevelColor(totalXP)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Section: Games ────────────────────────────────────
            SectionHeader(title = "🎮 Games")
            Spacer(modifier = Modifier.height(12.dp))

            // Quiz + Fill Blank side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HubCard(
                    modifier    = Modifier.weight(1f),
                    emoji       = "🏆",
                    title       = "ಕ್ವಿಜ್",
                    subtitle    = "Kavya Quiz",
                    description = "Test your knowledge of poem meanings",
                    gradient    = listOf(Color(0xFF0D1B2A), Color(0xFF1B3A5C)),
                    accentColor = Color(0xFF4FC3F7),
                    onClick     = { onNavigate("quiz") }
                )
                HubCard(
                    modifier    = Modifier.weight(1f),
                    emoji       = "✍️",
                    title       = "ಖಾಲಿ ತುಂಬಿ",
                    subtitle    = "Fill the Blank",
                    description = "Complete the missing word in the verse",
                    gradient    = listOf(Color(0xFF1A000E), Color(0xFF3D0025)),
                    accentColor = Color(0xFFFF80AB),
                    onClick     = { onNavigate("fillblank") }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Section: Study ────────────────────────────────────
            SectionHeader(title = "📖 Study")
            Spacer(modifier = Modifier.height(12.dp))

            // Bhavartha — full width
            HubCardWide(
                emoji       = "📚",
                title       = "ಭಾವಾರ್ಥ",
                subtitle    = "Word Meanings & Explanation",
                description = "Tap highlighted Kannada words to reveal their meanings. Search any poem or poet.",
                gradient    = listOf(Color(0xFF0A1A0A), Color(0xFF1A3A1A)),
                accentColor = Color(0xFF81C784),
                badge       = null,
                onClick     = { onNavigate("bhavartha") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Favourites — full width
            HubCardWide(
                emoji       = "❤️",
                title       = "ನೆಚ್ಚಿನ ಕವನಗಳು",
                subtitle    = "My Favourite Poems",
                description = "All your saved poems in one place. Tap to expand verse, meaning and bhavartha.",
                gradient    = listOf(Color(0xFF1A0005), Color(0xFF3A0015)),
                accentColor = Color(0xFFFF4444),
                badge       = if (savedCount > 0) "$savedCount saved" else null,
                onClick     = { onNavigate("favourites") }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Quick stats row ───────────────────────────────────
            SectionHeader(title = "⚡ Quick Stats")
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MiniStat(
                    modifier = Modifier.weight(1f),
                    label = "Level",
                    value = XPManager.getLevel(totalXP).split(" ").last(),
                    color = XPManager.getLevelColor(totalXP)
                )
                MiniStat(
                    modifier = Modifier.weight(1f),
                    label = "Total XP",
                    value = "$totalXP",
                    color = Color(0xFFFFD700)
                )
                MiniStat(
                    modifier = Modifier.weight(1f),
                    label = "Saved",
                    value = "$savedCount",
                    color = Color(0xFFFF4444)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Section header ────────────────────────────────────────────────
@Composable
private fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFDEB887)
        )
        Spacer(modifier = Modifier.width(10.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = Color(0x33FFD700)
        )
    }
}

// ── Square hub card (for games) ───────────────────────────────────
@Composable
private fun HubCard(
    modifier: Modifier = Modifier,
    emoji: String,
    title: String,
    subtitle: String,
    description: String,
    gradient: List<Color>,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(gradient))
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(18.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = emoji, fontSize = 36.sp)
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = accentColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = Color(0xAAFFFFFF),
                lineHeight = 16.sp
            )

            // Tap hint
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(accentColor.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Play →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        }
    }
}

// ── Wide hub card (for study) ─────────────────────────────────────
@Composable
private fun HubCardWide(
    emoji: String,
    title: String,
    subtitle: String,
    description: String,
    gradient: List<Color>,
    accentColor: Color,
    badge: String?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(gradient))
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = emoji, fontSize = 40.sp)
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    if (badge != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(accentColor.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badge,
                                fontSize = 10.sp,
                                color = accentColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = Color(0xAAFFFFFF),
                    lineHeight = 18.sp
                )
            }
            Text(
                text = "→",
                fontSize = 22.sp,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ── Mini stat box ─────────────────────────────────────────────────
@Composable
private fun MiniStat(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    color: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0x22FFD700)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )
        }
    }
}