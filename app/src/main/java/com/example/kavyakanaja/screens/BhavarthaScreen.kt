//package com.example.kavyakanaja.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.ClickableText
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import com.example.kavyakanaja.PoemRepository
//
//enum class BhavarthaLanguage { KANNADA, ENGLISH }
//
//@Composable
//fun BhavarthaScreen() {
//    val context = LocalContext.current
//    val poems = remember { PoemRepository.loadPoems(context) }
//    var selectedIndex by remember { mutableIntStateOf(0) }
//    var language by remember { mutableStateOf(BhavarthaLanguage.KANNADA) }
//    val selectedPoem = poems[selectedIndex]
//
//    var popupWord by remember { mutableStateOf("") }
//    var popupMeaning by remember { mutableStateOf("") }
//    var showPopup by remember { mutableStateOf(false) }
//
//    // ── Search state ─────────────────────────────────────────────────
//    var searchQuery by remember { mutableStateOf("") }
//    val focusManager = LocalFocusManager.current
//
//    val filteredPoems = remember(searchQuery) {
//        if (searchQuery.isBlank()) poems
//        else poems.filter { poem ->
//            poem.title.contains(searchQuery, ignoreCase = true) ||
//                    poem.poet.contains(searchQuery, ignoreCase = true) ||
//                    poem.poetEnglish.contains(searchQuery, ignoreCase = true) ||
//                    poem.verse.contains(searchQuery, ignoreCase = true) ||
//                    poem.meaning.contains(searchQuery, ignoreCase = true)
//        }
//    }
//    // ─────────────────────────────────────────────────────────────────
//
//    if (showPopup) {
//        Dialog(onDismissRequest = { showPopup = false }) {
//            Card(
//                shape = RoundedCornerShape(20.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3A1A)),
//                elevation = CardDefaults.cardElevation(12.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(28.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(text = "📖", fontSize = 36.sp)
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Text(
//                        text = popupWord,
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFFD700),
//                        textAlign = TextAlign.Center
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    HorizontalDivider(color = Color(0x66FFD700))
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = popupMeaning,
//                        fontSize = 16.sp,
//                        color = Color(0xFFFFFFFF),
//                        textAlign = TextAlign.Center
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(
//                        onClick = { showPopup = false },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color(0xFFFFD700)
//                        ),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Text(
//                            text = "Close",
//                            color = Color(0xFF1A3A1A),
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//            }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF0A1A0A),
//                        Color(0xFF1A3A1A),
//                        Color(0xFF2D5A27)
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
//                text = "ಭಾವಾರ್ಥ",
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFFFFD700),
//                textAlign = TextAlign.Center
//            )
//            Text(
//                text = "Word Meanings & Explanation",
//                fontSize = 13.sp,
//                color = Color(0xFFDEB887),
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Language Toggle
//            Row(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(50))
//                    .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(50))
//                    .background(Color(0x22FFD700)),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                val kannadaSelected = language == BhavarthaLanguage.KANNADA
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(if (kannadaSelected) Color(0xFFFFD700) else Color.Transparent)
//                        .clickable { language = BhavarthaLanguage.KANNADA }
//                        .padding(horizontal = 24.dp, vertical = 10.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "🇮🇳 ಕನ್ನಡ",
//                        fontSize = 14.sp,
//                        fontWeight = if (kannadaSelected) FontWeight.Bold else FontWeight.Normal,
//                        color = if (kannadaSelected) Color(0xFF1A3A1A) else Color(0xFFFFD700)
//                    )
//                }
//
//                val englishSelected = language == BhavarthaLanguage.ENGLISH
//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(50))
//                        .background(if (englishSelected) Color(0xFFFFD700) else Color.Transparent)
//                        .clickable { language = BhavarthaLanguage.ENGLISH }
//                        .padding(horizontal = 24.dp, vertical = 10.dp),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "🇬🇧 English",
//                        fontSize = 14.sp,
//                        fontWeight = if (englishSelected) FontWeight.Bold else FontWeight.Normal,
//                        color = if (englishSelected) Color(0xFF1A3A1A) else Color(0xFFFFD700)
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ── VERSE CARD ───────────────────────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x44FFD700)),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Column(
//                    modifier = Modifier.padding(20.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = selectedPoem.title,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFFD700),
//                        textAlign = TextAlign.Center
//                    )
//                    Text(
//                        text = if (language == BhavarthaLanguage.KANNADA)
//                            "— ${selectedPoem.poet}" else "— ${selectedPoem.poetEnglish}",
//                        fontSize = 13.sp,
//                        color = Color(0xFFDEB887),
//                        fontStyle = FontStyle.Italic
//                    )
//                    Text(
//                        text = selectedPoem.era,
//                        fontSize = 11.sp,
//                        color = Color(0xFFDEB887).copy(alpha = 0.7f),
//                        fontStyle = FontStyle.Italic
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//                    HorizontalDivider(color = Color(0x44FFD700))
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    if (language == BhavarthaLanguage.KANNADA) {
//                        val difficultWordsList = selectedPoem.difficultWords
//                        val verseWords = selectedPoem.verse.split(" ")
//
//                        val annotatedVerse = buildAnnotatedString {
//                            verseWords.forEachIndexed { index, word ->
//                                val cleanWord = word
//                                    .replace(",", "").replace(".", "")
//                                    .replace("—", "").replace("!", "")
//                                    .replace("?", "").replace(";", "")
//                                    .replace(":", "").trim()
//
//                                val matchedWord = difficultWordsList.find { dw ->
//                                    dw.word.trim() == cleanWord.trim()
//                                }
//
//                                if (matchedWord != null) {
//                                    pushStringAnnotation(
//                                        tag = "DIFFICULT",
//                                        annotation = matchedWord.word
//                                    )
//                                    withStyle(
//                                        SpanStyle(
//                                            color = Color(0xFFFFD700),
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 17.sp,
//                                            fontStyle = FontStyle.Italic,
//                                            background = Color(0x22FFD700)
//                                        )
//                                    ) {
//                                        append(if (index < verseWords.size - 1) "$word " else word)
//                                    }
//                                    pop()
//                                } else {
//                                    withStyle(
//                                        SpanStyle(
//                                            color = Color(0xFFFFFAF0),
//                                            fontSize = 16.sp,
//                                            fontStyle = FontStyle.Italic
//                                        )
//                                    ) {
//                                        append(if (index < verseWords.size - 1) "$word " else word)
//                                    }
//                                }
//                            }
//                        }
//
//                        ClickableText(
//                            text = annotatedVerse,
//                            style = TextStyle(
//                                textAlign = TextAlign.Center,
//                                lineHeight = 30.sp
//                            ),
//                            onClick = { offset ->
//                                val annotations = annotatedVerse.getStringAnnotations(
//                                    tag = "DIFFICULT",
//                                    start = offset,
//                                    end = offset
//                                )
//                                if (annotations.isNotEmpty()) {
//                                    val tappedWord = annotations.first().item
//                                    val wordObj = selectedPoem.difficultWords.find {
//                                        it.word.trim() == tappedWord.trim()
//                                    }
//                                    if (wordObj != null) {
//                                        popupWord = wordObj.word
//                                        popupMeaning = wordObj.meaning
//                                        showPopup = true
//                                    }
//                                }
//                            }
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            text = "💡 Tap gold words to see meaning",
//                            fontSize = 11.sp,
//                            color = Color(0xFFDEB887),
//                            fontStyle = FontStyle.Italic
//                        )
//                    } else {
//                        Text(
//                            text = selectedPoem.meaning,
//                            fontSize = 16.sp,
//                            color = Color(0xFFFFFAF0),
//                            textAlign = TextAlign.Center,
//                            lineHeight = 28.sp,
//                            fontStyle = FontStyle.Italic
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ── DIFFICULT WORDS ──────────────────────────────────────
//            Text(
//                text = if (language == BhavarthaLanguage.KANNADA)
//                    "📚 ಕಷ್ಟದ ಪದಗಳು" else "📚 Difficult Words",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color(0xFFFFD700)
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//
//            selectedPoem.difficultWords.forEach { word ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 4.dp),
//                    colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
//                    shape = RoundedCornerShape(10.dp)
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Text(
//                            text = word.word,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFFFFD700)
//                        )
//                        Text(
//                            text = word.meaning,
//                            fontSize = 14.sp,
//                            color = Color(0xFFFFFFFF)
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // ── BHAVARTHA ────────────────────────────────────────────
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Column(modifier = Modifier.padding(20.dp)) {
//                    Text(
//                        text = if (language == BhavarthaLanguage.KANNADA)
//                            "💡 ಭಾವಾರ್ಥ" else "💡 Bhavartha",
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFFFFD700)
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = selectedPoem.bhavartha,
//                        fontSize = 15.sp,
//                        color = Color(0xFFFFFFFF),
//                        lineHeight = 24.sp
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // ── POEM LIST WITH SEARCH ────────────────────────────────
//            HorizontalDivider(color = Color(0x44FFD700))
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                text = if (language == BhavarthaLanguage.KANNADA)
//                    "ಕವನ ಆಯ್ಕೆ ಮಾಡಿ" else "Select a Poem",
//                fontSize = 14.sp,
//                color = Color(0xFFDEB887),
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // ── Search Bar ───────────────────────────────────────────
//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = { searchQuery = it },
//                modifier = Modifier.fillMaxWidth(),
//                placeholder = {
//                    Text(
//                        text = if (language == BhavarthaLanguage.KANNADA)
//                            "ಕವನ ಅಥವಾ ಕವಿ ಹೆಸರು ಹುಡುಕಿ..." else "Search poem or poet...",
//                        color = Color(0x88DEB887),
//                        fontSize = 14.sp
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Filled.Search,
//                        contentDescription = "Search",
//                        tint = Color(0xFFFFD700)
//                    )
//                },
//                trailingIcon = {
//                    if (searchQuery.isNotEmpty()) {
//                        IconButton(onClick = {
//                            searchQuery = ""
//                            focusManager.clearFocus()
//                        }) {
//                            Icon(
//                                imageVector = Icons.Filled.Close,
//                                contentDescription = "Clear",
//                                tint = Color(0xFFDEB887)
//                            )
//                        }
//                    }
//                },
//                singleLine = true,
//                shape = RoundedCornerShape(14.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFFFFD700),
//                    unfocusedBorderColor = Color(0x66FFD700),
//                    focusedContainerColor = Color(0x22FFD700),
//                    unfocusedContainerColor = Color(0x11FFD700),
//                    cursorColor = Color(0xFFFFD700),
//                    focusedTextColor = Color(0xFFFFFFFF),
//                    unfocusedTextColor = Color(0xFFFFFFFF)
//                ),
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
//            )
//            // ─────────────────────────────────────────────────────────
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Results count hint when searching
//            if (searchQuery.isNotBlank()) {
//                Text(
//                    text = "${filteredPoems.size} result${if (filteredPoems.size != 1) "s" else ""} found",
//                    fontSize = 12.sp,
//                    color = Color(0xFFDEB887),
//                    modifier = Modifier.align(Alignment.Start)
//                )
//                Spacer(modifier = Modifier.height(6.dp))
//            }
//
//            if (filteredPoems.isEmpty()) {
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    text = "🔍 No poems found for \"$searchQuery\"",
//                    fontSize = 14.sp,
//                    color = Color(0xFFDEB887),
//                    textAlign = TextAlign.Center
//                )
//            } else {
//                filteredPoems.forEach { poem ->
//                    val actualIndex = poems.indexOf(poem)
//                    Card(
//                        onClick = {
//                            selectedIndex = actualIndex
//                            searchQuery = ""
//                            focusManager.clearFocus()
//                            showPopup = false
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = if (selectedIndex == actualIndex)
//                                Color(0xFFFFD700) else Color(0x33FFFFFF)
//                        ),
//                        shape = RoundedCornerShape(10.dp)
//                    ) {
//                        Text(
//                            text = if (language == BhavarthaLanguage.KANNADA)
//                                "${poem.title} — ${poem.poet}"
//                            else
//                                "${poem.title} — ${poem.poetEnglish}",
//                            modifier = Modifier.padding(12.dp),
//                            fontSize = 13.sp,
//                            color = if (selectedIndex == actualIndex)
//                                Color(0xFF1A3A1A) else Color(0xFFFFFFFF),
//                            fontWeight = if (selectedIndex == actualIndex)
//                                FontWeight.Bold else FontWeight.Normal
//                        )
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//        }
//    }
//}









package com.example.kavyakanaja.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.kavyakanaja.PoemRepository

enum class BhavarthaLanguage { KANNADA, ENGLISH }

@Composable
fun BhavarthaScreen() {
    val context = LocalContext.current
    val poems = remember { PoemRepository.loadPoems(context) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var language by remember { mutableStateOf(BhavarthaLanguage.KANNADA) }
    val selectedPoem = poems[selectedIndex]

    var popupWord by remember { mutableStateOf("") }
    var popupMeaning by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }

    // ── Search state ─────────────────────────────────────────────────
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val filteredPoems = remember(searchQuery) {
        if (searchQuery.isBlank()) poems
        else poems.filter { poem ->
            poem.title.contains(searchQuery, ignoreCase = true) ||
                    poem.poet.contains(searchQuery, ignoreCase = true) ||
                    poem.poetEnglish.contains(searchQuery, ignoreCase = true) ||
                    poem.verse.contains(searchQuery, ignoreCase = true) ||
                    poem.meaning.contains(searchQuery, ignoreCase = true)
        }
    }
    // ─────────────────────────────────────────────────────────────────

    if (showPopup) {
        Dialog(onDismissRequest = { showPopup = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3A1A)),
                elevation = CardDefaults.cardElevation(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "📖", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = popupWord,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0x66FFD700))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = popupMeaning,
                        fontSize = 16.sp,
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showPopup = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Close",
                            color = Color(0xFF1A3A1A),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A1A0A),
                        Color(0xFF1A3A1A),
                        Color(0xFF2D5A27)
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
                text = "ಭಾವಾರ್ಥ",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Word Meanings & Explanation",
                fontSize = 13.sp,
                color = Color(0xFFDEB887),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Language Toggle + Random Button Row ──────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Language Toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(50))
                        .background(Color(0x22FFD700)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val kannadaSelected = language == BhavarthaLanguage.KANNADA
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (kannadaSelected) Color(0xFFFFD700) else Color.Transparent)
                            .clickable { language = BhavarthaLanguage.KANNADA }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🇮🇳 ಕನ್ನಡ",
                            fontSize = 13.sp,
                            fontWeight = if (kannadaSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (kannadaSelected) Color(0xFF1A3A1A) else Color(0xFFFFD700)
                        )
                    }

                    val englishSelected = language == BhavarthaLanguage.ENGLISH
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (englishSelected) Color(0xFFFFD700) else Color.Transparent)
                            .clickable { language = BhavarthaLanguage.ENGLISH }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🇬🇧 English",
                            fontSize = 13.sp,
                            fontWeight = if (englishSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (englishSelected) Color(0xFF1A3A1A) else Color(0xFFFFD700)
                        )
                    }
                }

                // ── Random Poem Button ────────────────────────────────
                Button(
                    onClick = {
                        val randomIndex = (poems.indices - selectedIndex).random()
                        selectedIndex = randomIndex
                        searchQuery = ""
                        focusManager.clearFocus()
                        showPopup = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD700)
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shuffle,
                        contentDescription = "Random Poem",
                        tint = Color(0xFF1A3A1A),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Random",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A3A1A)
                    )
                }
            }
            // ─────────────────────────────────────────────────────────

            Spacer(modifier = Modifier.height(16.dp))

            // ── VERSE CARD ───────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x44FFD700)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = selectedPoem.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = if (language == BhavarthaLanguage.KANNADA)
                            "— ${selectedPoem.poet}" else "— ${selectedPoem.poetEnglish}",
                        fontSize = 13.sp,
                        color = Color(0xFFDEB887),
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = selectedPoem.era,
                        fontSize = 11.sp,
                        color = Color(0xFFDEB887).copy(alpha = 0.7f),
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0x44FFD700))
                    Spacer(modifier = Modifier.height(12.dp))

                    if (language == BhavarthaLanguage.KANNADA) {
                        val difficultWordsList = selectedPoem.difficultWords
                        val verseWords = selectedPoem.verse.split(" ")

                        val annotatedVerse = buildAnnotatedString {
                            verseWords.forEachIndexed { index, word ->
                                val cleanWord = word
                                    .replace(",", "").replace(".", "")
                                    .replace("—", "").replace("!", "")
                                    .replace("?", "").replace(";", "")
                                    .replace(":", "").trim()

                                val matchedWord = difficultWordsList.find { dw ->
                                    dw.word.trim() == cleanWord.trim()
                                }

                                if (matchedWord != null) {
                                    pushStringAnnotation(
                                        tag = "DIFFICULT",
                                        annotation = matchedWord.word
                                    )
                                    withStyle(
                                        SpanStyle(
                                            color = Color(0xFFFFD700),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp,
                                            fontStyle = FontStyle.Italic,
                                            background = Color(0x22FFD700)
                                        )
                                    ) {
                                        append(if (index < verseWords.size - 1) "$word " else word)
                                    }
                                    pop()
                                } else {
                                    withStyle(
                                        SpanStyle(
                                            color = Color(0xFFFFFAF0),
                                            fontSize = 16.sp,
                                            fontStyle = FontStyle.Italic
                                        )
                                    ) {
                                        append(if (index < verseWords.size - 1) "$word " else word)
                                    }
                                }
                            }
                        }

                        ClickableText(
                            text = annotatedVerse,
                            style = TextStyle(
                                textAlign = TextAlign.Center,
                                lineHeight = 30.sp
                            ),
                            onClick = { offset ->
                                val annotations = annotatedVerse.getStringAnnotations(
                                    tag = "DIFFICULT",
                                    start = offset,
                                    end = offset
                                )
                                if (annotations.isNotEmpty()) {
                                    val tappedWord = annotations.first().item
                                    val wordObj = selectedPoem.difficultWords.find {
                                        it.word.trim() == tappedWord.trim()
                                    }
                                    if (wordObj != null) {
                                        popupWord = wordObj.word
                                        popupMeaning = wordObj.meaning
                                        showPopup = true
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💡 Tap gold words to see meaning",
                            fontSize = 11.sp,
                            color = Color(0xFFDEB887),
                            fontStyle = FontStyle.Italic
                        )
                    } else {
                        Text(
                            text = selectedPoem.meaning,
                            fontSize = 16.sp,
                            color = Color(0xFFFFFAF0),
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── DIFFICULT WORDS ──────────────────────────────────────
            Text(
                text = if (language == BhavarthaLanguage.KANNADA)
                    "📚 ಕಷ್ಟದ ಪದಗಳು" else "📚 Difficult Words",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700)
            )
            Spacer(modifier = Modifier.height(8.dp))

            selectedPoem.difficultWords.forEach { word ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x33FFFFFF)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = word.word,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                        Text(
                            text = word.meaning,
                            fontSize = 14.sp,
                            color = Color(0xFFFFFFFF)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── BHAVARTHA ────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0x22FFFFFF)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = if (language == BhavarthaLanguage.KANNADA)
                            "💡 ಭಾವಾರ್ಥ" else "💡 Bhavartha",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = selectedPoem.bhavartha,
                        fontSize = 15.sp,
                        color = Color(0xFFFFFFFF),
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── POEM LIST WITH SEARCH ────────────────────────────────
            HorizontalDivider(color = Color(0x44FFD700))
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (language == BhavarthaLanguage.KANNADA)
                    "ಕವನ ಆಯ್ಕೆ ಮಾಡಿ" else "Select a Poem",
                fontSize = 14.sp,
                color = Color(0xFFDEB887),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            // ── Search Bar ───────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = if (language == BhavarthaLanguage.KANNADA)
                            "ಕವನ ಅಥವಾ ಕವಿ ಹೆಸರು ಹುಡುಕಿ..." else "Search poem or poet...",
                        color = Color(0x88DEB887),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color(0xFFFFD700)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            focusManager.clearFocus()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear",
                                tint = Color(0xFFDEB887)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFD700),
                    unfocusedBorderColor = Color(0x66FFD700),
                    focusedContainerColor = Color(0x22FFD700),
                    unfocusedContainerColor = Color(0x11FFD700),
                    cursorColor = Color(0xFFFFD700),
                    focusedTextColor = Color(0xFFFFFFFF),
                    unfocusedTextColor = Color(0xFFFFFFFF)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
            )
            // ─────────────────────────────────────────────────────────

            Spacer(modifier = Modifier.height(8.dp))

            // Results count hint when searching
            if (searchQuery.isNotBlank()) {
                Text(
                    text = "${filteredPoems.size} result${if (filteredPoems.size != 1) "s" else ""} found",
                    fontSize = 12.sp,
                    color = Color(0xFFDEB887),
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (filteredPoems.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "🔍 No poems found for \"$searchQuery\"",
                    fontSize = 14.sp,
                    color = Color(0xFFDEB887),
                    textAlign = TextAlign.Center
                )
            } else {
                filteredPoems.forEach { poem ->
                    val actualIndex = poems.indexOf(poem)
                    Card(
                        onClick = {
                            selectedIndex = actualIndex
                            searchQuery = ""
                            focusManager.clearFocus()
                            showPopup = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedIndex == actualIndex)
                                Color(0xFFFFD700) else Color(0x33FFFFFF)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (language == BhavarthaLanguage.KANNADA)
                                "${poem.title} — ${poem.poet}"
                            else
                                "${poem.title} — ${poem.poetEnglish}",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 13.sp,
                            color = if (selectedIndex == actualIndex)
                                Color(0xFF1A3A1A) else Color(0xFFFFFFFF),
                            fontWeight = if (selectedIndex == actualIndex)
                                FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}





