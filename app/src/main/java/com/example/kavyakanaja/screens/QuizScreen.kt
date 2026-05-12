//
//
//
//
//
//package com.example.kavyakanaja.screens
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.EmojiEvents
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material.icons.filled.Refresh
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.kavyakanaja.GoalManager
//import com.example.kavyakanaja.PoemRepository
//import com.example.kavyakanaja.XPManager
//import kotlinx.coroutines.delay
//
//// ── Data model for a single quiz question ────────────────────────
//data class QuizQuestion(
//    val verseSnippet: String,
//    val poet: String,
//    val correctAnswer: String,
//    val options: List<String>
//)
//
//// ── Quiz states ───────────────────────────────────────────────────
//enum class QuizState { ANSWERING, CORRECT, WRONG, FINISHED }
//
//@Composable
//fun QuizScreen() {
//    val context = LocalContext.current
//    val allPoems = remember { PoemRepository.loadPoems(context) }
//
//    val questions: List<QuizQuestion> = remember(allPoems) {
//        allPoems.shuffled().map { poem ->
//            val wrongOptions = allPoems
//                .filter { it.id != poem.id }
//                .shuffled()
//                .take(3)
//                .map { it.meaning }
//            val options = (wrongOptions + poem.meaning).shuffled()
//            QuizQuestion(
//                verseSnippet = poem.verse,
//                poet = poem.poet,
//                correctAnswer = poem.meaning,
//                options = options
//            )
//        }
//    }
//
//    var currentQuestionIndex by remember { mutableIntStateOf(0) }
//    var selectedAnswer by remember { mutableStateOf<String?>(null) }
//    var quizState by remember { mutableStateOf(QuizState.ANSWERING) }
//    var score by remember { mutableIntStateOf(0) }
//    var wrongCount by remember { mutableIntStateOf(0) }
//    var timeLeft by remember { mutableIntStateOf(15) }
//
//    // ── Lives system — 3 hearts ───────────────────────────────────
//    var livesLeft by remember { mutableIntStateOf(3) }
//
//    // ── XP earned this session ────────────────────────────────────
//    var sessionXP by remember { mutableIntStateOf(0) }
//    var totalXP by remember { mutableIntStateOf(XPManager.getXP(context)) }
//
//    // ── XP popup ─────────────────────────────────────────────────
//    var showXPPopup by remember { mutableStateOf(false) }
//    var xpPopupText by remember { mutableStateOf("") }
//
//    LaunchedEffect(currentQuestionIndex, quizState) {
//        timeLeft = 15
//        while (timeLeft > 0 && quizState == QuizState.ANSWERING) {
//            delay(1000L)
//            timeLeft--
//        }
//        if (timeLeft == 0 && quizState == QuizState.ANSWERING) {
//            wrongCount++
//            livesLeft--
//            quizState = if (livesLeft <= 0) QuizState.FINISHED else QuizState.WRONG
//        }
//    }
//
//    LaunchedEffect(showXPPopup) {
//        if (showXPPopup) {
//            delay(1200L)
//            showXPPopup = false
//        }
//    }
//
//    val currentQuestion = questions[currentQuestionIndex]
//    val totalQuestions = questions.size
//
//    // ── Result screen ─────────────────────────────────────────────
//    if (quizState == QuizState.FINISHED) {
//        GoalManager.markQuizDone(context)
//        QuizResultScreen(
//            score = score,
//            total = totalQuestions,
//            wrongCount = wrongCount,
//            sessionXP = sessionXP,
//            totalXP = totalXP,
//            onRestart = {
//                currentQuestionIndex = 0
//                selectedAnswer = null
//                quizState = QuizState.ANSWERING
//                score = 0
//                wrongCount = 0
//                livesLeft = 3
//                sessionXP = 0
//                totalXP = XPManager.getXP(context)
//            }
//        )
//        return
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B2A3B), Color(0xFF243447))
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
//                text = "ಕವ್ಯ ಕ್ವಿಜ್",
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFFFFD700),
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = "Kavya Quiz",
//                fontSize = 13.sp,
//                color = Color(0xFFDEB887),
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(14.dp))
//
//            // ── Lives + XP row ────────────────────────────────────
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                // ❤️ Lives
//                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                    repeat(3) { index ->
//                        Icon(
//                            imageVector = if (index < livesLeft) Icons.Filled.Favorite
//                            else Icons.Filled.FavoriteBorder,
//                            contentDescription = "Life",
//                            tint = if (index < livesLeft) Color(0xFFFF4444)
//                            else Color(0x55FF4444),
//                            modifier = Modifier.size(24.dp)
//                        )
//                    }
//                }
//
//                // ⚡ XP + Level badge
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(Color(0x44FFD700))
//                        .border(1.dp, Color(0x88FFD700), RoundedCornerShape(50))
//                        .padding(horizontal = 12.dp, vertical = 5.dp)
//                ) {
//                    Text(
//                        text = "⚡ $totalXP XP  •  ${XPManager.getLevel(totalXP)}",
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = XPManager.getLevelColor(totalXP)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // ── Progress + timer row ──────────────────────────────
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Q ${currentQuestionIndex + 1} / $totalQuestions",
//                    fontSize = 13.sp,
//                    color = Color(0xFFDEB887),
//                    fontWeight = FontWeight.Bold
//                )
//                Box(
//                    modifier = Modifier
//                        .size(40.dp)
//                        .clip(CircleShape)
//                        .background(if (timeLeft <= 5) Color(0xFFCC0000) else Color(0x44FFD700)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("$timeLeft", fontSize = 14.sp, fontWeight = FontWeight.Bold,
//                        color = Color.White)
//                }
//                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
//                    Text("✅ $score", fontSize = 13.sp, color = Color(0xFF4CAF50))
//                    Text("❌ $wrongCount", fontSize = 13.sp, color = Color(0xFFFF6B6B))
//                }
//            }
//
//            Spacer(modifier = Modifier.height(6.dp))
//            LinearProgressIndicator(
//                progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions },
//                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
//                color = Color(0xFFFFD700),
//                trackColor = Color(0x33FFD700)
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // ── Verse card ────────────────────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x44FFD700)),
//                shape = RoundedCornerShape(20.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(24.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(text = "📜", fontSize = 36.sp, textAlign = TextAlign.Center)
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Text(
//                        text = "What is the meaning of this verse?",
//                        fontSize = 13.sp,
//                        color = Color(0xFFDEB887),
//                        textAlign = TextAlign.Center
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Text(
//                        text = currentQuestion.verseSnippet,
//                        fontSize = 17.sp,
//                        color = Color(0xFFFFFAF0),
//                        textAlign = TextAlign.Center,
//                        lineHeight = 28.sp,
//                        fontStyle = FontStyle.Italic
//                    )
//                    Spacer(modifier = Modifier.height(10.dp))
//                    Text(
//                        text = "— ${currentQuestion.poet}",
//                        fontSize = 13.sp,
//                        color = Color(0xFFDEB887),
//                        fontStyle = FontStyle.Italic
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Text(
//                text = "Choose the correct meaning:",
//                fontSize = 14.sp,
//                color = Color(0xFFDEB887),
//                modifier = Modifier.align(Alignment.Start)
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // ── Answer options ────────────────────────────────────
//            currentQuestion.options.forEachIndexed { index, option ->
//                val isSelected  = selectedAnswer == option
//                val isCorrect   = option == currentQuestion.correctAnswer
//                val hasAnswered = quizState != QuizState.ANSWERING
//
//                val cardColor = when {
//                    !hasAnswered && isSelected -> Color(0x66FFD700)
//                    !hasAnswered              -> Color(0x22FFFFFF)
//                    isCorrect                 -> Color(0x55006400)
//                    isSelected && !isCorrect  -> Color(0x55CC0000)
//                    else                      -> Color(0x22FFFFFF)
//                }
//                val borderColor = when {
//                    !hasAnswered && isSelected -> Color(0xFFFFD700)
//                    hasAnswered && isCorrect   -> Color(0xFF4CAF50)
//                    hasAnswered && isSelected  -> Color(0xFFFF6B6B)
//                    else                       -> Color.Transparent
//                }
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 5.dp)
//                        .clip(RoundedCornerShape(14.dp))
//                        .background(cardColor)
//                        .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
//                        .clickable(enabled = !hasAnswered) { selectedAnswer = option }
//                        .padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(28.dp)
//                            .clip(CircleShape)
//                            .background(
//                                if (hasAnswered && isCorrect) Color(0xFF4CAF50)
//                                else if (hasAnswered && isSelected) Color(0xFFFF6B6B)
//                                else Color(0x44FFD700)
//                            ),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = listOf("A", "B", "C", "D")[index],
//                            fontSize = 13.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.White
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Text(
//                        text = option,
//                        fontSize = 14.sp,
//                        color = Color(0xFFFFFFFF),
//                        lineHeight = 20.sp,
//                        modifier = Modifier.weight(1f)
//                    )
//                    if (hasAnswered) {
//                        if (isCorrect) Icon(
//                            Icons.Filled.CheckCircle, "Correct",
//                            tint = Color(0xFF4CAF50), modifier = Modifier.size(22.dp)
//                        )
//                        else if (isSelected) Icon(
//                            Icons.Filled.Close, "Wrong",
//                            tint = Color(0xFFFF6B6B), modifier = Modifier.size(22.dp)
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ── +XP popup ─────────────────────────────────────────
//            AnimatedVisibility(visible = showXPPopup, enter = fadeIn(), exit = fadeOut()) {
//                Text(
//                    text = xpPopupText,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFFFFD700),
//                    textAlign = TextAlign.Center
//                )
//            }
//
//            Spacer(modifier = Modifier.height(6.dp))
//
//            // ── Feedback message ──────────────────────────────────
//            AnimatedVisibility(
//                visible = quizState == QuizState.CORRECT || quizState == QuizState.WRONG,
//                enter = fadeIn(), exit = fadeOut()
//            ) {
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = if (quizState == QuizState.CORRECT)
//                            Color(0x44006400) else Color(0x44CC0000)
//                    ),
//                    shape = RoundedCornerShape(14.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = if (quizState == QuizState.CORRECT)
//                                "🎉 ಸರಿಯಾದ ಉತ್ತರ! Correct!"
//                            else if (timeLeft == 0)
//                                "⏰ ಸಮಯ ಮುಗಿಯಿತು! Time's Up!"
//                            else
//                                "❌ ತಪ್ಪು ಉತ್ತರ! Wrong!",
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = if (quizState == QuizState.CORRECT)
//                                Color(0xFF4CAF50) else Color(0xFFFF6B6B),
//                            textAlign = TextAlign.Center
//                        )
//                        if (quizState == QuizState.WRONG && livesLeft > 0) {
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text(
//                                text = "💔 $livesLeft ${if (livesLeft == 1) "life" else "lives"} remaining!",
//                                fontSize = 12.sp,
//                                color = Color(0xFFFF6B6B),
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                        if (quizState == QuizState.WRONG) {
//                            Spacer(modifier = Modifier.height(6.dp))
//                            Text(
//                                text = "Correct: ${currentQuestion.correctAnswer}",
//                                fontSize = 13.sp,
//                                color = Color(0xFF4CAF50),
//                                textAlign = TextAlign.Center,
//                                lineHeight = 20.sp
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            val isLastQuestion = currentQuestionIndex == totalQuestions - 1
//
//            when (quizState) {
//                QuizState.ANSWERING -> {
//                    Button(
//                        onClick = {
//                            if (selectedAnswer != null) {
//                                if (selectedAnswer == currentQuestion.correctAnswer) {
//                                    score++
//                                    val earned = XPManager.XP_PER_CORRECT
//                                    sessionXP += earned
//                                    totalXP = XPManager.addXP(context, earned)
//                                    xpPopupText = "+${earned} XP ⚡"
//                                    showXPPopup = true
//                                    quizState = QuizState.CORRECT
//                                } else {
//                                    wrongCount++
//                                    livesLeft--
//                                    quizState = if (livesLeft <= 0) QuizState.FINISHED
//                                    else QuizState.WRONG
//                                }
//                            }
//                        },
//                        enabled = selectedAnswer != null,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFFFFD700),
//                            disabledContainerColor = Color(0x44FFD700)
//                        ),
//                        shape = RoundedCornerShape(50),
//                        modifier = Modifier.fillMaxWidth().height(52.dp)
//                    ) {
//                        Text("Submit Answer", fontSize = 16.sp, fontWeight = FontWeight.Bold,
//                            color = Color(0xFF0D1B2A))
//                    }
//                }
//
//                QuizState.CORRECT, QuizState.WRONG -> {
//                    Button(
//                        onClick = {
//                            if (isLastQuestion) quizState = QuizState.FINISHED
//                            else {
//                                currentQuestionIndex++
//                                selectedAnswer = null
//                                quizState = QuizState.ANSWERING
//                            }
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
//                        shape = RoundedCornerShape(50),
//                        modifier = Modifier.fillMaxWidth().height(52.dp)
//                    ) {
//                        Text(
//                            text = if (isLastQuestion) "See Results 🏆" else "Next Question →",
//                            fontSize = 16.sp, fontWeight = FontWeight.Bold,
//                            color = Color(0xFF0D1B2A)
//                        )
//                    }
//                }
//
//                QuizState.FINISHED -> { /* handled above */ }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}
//
//// ── Result Screen ─────────────────────────────────────────────────
//@Composable
//fun QuizResultScreen(
//    score: Int,
//    total: Int,
//    wrongCount: Int,
//    sessionXP: Int,
//    totalXP: Int,
//    onRestart: () -> Unit
//) {
//    val percentage = (score.toFloat() / total * 100).toInt()
//    val emoji   = when { percentage >= 80 -> "🏆"; percentage >= 50 -> "👍"; else -> "📚" }
//    val message = when {
//        percentage >= 80 -> "ಅದ್ಭುತ! Excellent!"
//        percentage >= 50 -> "ಚೆನ್ನಾಗಿದೆ! Good job!"
//        else             -> "ಮತ್ತೆ ಪ್ರಯತ್ನಿಸಿ! Keep learning!"
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B2A3B), Color(0xFF243447))
//                )
//            ),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .verticalScroll(rememberScrollState())
//                .padding(28.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = emoji, fontSize = 72.sp, textAlign = TextAlign.Center)
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = message, fontSize = 24.sp, fontWeight = FontWeight.Bold,
//                color = Color(0xFFFFD700), textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ── XP earned banner ──────────────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x66FFD700)),
//                shape = RoundedCornerShape(14.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "+$sessionXP XP earned! ⚡",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFFD700)
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = "Total: $totalXP XP  •  ${XPManager.getLevel(totalXP)}",
//                        fontSize = 14.sp,
//                        color = XPManager.getLevelColor(totalXP),
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x44FFD700)),
//                shape = RoundedCornerShape(20.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(28.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFFD700))
//                    Text("Score", fontSize = 14.sp, color = Color(0xFFDEB887))
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF4CAF50),
//                                modifier = Modifier.size(28.dp))
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text("$score", fontSize = 20.sp, fontWeight = FontWeight.Bold,
//                                color = Color(0xFF4CAF50))
//                            Text("Correct", fontSize = 12.sp, color = Color(0xFFDEB887))
//                        }
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Icon(Icons.Filled.Close, null, tint = Color(0xFFFF6B6B),
//                                modifier = Modifier.size(28.dp))
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text("$wrongCount", fontSize = 20.sp, fontWeight = FontWeight.Bold,
//                                color = Color(0xFFFF6B6B))
//                            Text("Wrong", fontSize = 12.sp, color = Color(0xFFDEB887))
//                        }
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Icon(Icons.Filled.EmojiEvents, null, tint = Color(0xFFFFD700),
//                                modifier = Modifier.size(28.dp))
//                            Spacer(modifier = Modifier.height(4.dp))
//                            Text("$percentage%", fontSize = 20.sp, fontWeight = FontWeight.Bold,
//                                color = Color(0xFFFFD700))
//                            Text("Accuracy", fontSize = 12.sp, color = Color(0xFFDEB887))
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(28.dp))
//
//            Button(
//                onClick = onRestart,
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
//                shape = RoundedCornerShape(50),
//                modifier = Modifier.fillMaxWidth().height(56.dp)
//            ) {
//                Icon(Icons.Filled.Refresh, "Restart", tint = Color(0xFF0D1B2A),
//                    modifier = Modifier.size(22.dp))
//                Spacer(modifier = Modifier.width(10.dp))
//                Text("Play Again", fontSize = 17.sp, fontWeight = FontWeight.Bold,
//                    color = Color(0xFF0D1B2A))
//            }
//        }
//    }
//}










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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.GoalManager
import com.example.kavyakanaja.PoemRepository
import com.example.kavyakanaja.XPManager
import kotlinx.coroutines.delay

// ── High Score helpers (SharedPreferences, no extra file needed) ──
private const val HS_PREFS = "kavya_highscore_prefs"
private const val HS_KEY   = "quiz_high_score"

private fun getHighScore(context: android.content.Context): Int =
    context.getSharedPreferences(HS_PREFS, android.content.Context.MODE_PRIVATE)
        .getInt(HS_KEY, 0)

private fun saveHighScore(context: android.content.Context, score: Int) {
    context.getSharedPreferences(HS_PREFS, android.content.Context.MODE_PRIVATE)
        .edit().putInt(HS_KEY, score).apply()
}

// ── Data model ────────────────────────────────────────────────────
data class QuizQuestion(
    val verseSnippet: String,
    val poet: String,
    val correctAnswer: String,
    val options: List<String>
)

// ── Quiz states ───────────────────────────────────────────────────
enum class QuizState { ANSWERING, CORRECT, WRONG, FINISHED }

@Composable
fun QuizScreen() {
    val context = LocalContext.current
    val allPoems = remember { PoemRepository.loadPoems(context) }

    val questions: List<QuizQuestion> = remember(allPoems) {
        allPoems.shuffled().map { poem ->
            val wrongOptions = allPoems
                .filter { it.id != poem.id }
                .shuffled()
                .take(3)
                .map { it.meaning }
            val options = (wrongOptions + poem.meaning).shuffled()
            QuizQuestion(
                verseSnippet = poem.verse,
                poet = poem.poet,
                correctAnswer = poem.meaning,
                options = options
            )
        }
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var quizState by remember { mutableStateOf(QuizState.ANSWERING) }
    var score by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var timeLeft by remember { mutableIntStateOf(15) }
    var livesLeft by remember { mutableIntStateOf(3) }
    var sessionXP by remember { mutableIntStateOf(0) }
    var totalXP by remember { mutableIntStateOf(XPManager.getXP(context)) }
    var showXPPopup by remember { mutableStateOf(false) }
    var xpPopupText by remember { mutableStateOf("") }

    // ── High Score ────────────────────────────────────────────────
    val highScore = remember { mutableIntStateOf(getHighScore(context)) }

    LaunchedEffect(currentQuestionIndex, quizState) {
        timeLeft = 15
        while (timeLeft > 0 && quizState == QuizState.ANSWERING) {
            delay(1000L)
            timeLeft--
        }
        if (timeLeft == 0 && quizState == QuizState.ANSWERING) {
            wrongCount++
            livesLeft--
            quizState = if (livesLeft <= 0) QuizState.FINISHED else QuizState.WRONG
        }
    }

    LaunchedEffect(showXPPopup) {
        if (showXPPopup) {
            delay(1200L)
            showXPPopup = false
        }
    }

    val currentQuestion = questions[currentQuestionIndex]
    val totalQuestions = questions.size

    // ── Result screen ─────────────────────────────────────────────
    if (quizState == QuizState.FINISHED) {
        GoalManager.markQuizDone(context)

        // ── Save high score if beaten ─────────────────────────────
        val isNewHighScore = score > highScore.intValue
        if (isNewHighScore) {
            saveHighScore(context, score)
            highScore.intValue = score
        }

        QuizResultScreen(
            score = score,
            total = totalQuestions,
            wrongCount = wrongCount,
            sessionXP = sessionXP,
            totalXP = totalXP,
            highScore = highScore.intValue,
            isNewHighScore = isNewHighScore,
            onRestart = {
                currentQuestionIndex = 0
                selectedAnswer = null
                quizState = QuizState.ANSWERING
                score = 0
                wrongCount = 0
                livesLeft = 3
                sessionXP = 0
                totalXP = XPManager.getXP(context)
            }
        )
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B2A3B), Color(0xFF243447))
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
                text = "ಕವ್ಯ ಕ್ವಿಜ್",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Kavya Quiz",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ── Lives + XP + High Score row ───────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ❤️ Lives
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(3) { index ->
                        Icon(
                            imageVector = if (index < livesLeft) Icons.Filled.Favorite
                            else Icons.Filled.FavoriteBorder,
                            contentDescription = "Life",
                            tint = if (index < livesLeft) Color(0xFFFF4444) else Color(0x55FF4444),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // 🏆 High Score badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x33FFD700))
                        .border(1.dp, Color(0x88FFD700), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "🏆 Best: ${highScore.intValue}/$totalQuestions",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }

                // ⚡ XP + Level badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x44FFD700))
                        .border(1.dp, Color(0x88FFD700), RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "⚡ $totalXP XP",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = XPManager.getLevelColor(totalXP)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ── Progress + timer row ──────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Q ${currentQuestionIndex + 1} / $totalQuestions",
                    fontSize = 13.sp,
                    color = Color(0xFFDEB887),
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (timeLeft <= 5) Color(0xFFCC0000) else Color(0x44FFD700)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$timeLeft", fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        color = Color.White)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("✅ $score", fontSize = 13.sp, color = Color(0xFF4CAF50))
                    Text("❌ $wrongCount", fontSize = 13.sp, color = Color(0xFFFF6B6B))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
                color = Color(0xFFFFD700),
                trackColor = Color(0x33FFD700)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Verse card ────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x44FFD700)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📜", fontSize = 36.sp, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "What is the meaning of this verse?",
                        fontSize = 13.sp,
                        color = Color(0xFFDEB887),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = currentQuestion.verseSnippet,
                        fontSize = 17.sp,
                        color = Color(0xFFFFFAF0),
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "— ${currentQuestion.poet}",
                        fontSize = 13.sp,
                        color = Color(0xFFDEB887),
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Choose the correct meaning:",
                fontSize = 14.sp,
                color = Color(0xFFDEB887),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // ── Answer options ────────────────────────────────────
            currentQuestion.options.forEachIndexed { index, option ->
                val isSelected  = selectedAnswer == option
                val isCorrect   = option == currentQuestion.correctAnswer
                val hasAnswered = quizState != QuizState.ANSWERING

                val cardColor = when {
                    !hasAnswered && isSelected -> Color(0x66FFD700)
                    !hasAnswered              -> Color(0x22FFFFFF)
                    isCorrect                 -> Color(0x55006400)
                    isSelected && !isCorrect  -> Color(0x55CC0000)
                    else                      -> Color(0x22FFFFFF)
                }
                val borderColor = when {
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
                        .background(cardColor)
                        .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                        .clickable(enabled = !hasAnswered) { selectedAnswer = option }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (hasAnswered && isCorrect) Color(0xFF4CAF50)
                                else if (hasAnswered && isSelected) Color(0xFFFF6B6B)
                                else Color(0x44FFD700)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = listOf("A", "B", "C", "D")[index],
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = option,
                        fontSize = 14.sp,
                        color = Color(0xFFFFFFFF),
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (hasAnswered) {
                        if (isCorrect) Icon(
                            Icons.Filled.CheckCircle, "Correct",
                            tint = Color(0xFF4CAF50), modifier = Modifier.size(22.dp)
                        )
                        else if (isSelected) Icon(
                            Icons.Filled.Close, "Wrong",
                            tint = Color(0xFFFF6B6B), modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── +XP popup ─────────────────────────────────────────
            AnimatedVisibility(visible = showXPPopup, enter = fadeIn(), exit = fadeOut()) {
                Text(
                    text = xpPopupText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ── Feedback message ──────────────────────────────────
            AnimatedVisibility(
                visible = quizState == QuizState.CORRECT || quizState == QuizState.WRONG,
                enter = fadeIn(), exit = fadeOut()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (quizState == QuizState.CORRECT)
                            Color(0x44006400) else Color(0x44CC0000)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (quizState == QuizState.CORRECT)
                                "🎉 ಸರಿಯಾದ ಉತ್ತರ! Correct!"
                            else if (timeLeft == 0)
                                "⏰ ಸಮಯ ಮುಗಿಯಿತು! Time's Up!"
                            else
                                "❌ ತಪ್ಪು ಉತ್ತರ! Wrong!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (quizState == QuizState.CORRECT)
                                Color(0xFF4CAF50) else Color(0xFFFF6B6B),
                            textAlign = TextAlign.Center
                        )
                        if (quizState == QuizState.WRONG && livesLeft > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "💔 $livesLeft ${if (livesLeft == 1) "life" else "lives"} remaining!",
                                fontSize = 12.sp,
                                color = Color(0xFFFF6B6B),
                                textAlign = TextAlign.Center
                            )
                        }
                        if (quizState == QuizState.WRONG) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Correct: ${currentQuestion.correctAnswer}",
                                fontSize = 13.sp,
                                color = Color(0xFF4CAF50),
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val isLastQuestion = currentQuestionIndex == totalQuestions - 1

            when (quizState) {
                QuizState.ANSWERING -> {
                    Button(
                        onClick = {
                            if (selectedAnswer != null) {
                                if (selectedAnswer == currentQuestion.correctAnswer) {
                                    score++
                                    val earned = XPManager.XP_PER_CORRECT
                                    sessionXP += earned
                                    totalXP = XPManager.addXP(context, earned)
                                    xpPopupText = "+${earned} XP ⚡"
                                    showXPPopup = true
                                    quizState = QuizState.CORRECT
                                } else {
                                    wrongCount++
                                    livesLeft--
                                    quizState = if (livesLeft <= 0) QuizState.FINISHED
                                    else QuizState.WRONG
                                }
                            }
                        },
                        enabled = selectedAnswer != null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700),
                            disabledContainerColor = Color(0x44FFD700)
                        ),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Text("Submit Answer", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1B2A))
                    }
                }

                QuizState.CORRECT, QuizState.WRONG -> {
                    Button(
                        onClick = {
                            if (isLastQuestion) quizState = QuizState.FINISHED
                            else {
                                currentQuestionIndex++
                                selectedAnswer = null
                                quizState = QuizState.ANSWERING
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) {
                        Text(
                            text = if (isLastQuestion) "See Results 🏆" else "Next Question →",
                            fontSize = 16.sp, fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1B2A)
                        )
                    }
                }

                QuizState.FINISHED -> { /* handled above */ }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ── Result Screen ─────────────────────────────────────────────────
@Composable
fun QuizResultScreen(
    score: Int,
    total: Int,
    wrongCount: Int,
    sessionXP: Int,
    totalXP: Int,
    highScore: Int,
    isNewHighScore: Boolean,
    onRestart: () -> Unit
) {
    val percentage = (score.toFloat() / total * 100).toInt()
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
                    colors = listOf(Color(0xFF0D1B2A), Color(0xFF1B2A3B), Color(0xFF243447))
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
            // ── New High Score celebration ────────────────────────
            if (isNewHighScore) {
                Text(
                    text = "🎉 NEW HIGH SCORE! 🎉",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Text(text = emoji, fontSize = 72.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message, fontSize = 24.sp, fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700), textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── High Score banner ─────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isNewHighScore) Color(0x66FFD700) else Color(0x33FFD700)
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isNewHighScore) "🏆 New Best Score!" else "🏆 Best Score",
                            fontSize = 13.sp,
                            color = Color(0xFFDEB887)
                        )
                        Text(
                            text = "$highScore / $total",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "This attempt",
                            fontSize = 13.sp,
                            color = Color(0xFFDEB887)
                        )
                        Text(
                            text = "$score / $total",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isNewHighScore) Color(0xFF4CAF50) else Color(0xFFFFFFFF)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── XP earned banner ──────────────────────────────────
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

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x44FFD700)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("$score / $total", fontSize = 48.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700))
                    Text("Score", fontSize = 14.sp, color = Color(0xFFDEB887))
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.CheckCircle, null, tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$score", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50))
                            Text("Correct", fontSize = 12.sp, color = Color(0xFFDEB887))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Close, null, tint = Color(0xFFFF6B6B),
                                modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$wrongCount", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6B6B))
                            Text("Wrong", fontSize = 12.sp, color = Color(0xFFDEB887))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.EmojiEvents, null, tint = Color(0xFFFFD700),
                                modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("$percentage%", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700))
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
                Icon(Icons.Filled.Refresh, "Restart", tint = Color(0xFF0D1B2A),
                    modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Play Again", fontSize = 17.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D1B2A))
            }
        }
    }
}