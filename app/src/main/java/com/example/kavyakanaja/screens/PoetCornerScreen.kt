package com.example.kavyakanaja.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Poet(
    val name: String,
    val kannadaName: String,
    val award: String,
    val period: String,
    val description: String,
    val famousWork: String,
    val emoji: String
)

@Composable
fun PoetCornerScreen() {
    val poets = listOf(
        Poet(
            name = "Kuvempu",
            kannadaName = "ಕುವೆಂಪು",
            award = "Jnanpith Award 1967",
            period = "1904 – 1994",
            description = "Kuppali Venkatappa Puttappa, known as Kuvempu, was the first Kannada writer to receive the Jnanpith Award. He is celebrated as the Rashtra Kavi (National Poet) of Karnataka. His works revolve around nature, humanism, and spiritual philosophy.",
            famousWork = "Sri Ramayana Darshanam, Malegalalli Madumagalu",
            emoji = "🌿"
        ),
        Poet(
            name = "Basavanna",
            kannadaName = "ಬಸವಣ್ಣ",
            award = "12th Century Saint-Poet",
            period = "1134 – 1196",
            description = "Basavanna was a 12th-century philosopher, statesman, and social reformer. His Vachanas (prose-poems) preached social equality and devotion to Lord Shiva. He founded the Lingayat movement and fought against caste discrimination.",
            famousWork = "Vachanas — timeless philosophical verses",
            emoji = "🕉️"
        ),
        Poet(
            name = "D.V. Gundappa",
            kannadaName = "ಡಿ.ವಿ.ಗುಂಡಪ್ಪ",
            award = "Padma Bhushan 1974",
            period = "1887 – 1975",
            description = "D.V. Gundappa, fondly called DVG, was a poet, philosopher, and journalist. His masterpiece Mankutimmana Kagga is considered the Bhagavad Gita of Kannada literature for its deep philosophical insights.",
            famousWork = "Mankutimmana Kagga",
            emoji = "📜"
        ),
        Poet(
            name = "Da Ra Bendre",
            kannadaName = "ದ.ರಾ. ಬೇಂದ್ರೆ",
            award = "Jnanpith Award 1973",
            period = "1896 – 1981",
            description = "Dattatreya Ramachandra Bendre, known as Ambikatanayadatta, was a celebrated lyric poet. His poetry is known for its musicality, imagery, and deep emotional resonance. He wrote in both classical and folk traditions of Kannada.",
            famousWork = "Naaku Tanti, Gari",
            emoji = "🎶"
        ),
        Poet(
            name = "Akka Mahadevi",
            kannadaName = "ಅಕ್ಕ ಮಹಾದೇವಿ",
            award = "12th Century Mystic Poet",
            period = "1130 – 1160",
            description = "Akka Mahadevi was a female mystic poet and saint of the Veerashaiva movement. She composed hundreds of Vachanas expressing her devotion to Lord Shiva. She is revered as a symbol of spiritual courage and independence.",
            famousWork = "Vachanas dedicated to Chennamallikarjuna",
            emoji = "🌸"
        ),
        Poet(
            name = "Sarvagna",
            kannadaName = "ಸರ್ವಜ್ಞ",
            award = "People's Poet of Karnataka",
            period = "1160 – 1240",
            description = "Sarvagna meaning all-knowing was a wandering poet who composed over 20,000 triplet verses called Vachanas. His simple yet profound sayings cover every aspect of life from morality to nature to human relationships.",
            famousWork = "Sarvagna Vachanas — 20,000 triplet verses",
            emoji = "⭐"
        ),
        Poet(
            name = "Kanakadasa",
            kannadaName = "ಕನಕದಾಸ",
            award = "16th Century Saint-Poet",
            period = "1509 – 1609",
            description = "Kanakadasa was a saint, poet, philosopher, musician and composer of the Bhakti movement. Born into a lower caste, he challenged social discrimination through his devotional compositions. Legend says the wall of Udupi Krishna temple cracked open for him.",
            famousWork = "Mohanatarangini, Naleshacharitra",
            emoji = "🙏"
        ),
        Poet(
            name = "Purandaradasa",
            kannadaName = "ಪುರಂದರ ದಾಸ",
            award = "Pitamaha of Carnatic Music",
            period = "1484 – 1564",
            description = "Purandaradasa is regarded as the grandfather of Carnatic music. He composed over 4,75,000 songs and standardized the teaching of Carnatic music. His Kritis and Ugabhogas are sung across South India to this day.",
            famousWork = "4,75,000 Kritis & Devaranamas",
            emoji = "🎵"
        ),
        Poet(
            name = "Shivaram Karanth",
            kannadaName = "ಶಿವರಾಮ ಕಾರಂತ",
            award = "Jnanpith Award 1977",
            period = "1902 – 1997",
            description = "Shivaram Karanth was a multifaceted genius — novelist, playwright, filmmaker, environmentalist and Yakshagana artist. He wrote over 500 books covering science, art, nature and fiction. He returned his Padma Bhushan to protest against nuclear testing.",
            famousWork = "Chomana Dudi, Mookajjiya Kanasugalu",
            emoji = "🌊"
        ),
        Poet(
            name = "K.S. Nissar Ahmed",
            kannadaName = "ನಿಸಾರ್ ಅಹಮದ್",
            award = "Rajyotsava Award & Padma Shri",
            period = "1936 – 2020",
            description = "K.S. Nissar Ahmed was one of the most beloved modern Kannada poets. His poem Nityotsava became a cultural anthem of Karnataka. He celebrated nature, freedom and Kannada culture through simple yet deeply resonant verse.",
            famousWork = "Nityotsava, Hakkiharutide Nodidira",
            emoji = "🕊️"
        )
    )

    // Track which card is expanded — only one open at a time
    var expandedIndex by remember { mutableIntStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0A00),
                        Color(0xFF3D2000),
                        Color(0xFF5C3300)
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
                text = "ಕವಿ ಮೂಲೆ",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Poet's Corner",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Tap a poet to read more ↓",
                fontSize = 11.sp,
                color = Color(0xFFDEB887).copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            poets.forEachIndexed { index, poet ->
                val isExpanded = expandedIndex == index

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .animateContentSize(),   // smooth height animation
                    colors = CardDefaults.cardColors(
                        containerColor = if (isExpanded)
                            Color(0x55FFD700) else Color(0x33FFD700)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        // ── Header Row (always visible) — tap to expand/collapse ──
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    expandedIndex = if (isExpanded) -1 else index
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = poet.emoji, fontSize = 36.sp)
                                Column {
                                    Text(
                                        text = poet.kannadaName,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD700)
                                    )
                                    Text(
                                        text = poet.name,
                                        fontSize = 14.sp,
                                        color = Color(0xFFDEB887)
                                    )
                                    Text(
                                        text = poet.period,
                                        fontSize = 11.sp,
                                        color = Color(0xFFDEB887).copy(alpha = 0.7f)
                                    )
                                }
                            }

                            // Expand / Collapse chevron icon
                            Icon(
                                imageVector = if (isExpanded)
                                    Icons.Filled.KeyboardArrowUp
                                else
                                    Icons.Filled.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        // ─────────────────────────────────────────────────────────

                        // ── Expandable content — animated slide in/out ────────────
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(14.dp))

                                // Award pill
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0x44FFD700)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "🏆 ${poet.award}",
                                            modifier = Modifier.padding(
                                                horizontal = 10.dp, vertical = 4.dp
                                            ),
                                            fontSize = 11.sp,
                                            color = Color(0xFFFFD700),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Color(0x44FFD700))
                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = poet.description,
                                    fontSize = 14.sp,
                                    color = Color(0xFFFFFFFF),
                                    lineHeight = 22.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Famous work section
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0x22FFD700))
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column {
                                        Text(
                                            text = "⭐ Famous Work",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD700)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = poet.famousWork,
                                            fontSize = 13.sp,
                                            color = Color(0xFFDEB887),
                                            lineHeight = 20.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Collapse button at bottom
                                TextButton(
                                    onClick = { expandedIndex = -1 },
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Icon(
                                        Icons.Filled.KeyboardArrowUp,
                                        contentDescription = "Collapse",
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Show less",
                                        fontSize = 12.sp,
                                        color = Color(0xFFFFD700)
                                    )
                                }
                            }
                        }
                        // ─────────────────────────────────────────────────────────
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}