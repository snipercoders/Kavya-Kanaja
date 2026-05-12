//package com.example.kavyakanaja.screens
//
//import android.speech.tts.TextToSpeech
//import android.speech.tts.UtteranceProgressListener
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Pause
//import androidx.compose.material.icons.filled.PlayArrow
//import androidx.compose.material.icons.filled.SkipNext
//import androidx.compose.material.icons.filled.SkipPrevious
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.kavyakanaja.PoemRepository
//import java.util.Locale
//
//enum class PlaybackLanguage { KANNADA, ENGLISH }
//
//@Composable
//fun ListenLearnScreen() {
//    val context = LocalContext.current
//    val poems = remember { PoemRepository.loadPoems(context) }
//    var currentIndex by remember { mutableIntStateOf(0) }
//    var isPlaying by remember { mutableStateOf(false) }
//    var progress by remember { mutableFloatStateOf(0f) }
//    var currentWordIndex by remember { mutableIntStateOf(-1) }
//    var ttsReady by remember { mutableStateOf(false) }
//    var ttsError by remember { mutableStateOf(false) }
//    var playbackLanguage by remember { mutableStateOf(PlaybackLanguage.KANNADA) }
//    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
//
//    val currentPoem = poems[currentIndex]
//
//    val activeText = when (playbackLanguage) {
//        PlaybackLanguage.KANNADA -> currentPoem.verse
//        PlaybackLanguage.ENGLISH -> currentPoem.meaning
//    }
//    val meaningText = when (playbackLanguage) {
//        PlaybackLanguage.KANNADA -> currentPoem.meaning
//        PlaybackLanguage.ENGLISH -> currentPoem.verse
//    }
//    val meaningLabel = when (playbackLanguage) {
//        PlaybackLanguage.KANNADA -> "📖 Meaning (English)"
//        PlaybackLanguage.ENGLISH -> "📖 ಅರ್ಥ (ಕನ್ನಡ)"
//    }
//
//    val words = activeText.split(" ")
//    val wordStartOffsets: List<Int> = remember(activeText) {
//        val offsets = mutableListOf<Int>()
//        var pos = 0
//        words.forEach { word ->
//            offsets.add(pos)
//            pos += word.length + 1
//        }
//        offsets
//    }
//
//    // Initialize TTS once
//    DisposableEffect(context) {
//        val textToSpeech = TextToSpeech(context) { status ->
//            if (status == TextToSpeech.SUCCESS) {
//                ttsReady = true; ttsError = false
//            } else {
//                ttsReady = false; ttsError = true
//            }
//        }
//        tts = textToSpeech
//        onDispose { textToSpeech.stop(); textToSpeech.shutdown(); tts = null }
//    }
//
//    LaunchedEffect(ttsReady, playbackLanguage) {
//        if (ttsReady) {
//            val locale = when (playbackLanguage) {
//                PlaybackLanguage.KANNADA -> Locale("kn", "IN")
//                PlaybackLanguage.ENGLISH -> Locale.ENGLISH
//            }
//            val result = tts?.setLanguage(locale)
//            if (result == TextToSpeech.LANG_MISSING_DATA ||
//                result == TextToSpeech.LANG_NOT_SUPPORTED
//            ) tts?.setLanguage(Locale.getDefault())
//        }
//    }
//
//    LaunchedEffect(currentIndex) {
//        tts?.stop(); isPlaying = false; progress = 0f; currentWordIndex = -1
//    }
//    LaunchedEffect(playbackLanguage) {
//        tts?.stop(); isPlaying = false; progress = 0f; currentWordIndex = -1
//    }
//
//    // ── UI ────────────────────────────────────────────────────────────
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF1A0A2E),
//                        Color(0xFF2C1A5C),
//                        Color(0xFF3D2B6B)
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
//                text = "ಕೇಳು & ಕಲಿ",
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFFFFD700),
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = "Listen & Learn",
//                fontSize = 13.sp,
//                color = Color(0xFFDEB887),
//                textAlign = TextAlign.Center
//            )
//
//            if (ttsError) {
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = "⚠️ Audio unavailable on this device",
//                    fontSize = 12.sp,
//                    color = Color(0xFFFF6B6B),
//                    textAlign = TextAlign.Center
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ── Poem Counter Badge ────────────────────────────────────
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                // Left dot indicators (max 5 shown)
//                val totalPoems = poems.size
//                val visibleDots = minOf(totalPoems, 5)
//                val dotOffset = if (currentIndex >= 3 && totalPoems > 5)
//                    (currentIndex - 2).coerceAtMost(totalPoems - visibleDots) else 0
//
//                (dotOffset until dotOffset + visibleDots).forEach { i ->
//                    Box(
//                        modifier = Modifier
//                            .padding(horizontal = 3.dp)
//                            .size(if (i == currentIndex) 10.dp else 6.dp)
//                            .clip(CircleShape)
//                            .background(
//                                if (i == currentIndex) Color(0xFFFFD700)
//                                else Color(0x55FFD700)
//                            )
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(10.dp))
//
//                // Counter pill: "3 / 20"
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(Color(0x33FFD700))
//                        .border(1.dp, Color(0x88FFD700), RoundedCornerShape(50))
//                        .padding(horizontal = 14.dp, vertical = 5.dp)
//                ) {
//                    Text(
//                        text = "${currentIndex + 1} / $totalPoems",
//                        fontSize = 13.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFFD700)
//                    )
//                }
//            }
//            // ─────────────────────────────────────────────────────────
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ── Language Toggle ───────────────────────────────────────
//            Text(
//                text = "🔊 Play in",
//                fontSize = 13.sp,
//                color = Color(0xFFDEB887),
//                textAlign = TextAlign.Center
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(50))
//                    .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(50))
//                    .background(Color(0x22FFD700)),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                val kannadaSelected = playbackLanguage == PlaybackLanguage.KANNADA
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(if (kannadaSelected) Color(0xFFFFD700) else Color.Transparent)
//                        .clickable { playbackLanguage = PlaybackLanguage.KANNADA }
//                        .padding(horizontal = 24.dp, vertical = 10.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "🇮🇳 ಕನ್ನಡ",
//                        fontSize = 14.sp,
//                        fontWeight = if (kannadaSelected) FontWeight.Bold else FontWeight.Normal,
//                        color = if (kannadaSelected) Color(0xFF1A0A2E) else Color(0xFFFFD700)
//                    )
//                }
//
//                val englishSelected = playbackLanguage == PlaybackLanguage.ENGLISH
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(if (englishSelected) Color(0xFFFFD700) else Color.Transparent)
//                        .clickable { playbackLanguage = PlaybackLanguage.ENGLISH }
//                        .padding(horizontal = 24.dp, vertical = 10.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "🇬🇧 English",
//                        fontSize = 14.sp,
//                        fontWeight = if (englishSelected) FontWeight.Bold else FontWeight.Normal,
//                        color = if (englishSelected) Color(0xFF1A0A2E) else Color(0xFFFFD700)
//                    )
//                }
//            }
//            // ─────────────────────────────────────────────────────────
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ── Poem Card ─────────────────────────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
//                shape = RoundedCornerShape(20.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(24.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = if (isPlaying) "🎵" else "🎶",
//                        fontSize = 48.sp,
//                        textAlign = TextAlign.Center
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    Text(
//                        text = currentPoem.title,
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFFD700),
//                        textAlign = TextAlign.Center
//                    )
//                    Text(
//                        text = "— ${currentPoem.poet}",
//                        fontSize = 14.sp,
//                        color = Color(0xFFDEB887),
//                        fontStyle = FontStyle.Italic
//                    )
//
//                    // ── Era Label ─────────────────────────────────────
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Box(
//                        modifier = Modifier
//                            .clip(RoundedCornerShape(50))
//                            .background(Color(0x22FFD700))
//                            .border(1.dp, Color(0x55FFD700), RoundedCornerShape(50))
//                            .padding(horizontal = 12.dp, vertical = 4.dp)
//                    ) {
//                        Text(
//                            text = "🕰 ${currentPoem.era}",
//                            fontSize = 11.sp,
//                            color = Color(0xFFDEB887),
//                            fontStyle = FontStyle.Italic,
//                            textAlign = TextAlign.Center
//                        )
//                    }
//                    // ─────────────────────────────────────────────────
//
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Text(
//                        text = if (playbackLanguage == PlaybackLanguage.KANNADA)
//                            "ಕನ್ನಡ ಪಾಠ" else "English Reading",
//                        fontSize = 11.sp,
//                        color = Color(0xFFFFD700).copy(alpha = 0.7f),
//                        fontStyle = FontStyle.Italic
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Word-by-word highlighted AnnotatedString
//                    val annotatedVerse = buildAnnotatedString {
//                        words.forEachIndexed { index, word ->
//                            val isCurrentWord = isPlaying && index == currentWordIndex
//                            val isPastWord    = isPlaying && index < currentWordIndex
//                            val color = when {
//                                isCurrentWord -> Color(0xFFFFD700)
//                                isPastWord    -> Color(0xFFDEB887)
//                                else          -> Color(0xFFFFFAF0)
//                            }
//                            withStyle(
//                                SpanStyle(
//                                    color = color,
//                                    fontWeight = if (isCurrentWord) FontWeight.Bold else FontWeight.Normal,
//                                    fontStyle = FontStyle.Italic,
//                                    fontSize = 16.sp
//                                )
//                            ) {
//                                append(if (index < words.size - 1) "$word " else word)
//                            }
//                        }
//                    }
//                    Text(
//                        text = annotatedVerse,
//                        textAlign = TextAlign.Center,
//                        lineHeight = 28.sp,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            LinearProgressIndicator(
//                progress = { progress },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(6.dp),
//                color = Color(0xFFFFD700),
//                trackColor = Color(0x44FFD700)
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ── Playback Controls ─────────────────────────────────────
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(24.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(
//                    onClick = {
//                        if (currentIndex > 0) {
//                            tts?.stop()
//                            currentIndex--
//                            isPlaying = false
//                            progress = 0f
//                            currentWordIndex = -1
//                        }
//                    }
//                ) {
//                    Icon(
//                        Icons.Filled.SkipPrevious,
//                        contentDescription = "Previous",
//                        tint = if (currentIndex > 0) Color(0xFFFFD700) else Color(0x55FFD700),
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
//
//                Button(
//                    onClick = {
//                        if (!ttsReady) return@Button
//                        if (isPlaying) {
//                            tts?.stop()
//                            isPlaying = false
//                            currentWordIndex = -1
//                            progress = 0f
//                        } else {
//                            val textToSpeak = when (playbackLanguage) {
//                                PlaybackLanguage.KANNADA -> currentPoem.verse
//                                PlaybackLanguage.ENGLISH -> currentPoem.meaning
//                            }
//                            val utteranceId = "poem_${currentIndex}_${playbackLanguage.name}"
//                            val capturedOffsets = wordStartOffsets
//                            val totalLen = textToSpeak.length.toFloat()
//
//                            tts?.setOnUtteranceProgressListener(object :
//                                UtteranceProgressListener() {
//                                override fun onStart(utteranceId: String?) {
//                                    isPlaying = true; progress = 0f; currentWordIndex = 0
//                                }
//                                override fun onRangeStart(
//                                    utteranceId: String?,
//                                    start: Int,
//                                    end: Int,
//                                    frame: Int
//                                ) {
//                                    val idx = capturedOffsets.indexOfLast { it <= start }
//                                    if (idx >= 0) currentWordIndex = idx
//                                    progress = (end.toFloat() / totalLen).coerceIn(0f, 1f)
//                                }
//                                override fun onDone(utteranceId: String?) {
//                                    isPlaying = false; progress = 1f; currentWordIndex = -1
//                                }
//                                @Deprecated("Deprecated in Java")
//                                override fun onError(utteranceId: String?) {
//                                    isPlaying = false; currentWordIndex = -1
//                                }
//                            })
//
//                            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
//                        }
//                    },
//                    shape = CircleShape,
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (ttsReady) Color(0xFFFFD700) else Color(0xFF888888)
//                    ),
//                    modifier = Modifier.size(72.dp)
//                ) {
//                    Icon(
//                        if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
//                        contentDescription = "Play/Pause",
//                        tint = Color(0xFF1A0A2E),
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
//
//                IconButton(
//                    onClick = {
//                        if (currentIndex < poems.size - 1) {
//                            tts?.stop()
//                            currentIndex++
//                            isPlaying = false
//                            progress = 0f
//                            currentWordIndex = -1
//                        }
//                    }
//                ) {
//                    Icon(
//                        Icons.Filled.SkipNext,
//                        contentDescription = "Next",
//                        tint = if (currentIndex < poems.size - 1) Color(0xFFFFD700) else Color(0x55FFD700),
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                text = when {
//                    ttsError       -> "⚠️ TTS not available — visual mode only"
//                    isPlaying      -> "▶ Reciting in ${if (playbackLanguage == PlaybackLanguage.KANNADA) "ಕನ್ನಡ" else "English"} — ${currentPoem.title}"
//                    progress >= 1f -> "✅ Done"
//                    progress > 0f  -> "⏸ Paused"
//                    else           -> "Tap play to begin recitation"
//                },
//                fontSize = 13.sp,
//                color = Color(0xFFDEB887),
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ── Meaning Card ──────────────────────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Column(modifier = Modifier.padding(20.dp)) {
//                    Text(
//                        text = meaningLabel,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFFD700)
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = meaningText,
//                        fontSize = 15.sp,
//                        color = Color(0xFFFFFFFF),
//                        lineHeight = 24.sp
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}











package com.example.kavyakanaja.screens

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
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
import java.util.Locale

enum class PlaybackLanguage { KANNADA, ENGLISH }

// ── Playback speed options ────────────────────────────────────────
enum class PlaybackSpeed(val label: String, val rate: Float) {
    SLOW("0.5x", 0.5f),
    NORMAL("1x", 1.0f),
    FAST("1.5x", 1.5f)
}

@Composable
fun ListenLearnScreen() {
    val context = LocalContext.current
    val poems = remember { PoemRepository.loadPoems(context) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var currentWordIndex by remember { mutableIntStateOf(-1) }
    var ttsReady by remember { mutableStateOf(false) }
    var ttsError by remember { mutableStateOf(false) }
    var playbackLanguage by remember { mutableStateOf(PlaybackLanguage.KANNADA) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    // ── Speed state ───────────────────────────────────────────────
    var playbackSpeed by remember { mutableStateOf(PlaybackSpeed.NORMAL) }

    val currentPoem = poems[currentIndex]

    val activeText = when (playbackLanguage) {
        PlaybackLanguage.KANNADA -> currentPoem.verse
        PlaybackLanguage.ENGLISH -> currentPoem.meaning
    }
    val meaningText = when (playbackLanguage) {
        PlaybackLanguage.KANNADA -> currentPoem.meaning
        PlaybackLanguage.ENGLISH -> currentPoem.verse
    }
    val meaningLabel = when (playbackLanguage) {
        PlaybackLanguage.KANNADA -> "📖 Meaning (English)"
        PlaybackLanguage.ENGLISH -> "📖 ಅರ್ಥ (ಕನ್ನಡ)"
    }

    val words = activeText.split(" ")
    val wordStartOffsets: List<Int> = remember(activeText) {
        val offsets = mutableListOf<Int>()
        var pos = 0
        words.forEach { word ->
            offsets.add(pos)
            pos += word.length + 1
        }
        offsets
    }

    // Initialize TTS once
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true; ttsError = false
            } else {
                ttsReady = false; ttsError = true
            }
        }
        tts = textToSpeech
        onDispose { textToSpeech.stop(); textToSpeech.shutdown(); tts = null }
    }

    LaunchedEffect(ttsReady, playbackLanguage) {
        if (ttsReady) {
            val locale = when (playbackLanguage) {
                PlaybackLanguage.KANNADA -> Locale("kn", "IN")
                PlaybackLanguage.ENGLISH -> Locale.ENGLISH
            }
            val result = tts?.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) tts?.setLanguage(Locale.getDefault())
        }
    }

    // ── Apply speed to TTS whenever it changes ────────────────────
    LaunchedEffect(ttsReady, playbackSpeed) {
        if (ttsReady) {
            tts?.setSpeechRate(playbackSpeed.rate)
        }
    }

    LaunchedEffect(currentIndex) {
        tts?.stop(); isPlaying = false; progress = 0f; currentWordIndex = -1
    }
    LaunchedEffect(playbackLanguage) {
        tts?.stop(); isPlaying = false; progress = 0f; currentWordIndex = -1
    }

    // ── UI ────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A0A2E),
                        Color(0xFF2C1A5C),
                        Color(0xFF3D2B6B)
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
                text = "ಕೇಳು & ಕಲಿ",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Listen & Learn",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            if (ttsError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⚠️ Audio unavailable on this device",
                    fontSize = 12.sp,
                    color = Color(0xFFFF6B6B),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Poem Counter Badge ────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                val totalPoems = poems.size
                val visibleDots = minOf(totalPoems, 5)
                val dotOffset = if (currentIndex >= 3 && totalPoems > 5)
                    (currentIndex - 2).coerceAtMost(totalPoems - visibleDots) else 0

                (dotOffset until dotOffset + visibleDots).forEach { i ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (i == currentIndex) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (i == currentIndex) Color(0xFFFFD700)
                                else Color(0x55FFD700)
                            )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0x33FFD700))
                        .border(1.dp, Color(0x88FFD700), RoundedCornerShape(50))
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "${currentIndex + 1} / $totalPoems",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Language Toggle ───────────────────────────────────────
            Text(
                text = "🔊 Play in",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(50))
                    .background(Color(0x22FFD700)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val kannadaSelected = playbackLanguage == PlaybackLanguage.KANNADA
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (kannadaSelected) Color(0xFFFFD700) else Color.Transparent)
                        .clickable { playbackLanguage = PlaybackLanguage.KANNADA }
                        .padding(horizontal = 24.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🇮🇳 ಕನ್ನಡ",
                        fontSize = 14.sp,
                        fontWeight = if (kannadaSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (kannadaSelected) Color(0xFF1A0A2E) else Color(0xFFFFD700)
                    )
                }

                val englishSelected = playbackLanguage == PlaybackLanguage.ENGLISH
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (englishSelected) Color(0xFFFFD700) else Color.Transparent)
                        .clickable { playbackLanguage = PlaybackLanguage.ENGLISH }
                        .padding(horizontal = 24.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🇬🇧 English",
                        fontSize = 14.sp,
                        fontWeight = if (englishSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (englishSelected) Color(0xFF1A0A2E) else Color(0xFFFFD700)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Speed Toggle ──────────────────────────────────────────
            Text(
                text = "⚡ Speed",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(50))
                    .background(Color(0x22FFD700)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PlaybackSpeed.entries.forEach { speed ->
                    val isSelected = playbackSpeed == speed
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (isSelected) Color(0xFFFFD700) else Color.Transparent)
                            .clickable {
                                playbackSpeed = speed
                                // Apply immediately if currently playing
                                if (isPlaying) {
                                    tts?.setSpeechRate(speed.rate)
                                }
                            }
                            .padding(horizontal = 22.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = speed.label,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color(0xFF1A0A2E) else Color(0xFFFFD700)
                        )
                    }
                }
            }
            // ─────────────────────────────────────────────────────────

            Spacer(modifier = Modifier.height(20.dp))

            // ── Poem Card ─────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x33FFD700)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isPlaying) "🎵" else "🎶",
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentPoem.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "— ${currentPoem.poet}",
                        fontSize = 14.sp,
                        color = Color(0xFFDEB887),
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0x22FFD700))
                            .border(1.dp, Color(0x55FFD700), RoundedCornerShape(50))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "🕰 ${currentPoem.era}",
                            fontSize = 11.sp,
                            color = Color(0xFFDEB887),
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (playbackLanguage == PlaybackLanguage.KANNADA)
                            "ಕನ್ನಡ ಪಾಠ" else "English Reading",
                        fontSize = 11.sp,
                        color = Color(0xFFFFD700).copy(alpha = 0.7f),
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    val annotatedVerse = buildAnnotatedString {
                        words.forEachIndexed { index, word ->
                            val isCurrentWord = isPlaying && index == currentWordIndex
                            val isPastWord    = isPlaying && index < currentWordIndex
                            val color = when {
                                isCurrentWord -> Color(0xFFFFD700)
                                isPastWord    -> Color(0xFFDEB887)
                                else          -> Color(0xFFFFFAF0)
                            }
                            withStyle(
                                SpanStyle(
                                    color = color,
                                    fontWeight = if (isCurrentWord) FontWeight.Bold else FontWeight.Normal,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 16.sp
                                )
                            ) {
                                append(if (index < words.size - 1) "$word " else word)
                            }
                        }
                    }
                    Text(
                        text = annotatedVerse,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                color = Color(0xFFFFD700),
                trackColor = Color(0x44FFD700)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Playback Controls ─────────────────────────────────────
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (currentIndex > 0) {
                            tts?.stop()
                            currentIndex--
                            isPlaying = false
                            progress = 0f
                            currentWordIndex = -1
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.SkipPrevious,
                        contentDescription = "Previous",
                        tint = if (currentIndex > 0) Color(0xFFFFD700) else Color(0x55FFD700),
                        modifier = Modifier.size(36.dp)
                    )
                }

                Button(
                    onClick = {
                        if (!ttsReady) return@Button
                        if (isPlaying) {
                            tts?.stop()
                            isPlaying = false
                            currentWordIndex = -1
                            progress = 0f
                        } else {
                            val textToSpeak = when (playbackLanguage) {
                                PlaybackLanguage.KANNADA -> currentPoem.verse
                                PlaybackLanguage.ENGLISH -> currentPoem.meaning
                            }
                            val utteranceId = "poem_${currentIndex}_${playbackLanguage.name}"
                            val capturedOffsets = wordStartOffsets
                            val totalLen = textToSpeak.length.toFloat()

                            // Apply current speed before speaking
                            tts?.setSpeechRate(playbackSpeed.rate)

                            tts?.setOnUtteranceProgressListener(object :
                                UtteranceProgressListener() {
                                override fun onStart(utteranceId: String?) {
                                    isPlaying = true; progress = 0f; currentWordIndex = 0
                                }
                                override fun onRangeStart(
                                    utteranceId: String?,
                                    start: Int,
                                    end: Int,
                                    frame: Int
                                ) {
                                    val idx = capturedOffsets.indexOfLast { it <= start }
                                    if (idx >= 0) currentWordIndex = idx
                                    progress = (end.toFloat() / totalLen).coerceIn(0f, 1f)
                                }
                                override fun onDone(utteranceId: String?) {
                                    isPlaying = false; progress = 1f; currentWordIndex = -1
                                }
                                @Deprecated("Deprecated in Java")
                                override fun onError(utteranceId: String?) {
                                    isPlaying = false; currentWordIndex = -1
                                }
                            })

                            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
                        }
                    },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (ttsReady) Color(0xFFFFD700) else Color(0xFF888888)
                    ),
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = Color(0xFF1A0A2E),
                        modifier = Modifier.size(36.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (currentIndex < poems.size - 1) {
                            tts?.stop()
                            currentIndex++
                            isPlaying = false
                            progress = 0f
                            currentWordIndex = -1
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.SkipNext,
                        contentDescription = "Next",
                        tint = if (currentIndex < poems.size - 1) Color(0xFFFFD700) else Color(0x55FFD700),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = when {
                    ttsError       -> "⚠️ TTS not available — visual mode only"
                    isPlaying      -> "▶ Reciting at ${playbackSpeed.label} in ${if (playbackLanguage == PlaybackLanguage.KANNADA) "ಕನ್ನಡ" else "English"} — ${currentPoem.title}"
                    progress >= 1f -> "✅ Done"
                    progress > 0f  -> "⏸ Paused"
                    else           -> "Tap play to begin recitation"
                },
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Meaning Card ──────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = meaningLabel,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = meaningText,
                        fontSize = 15.sp,
                        color = Color(0xFFFFFFFF),
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}