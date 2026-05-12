package com.example.kavyakanaja.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.PoemRepository
import com.example.kavyakanaja.XPManager

// ── Data model for one fill-in-the-blank question ─────────────────
data class FillQuestion(
    val poemTitle: String,
    val poet: String,
    val beforeBlank: String,      // verse text before the missing word
    val afterBlank: String,       // verse text after the missing word
    val correctWord: String,      // the hidden word
    val options: List<String>     // 3 options including correct
)

enum class FillState { ANSWERING, CORRECT, WRONG, FINISHED }

@Composable
fun FillBlankScreen() {
    val context = LocalContext.current
    val allPoems = remember { PoemRepository.loadPoems(context) }

    // ── Build questions — pick a meaningful word from each verse ──
    val questions: List<FillQuestion> = remember(allPoems) {
        allPoems.shuffled().mapNotNull { poem ->
            val words = poem.verse
                .split(" ")
                .map { it.trim() }
                .filter { w ->
                    // Pick words that are interesting — Kannada chars,
                    // length >= 3, not punctuation-only
                    w.length >= 3 &&
                            w.any { c -> c.code in 0x0C80..0x0CFF } // Kannada unicode range
                }

            if (words.isEmpty()) return@mapNotNull null

            // Pick the word closest to the middle of the verse for context
            val verseWords = poem.verse.split(" ")
            val midIndex   = verseWords.size / 2
            val chosenWord = words.minByOrNull { w ->
                val idx = verseWords.indexOfFirst { it.trim() == w }
                Math.abs(idx - midIndex)
            } ?: words.first()

            val wordIndex  = verseWords.indexOfFirst { it.trim() == chosenWord }
            if (wordIndex < 0) return@mapNotNull null

            val before = verseWords.take(wordIndex).joinToString(" ")
            val after  = verseWords.drop(wordIndex + 1).joinToString(" ")

            // Wrong options: words from other poems' difficultWords
            val wrongOptions = allPoems
                .filter { it.id != poem.id }
                .flatMap { it.difficultWords }
                .map { it.word.trim() }
                .filter { it != chosenWord && it.length >= 3 }
                .shuffled()
                .take(2)

            // If we can't get 2 wrong options, skip this question
            if (wrongOptions.size < 2) return@mapNotNull null

            val options = (wrongOptions + chosenWord).shuffled()

            FillQuestion(
                poemTitle   = poem.title,
                poet        = poem.poet,
                beforeBlank = before,
                afterBlank  = after,
                correctWord = chosenWord,
                options     = options
            )
        }
    }

    var currentIndex   by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var fillState      by remember { mutableStateOf(FillState.ANSWERING) }
    var score          by remember { mutableIntStateOf(0) }
    var wrongCount     by remember { mutableIntStateOf(0) }
    var livesLeft      by remember { mutableIntStateOf(3) }
    var sessionXP      by remember { mutableIntStateOf(0) }
    var totalXP        by remember { mutableIntStateOf(XPManager.getXP(context)) }
    var showXPPopup    by remember { mutableStateOf(false) }
    var xpPopupText    by remember { mutableStateOf("") }

    LaunchedEffect(showXPPopup) {
        if (showXPPopup) {
            kotlinx.coroutines.delay(1200L)
            showXPPopup = false
        }
    }

    // Guard — if no questions built, show error
    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF0D1B2A)),
            contentAlignment = Alignment.Center
        ) {
            Text("No questions available", color = Color.White)
        }
        return
    }

    // ── Result screen ─────────────────────────────────────────────
    if (fillState == FillState.FINISHED) {
        FillBlankResultScreen(
            score      = score,
            total      = questions.size,
            wrongCount = wrongCount,
            sessionXP  = sessionXP,
            totalXP    = totalXP,
            onRestart  = {
                currentIndex   = 0
                selectedOption = null
                fillState      = FillState.ANSWERING
                score          = 0
                wrongCount     = 0
                livesLeft      = 3
                sessionXP      = 0
                totalXP        = XPManager.getXP(context)
            }
        )
        return
    }

    val question      = questions[currentIndex]
    val totalQ        = questions.size
    val isLastQ       = currentIndex == totalQ - 1
    val hasAnswered   = fillState != FillState.ANSWERING

    // ── UI ────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A000E),
                        Color(0xFF2D0018),
                        Color(0xFF3D0025)
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
                text = "ಖಾಲಿ ತುಂಬಿ",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Fill in the Blank",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ── Lives + XP row ────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) { index ->
                        Icon(
                            imageVector = if (index < livesLeft) Icons.Filled.Favorite
                            else Icons.Filled.FavoriteBorder,
                            contentDescription = "Life",
                            tint = if (index < livesLeft) Color(0xFFFF4444)
                            else Color(0x55FF4444),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x44FFD700))
                        .border(1.dp, Color(0x88FFD700), RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "⚡ $totalXP XP  •  ${XPManager.getLevel(totalXP)}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = XPManager.getLevelColor(totalXP)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Progress row ──────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Q ${currentIndex + 1} / $totalQ",
                    fontSize = 13.sp,
                    color = Color(0xFFDEB887),
                    fontWeight = FontWeight.Bold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("✅ $score",      fontSize = 13.sp, color = Color(0xFF4CAF50))
                    Text("❌ $wrongCount", fontSize = 13.sp, color = Color(0xFFFF6B6B))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / totalQ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50)),
                color = Color(0xFFFFD700),
                trackColor = Color(0x33FFD700)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Verse card with blank ─────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x44FFD700)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✍️",
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = question.poemTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "— ${question.poet}",
                        fontSize = 12.sp,
                        color = Color(0xFFDEB887),
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Fill in the missing Kannada word:",
                        fontSize = 13.sp,
                        color = Color(0xFFDEB887),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // ── Verse with animated blank ─────────────────
                    val blankDisplay = when {
                        !hasAnswered              -> "________"
                        fillState == FillState.CORRECT -> question.correctWord
                        else                      -> selectedOption ?: "________"
                    }
                    val blankColor = when {
                        !hasAnswered              -> Color(0xFFFFD700)
                        fillState == FillState.CORRECT -> Color(0xFF4CAF50)
                        else                      -> Color(0xFFFF6B6B)
                    }

                    // Build annotated verse: before + blank + after
                    val annotated = buildAnnotatedString {
                        if (question.beforeBlank.isNotBlank()) {
                            withStyle(SpanStyle(
                                color = Color(0xFFFFFAF0),
                                fontSize = 17.sp,
                                fontStyle = FontStyle.Italic
                            )) {
                                append("${question.beforeBlank} ")
                            }
                        }
                        withStyle(SpanStyle(
                            color = blankColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            background = Color(0x22FFD700)
                        )) {
                            append("[ $blankDisplay ]")
                        }
                        if (question.afterBlank.isNotBlank()) {
                            withStyle(SpanStyle(
                                color = Color(0xFFFFFAF0),
                                fontSize = 17.sp,
                                fontStyle = FontStyle.Italic
                            )) {
                                append(" ${question.afterBlank}")
                            }
                        }
                    }

                    Text(
                        text = annotated,
                        textAlign = TextAlign.Center,
                        lineHeight = 30.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Choose the missing word:",
                fontSize = 14.sp,
                color = Color(0xFFDEB887),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // ── 3 option buttons ──────────────────────────────────
            question.options.forEachIndexed { index, option ->
                val isSelected = selectedOption == option
                val isCorrect  = option == question.correctWord

                val cardBg = when {
                    !hasAnswered && isSelected -> Color(0x66FFD700)
                    !hasAnswered              -> Color(0x22FFFFFF)
                    isCorrect                 -> Color(0x55006400)
                    isSelected && !isCorrect  -> Color(0x55CC0000)
                    else                      -> Color(0x22FFFFFF)
                }
                val borderCol = when {
                    !hasAnswered && isSelected -> Color(0xFFFFD700)
                    hasAnswered && isCorrect   -> Color(0xFF4CAF50)
                    hasAnswered && isSelected  -> Color(0xFFFF6B6B)
                    else                       -> Color.Transparent
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBg)
                        .border(1.5.dp, borderCol, RoundedCornerShape(14.dp))
                        .clickable(enabled = !hasAnswered) { selectedOption = option }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Letter badge
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    hasAnswered && isCorrect  -> Color(0xFF4CAF50)
                                    hasAnswered && isSelected -> Color(0xFFFF6B6B)
                                    else                      -> Color(0x44FFD700)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = listOf("A", "B", "C")[index],
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    // Result icon
                    if (hasAnswered) {
                        if (isCorrect) Icon(
                            Icons.Filled.CheckCircle, "Correct",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(22.dp)
                        ) else if (isSelected) Icon(
                            Icons.Filled.Close, "Wrong",
                            tint = Color(0xFFFF6B6B),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── +XP popup ─────────────────────────────────────────
            AnimatedVisibility(visible = showXPPopup, enter = fadeIn(), exit = fadeOut()) {
                Text(
                    text = xpPopupText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ── Feedback card ─────────────────────────────────────
            AnimatedVisibility(
                visible = fillState == FillState.CORRECT || fillState == FillState.WRONG,
                enter = fadeIn(), exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (fillState == FillState.CORRECT)
                            Color(0x44006400) else Color(0x44CC0000)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (fillState == FillState.CORRECT)
                                "🎉 ಸರಿಯಾದ ಉತ್ತರ! Correct!"
                            else
                                "❌ ತಪ್ಪು ಉತ್ತರ! Wrong!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (fillState == FillState.CORRECT)
                                Color(0xFF4CAF50) else Color(0xFFFF6B6B),
                            textAlign = TextAlign.Center
                        )
                        if (fillState == FillState.WRONG) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "✅ Answer: ${question.correctWord}",
                                fontSize = 14.sp,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "💔 $livesLeft ${if (livesLeft == 1) "life" else "lives"} remaining",
                                fontSize = 12.sp,
                                color = Color(0xFFFF6B6B),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Submit / Next button ──────────────────────────────
            when (fillState) {
                FillState.ANSWERING -> {
                    Button(
                        onClick = {
                            if (selectedOption != null) {
                                if (selectedOption == question.correctWord) {
                                    score++
                                    val earned = XPManager.XP_PER_CORRECT
                                    sessionXP += earned
                                    totalXP = XPManager.addXP(context, earned)
                                    xpPopupText = "+$earned XP ⚡"
                                    showXPPopup = true
                                    fillState = FillState.CORRECT
                                } else {
                                    wrongCount++
                                    livesLeft--
                                    fillState = if (livesLeft <= 0) FillState.FINISHED
                                    else FillState.WRONG
                                }
                            }
                        },
                        enabled = selectedOption != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700),
                            disabledContainerColor = Color(0x44FFD700)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Text(
                            "Submit Answer",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A000E)
                        )
                    }
                }

                FillState.CORRECT, FillState.WRONG -> {
                    Button(
                        onClick = {
                            if (isLastQ) {
                                fillState = FillState.FINISHED
                            } else {
                                currentIndex++
                                selectedOption = null
                                fillState = FillState.ANSWERING
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Text(
                            text = if (isLastQ) "See Results 🏆" else "Next Question →",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A000E)
                        )
                    }
                }

                FillState.FINISHED -> { /* handled above */ }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Result screen ─────────────────────────────────────────────────
@Composable
fun FillBlankResultScreen(
    score: Int,
    total: Int,
    wrongCount: Int,
    sessionXP: Int,
    totalXP: Int,
    onRestart: () -> Unit
) {
    val percentage = if (total > 0) (score.toFloat() / total * 100).toInt() else 0
    val emoji   = when { percentage >= 80 -> "🏆"; percentage >= 50 -> "👍"; else -> "📚" }
    val message = when {
        percentage >= 80 -> "ಅದ್ಭುತ! Excellent!"
        percentage >= 50 -> "ಚೆನ್ನಾಗಿದೆ! Good job!"
        else             -> "ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ! Keep learning!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A000E), Color(0xFF2D0018), Color(0xFF3D0025))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 72.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message, fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // XP banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x66FFD700)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "+$sessionXP XP earned! ⚡",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Total: $totalXP XP  •  ${XPManager.getLevel(totalXP)}",
                        fontSize = 14.sp,
                        color = XPManager.getLevelColor(totalXP),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Score card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x44FFD700)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "$score / $total",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Text("Score", fontSize = 14.sp, color = Color(0xFFDEB887))
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.CheckCircle, null,
                                tint = Color(0xFF4CAF50), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$score", fontSize = 20.sp,
                                fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                            Text("Correct", fontSize = 12.sp, color = Color(0xFFDEB887))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Close, null,
                                tint = Color(0xFFFF6B6B), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$wrongCount", fontSize = 20.sp,
                                fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
                            Text("Wrong", fontSize = 12.sp, color = Color(0xFFDEB887))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.EmojiEvents, null,
                                tint = Color(0xFFFFD700), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$percentage%", fontSize = 20.sp,
                                fontWeight = FontWeight.Bold, color = Color(0xFFFFD700))
                            Text("Accuracy", fontSize = 12.sp, color = Color(0xFFDEB887))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Filled.Refresh, "Restart",
                    tint = Color(0xFF1A000E), modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Play Again",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A000E)
                )
            }
        }
    }
}