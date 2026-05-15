# ಕಾವ್ಯ ಕಣಜ — Kavya-Kanaja 📖

> **Poetry Granary** — A Kannada Literary Revival Android App

[![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

---

## 📌 About

**Kavya-Kanaja** (Poetry Granary) is a cultural and literary Android application developed to revive classical Kannada poetry among the younger generation. Karnataka has a rich literary history with poets like **Kuvempu**, **Basavanna**, and **Pampa** — but the old language feels inaccessible to Gen-Z students. This app bridges that gap through daily poems, audio recitation, interactive word meanings, poet biographies, quizzes, and a Duolingo-style gamification system.

> Built as part of the **Android App Development using Gen AI** internship at **MindMatrix**, Bengaluru.
>
> **Student:** Ansh Jamwal (1AH22CS015)
> **College:** ACS College of Engineering, Bangalore
> **Academic Year:** 2025–26

---

## 🎯 Problem Statement

- Classical Kannada poetry is inaccessible to the younger generation
- No engaging digital platform exists for discovering classical Kannada verses
- Students have no interactive way to understand difficult classical Kannada words
- No gamification or daily reminder system to encourage consistent engagement with Kannada literature

---

## ✨ Features

| Screen | Feature |
|---|---|
| 🏠 **Poem of the Day** | Daily rotating poem using `dayOfYear % totalPoems` formula, streak badge, XP badge, daily goals ring, share & favourite |
| 🎧 **Listen & Learn** | TextToSpeech audio recitation with live word-by-word gold highlighting, language toggle (Kannada/English), speed control (0.5x / 1x / 1.5x) |
| 📖 **Bhavartha** | Tap highlighted gold words to see meanings in a popup dialog, search bar, random poem button |
| 🧑‍🎤 **Poet's Corner** | Scrollable biography cards for classical Kannada poets with name, era, and biography |
| 🧠 **Quiz** | Shuffled MCQs from poems JSON, 15-second countdown timer, 3-lives system, +10 XP per correct answer, high score tracker |
| ❤️ **Favourites** | Save and manage favourite poems using SharedPreferences |
| 📊 **Progress** | Streak counter, XP level bar (Beginner → Scholar → Poet → Kavi), daily goals ring, 12 achievement badges, daily reminder with AlarmManager |

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| **Kotlin** | Programming language for all app logic |
| **Jetpack Compose** | Declarative UI for all screens |
| **Local JSON (`poems.json`)** | Stores 15+ classical Kannada poems — no internet needed |
| **Android TextToSpeech API** | Audio recitation with word-by-word highlighting via `UtteranceProgressListener` |
| **SharedPreferences** | Local storage for streak, XP, high scores, favourites, goals, notification prefs |
| **AlarmManager + NotificationManager** | Daily poem reminder notifications at user-chosen time |
| **ClickableText (Compose)** | Interactive word meaning popups in Bhavartha screen |
| **Canvas (Compose)** | Circular daily goals ring drawn with `drawArc` |

---

## 📁 Project Structure

```
KavyaKanaja/
├── app/src/main/
│   ├── java/com/example/kavyakanaja/
│   │   ├── screens/
│   │   │   ├── PoemOfDayScreen.kt
│   │   │   ├── ListenLearnScreen.kt
│   │   │   ├── BhavarthaScreen.kt
│   │   │   ├── PoetCornerScreen.kt
│   │   │   ├── QuizScreen.kt
│   │   │   ├── FavouritesScreen.kt
│   │   │   └── Progressscreen.kt
│   │   ├── MainActivity.kt
│   │   ├── PoemModel.kt
│   │   ├── PoemRepository.kt
│   │   ├── FavouritesManager.kt
│   │   ├── StreakManager.kt
│   │   ├── XPManager.kt
│   │   ├── GoalManager.kt
│   │   └── NotificationHelper.kt
│   ├── assets/
│   │   └── poems.json
│   └── AndroidManifest.xml
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable)
- Android SDK 24+
- Kotlin 1.9+

### Setup

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/KavyaKanaja.git

# 2. Open in Android Studio
File → Open → Select the KavyaKanaja folder

# 3. Build and run
Click the Run button or press Shift+F10
```

### AndroidManifest Permissions Required

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"
    android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.USE_EXACT_ALARM" />
```

---

## 📊 Gamification System

| System | How It Works |
|---|---|
| 🔥 **Streak** | Opens app on consecutive days → streak increments; miss a day → resets to 1 (SharedPreferences + date comparison) |
| ⚡ **XP** | +10 XP per correct quiz answer; levels: Beginner (0) → Scholar (80) → Poet (200) → Kavi (500) |
| 🏆 **High Score** | Best quiz score saved in SharedPreferences; "New High Score!" banner on result screen |
| 🎯 **Daily Goals** | Read poem + complete quiz = 2/2 goals; ring fills on Progress and Poem of Day screens |
| 🏅 **Badges** | 12 achievement badges unlocked by milestones: streaks, XP thresholds, favourites count |

---

## 📱 Screenshots

| Poem of the Day | Listen & Learn | Bhavartha |
|---|---|---|
| ![Poem of Day](screenshots/poem_of_day.png) | ![Listen Learn](screenshots/listen_learn.png) | ![Bhavartha](screenshots/bhavartha.png) |

| Quiz | Progress |
|---|---|
| ![Quiz](screenshots/quiz.png) | ![Progress](screenshots/progress.png) |

> Add screenshots to a `/screenshots` folder in the root of the repository.

---

## 📄 poems.json Structure

```json
{
  "id": 1,
  "title": "ಬಾರೋ ಶಿಶುವೇ",
  "poet": "ಕುವೆಂಪು",
  "poetEnglish": "Kuvempu",
  "era": "Modern Era",
  "verse": "ಬಾರೋ ಶಿಶುವೇ ಬಾರೋ...",
  "meaning": "Come child, come to the world of knowledge...",
  "bhavartha": "In this verse, the poet Kuvempu invites...",
  "difficultWords": [
    { "word": "ಶಿಶುವೇ", "meaning": "Child" },
    { "word": "ಜ್ಞಾನ", "meaning": "Knowledge" }
  ]
}
```

---

## 🔮 Future Enhancements

- [ ] Cloud sync for streaks and XP using Firebase
- [ ] User authentication with Google Sign-In
- [ ] More poems covering all major eras of Kannada literature
- [ ] Offline MP3 audio files for higher quality recitation
- [ ] Social sharing of quiz scores and badges
- [ ] Leaderboard for top scorers
- [ ] Support for more Indian regional languages

---

## 👨‍💻 Author

**Ansh Jamwal**
- USN: 1AH22CS015
- College: ACS College of Engineering, Bangalore
- Internship: MindMatrix, HSR Layout, Bengaluru

---

## 🙏 Acknowledgments

- **Mrs. Lakshmi G** — Internal Guide, Assistant Professor, Dept of CSE, ACSCE
- **Mr. Tirumal Mutalikdesai** — Program Coordinator, MindMatrix
- **Dr. Nandha Gopal S M** — HOD, Dept of CSE, ACSCE
- **Dr. B S Shylaja** — Principal, ACSCE

---

## 📜 License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.

---

*Kavya-Kanaja — Preserving Karnataka's Literary Heritage Through Technology* 🌸
