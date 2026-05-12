package com.example.kavyakanaja.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.FavouritesManager
import com.example.kavyakanaja.PoemRepository

@Composable
fun FavouritesScreen() {
    val context = LocalContext.current
    val allPoems = remember { PoemRepository.loadPoems(context) }

    // Reload favourites every time screen is visible using a mutable state
    var favouriteIds by remember { mutableStateOf(FavouritesManager.getFavouriteIds(context)) }
    val favouritePoems = allPoems.filter { it.id in favouriteIds }

    var expandedIndex by remember { mutableIntStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A000A),
                        Color(0xFF3A001A),
                        Color(0xFF5C0020)
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
                text = "❤️ ನೆಚ್ಚಿನ ಕವನಗಳು",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Text(
                text = "My Favourite Poems",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Count badge
            Box(
                modifier = Modifier
                    .background(Color(0x33FF4444), RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${favouritePoems.size} poem${if (favouritePoems.size != 1) "s" else ""} saved",
                    fontSize = 12.sp,
                    color = Color(0xFFFF8888),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Empty state ───────────────────────────────────────────
            if (favouritePoems.isEmpty()) {
                Spacer(modifier = Modifier.height(60.dp))
                Text(text = "🤍", fontSize = 64.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No favourites yet!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap the ❤️ on any poem\nin Poem of the Day to save it here.",
                    fontSize = 14.sp,
                    color = Color(0xFFDEB887),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            } else {

                // ── Favourite poem cards ──────────────────────────────
                favouritePoems.forEachIndexed { index, poem ->
                    val isExpanded = expandedIndex == index

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isExpanded)
                                Color(0x55FF4444) else Color(0x33FF4444)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {

                            // ── Header row — tap to expand ────────────
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Left: emoji + title + poet
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "❤️", fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = poem.title,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFD700)
                                        )
                                        Text(
                                            text = "— ${poem.poet}",
                                            fontSize = 12.sp,
                                            color = Color(0xFFDEB887),
                                            fontStyle = FontStyle.Italic
                                        )
                                        Text(
                                            text = poem.era,
                                            fontSize = 10.sp,
                                            color = Color(0xFFDEB887).copy(alpha = 0.6f)
                                        )
                                    }
                                }

                                // Right: expand chevron + unsave button
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Unsave button
                                    IconButton(
                                        onClick = {
                                            FavouritesManager.removeFavourite(context, poem.id)
                                            favouriteIds = FavouritesManager.getFavouriteIds(context)
                                            if (expandedIndex == index) expandedIndex = -1
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = "Remove from favourites",
                                            tint = Color(0xFFFF4444),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    // Expand/collapse chevron
                                    IconButton(
                                        onClick = {
                                            expandedIndex = if (isExpanded) -1 else index
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isExpanded)
                                                Icons.Filled.KeyboardArrowUp
                                            else
                                                Icons.Filled.KeyboardArrowDown,
                                            contentDescription = if (isExpanded)
                                                "Collapse" else "Expand",
                                            tint = Color(0xFFFFD700),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                            // ─────────────────────────────────────────

                            // ── Expandable content ────────────────────
                            AnimatedVisibility(
                                visible = isExpanded,
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Column {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    HorizontalDivider(color = Color(0x44FFD700))
                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Verse
                                    Text(
                                        text = poem.verse,
                                        fontSize = 16.sp,
                                        color = Color(0xFFFFFAF0),
                                        textAlign = TextAlign.Center,
                                        lineHeight = 28.sp,
                                        fontStyle = FontStyle.Italic,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    // Meaning section
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0x22FFFFFF)
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(
                                                text = "📖 Meaning",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFD700)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = poem.meaning,
                                                fontSize = 14.sp,
                                                color = Color(0xFFFFFFFF),
                                                lineHeight = 22.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Bhavartha section
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0x22FFFFFF)
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Text(
                                                text = "💡 Bhavartha",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFD700)
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = poem.bhavartha,
                                                fontSize = 14.sp,
                                                color = Color(0xFFFFFFFF),
                                                lineHeight = 22.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Collapse button
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
                            // ─────────────────────────────────────────
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}