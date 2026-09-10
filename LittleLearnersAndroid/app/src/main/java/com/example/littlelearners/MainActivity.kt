package com.example.littlelearners

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.math.min
import kotlin.random.Random

data class ColorItem(val name: String, val hex: String)

data class WordItem(val word: String, val icon: String)

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var root: LinearLayout
    private lateinit var scoreView: TextView
    private lateinit var levelView: TextView

    private val prefs by lazy { getSharedPreferences("progress", Context.MODE_PRIVATE) }

    private var score = 0
    private var level = 1
    private var difficulty = 0 // 0 easy, 1 medium, 2 hard

    private val colors = listOf(
        ColorItem("Red", "#F44336"), ColorItem("Blue", "#2196F3"),
        ColorItem("Yellow", "#FFEB3B"), ColorItem("Green", "#4CAF50"),
        ColorItem("Orange", "#FF9800"), ColorItem("Purple", "#9C27B0"),
        ColorItem("Pink", "#E91E63"), ColorItem("Brown", "#795548"),
        ColorItem("Black", "#212121"), ColorItem("White", "#FFFFFF"),
        ColorItem("Gray", "#9E9E9E"), ColorItem("Cyan", "#00BCD4"),
        ColorItem("Teal", "#009688"), ColorItem("Lime", "#CDDC39"),
        ColorItem("Indigo", "#3F51B5"), ColorItem("Gold", "#FFD700"),
        ColorItem("Silver", "#C0C0C0"), ColorItem("Navy", "#000080"),
        ColorItem("Sky Blue", "#87CEEB"), ColorItem("Violet", "#8A2BE2"),
        ColorItem("Coral", "#FF7F50"), ColorItem("Tomato", "#FF6347"),
        ColorItem("Salmon", "#FA8072"), ColorItem("Turquoise", "#40E0D0"),
        ColorItem("Olive", "#808000"), ColorItem("Maroon", "#800000"),
        ColorItem("Beige", "#F5F5DC"), ColorItem("Mint", "#98FF98"),
        ColorItem("Lavender", "#E6E6FA"), ColorItem("Peach", "#FFE5B4"),
        ColorItem("Magenta", "#FF00FF"), ColorItem("Aqua", "#00FFFF")
    )

    private val letters = ('A'..'Z').toList()
    private val letterWords = mapOf(
        'A' to "Apple 🍎", 'B' to "Ball ⚽", 'C' to "Cat 🐱", 'D' to "Dog 🐶",
        'E' to "Elephant 🐘", 'F' to "Fish 🐟", 'G' to "Giraffe 🦒",
        'H' to "Horse 🐴", 'I' to "Ice cream 🍦", 'J' to "Juice 🧃",
        'K' to "Kite 🪁", 'L' to "Lion 🦁", 'M' to "Monkey 🐵",
        'N' to "Nest 🪺", 'O' to "Orange 🍊", 'P' to "Panda 🐼",
        'Q' to "Queen 👑", 'R' to "Rabbit 🐰", 'S' to "Sun ☀️",
        'T' to "Tiger 🐯", 'U' to "Umbrella ☂️", 'V' to "Van 🚌",
        'W' to "Whale 🐳", 'X' to "Xylophone 🎵", 'Y' to "Yo-yo 🪀",
        'Z' to "Zebra 🦓"
    )

    private val animals = listOf(
        WordItem("Cat", "🐱"), WordItem("Dog", "🐶"), WordItem("Lion", "🦁"),
        WordItem("Tiger", "🐯"), WordItem("Elephant", "🐘"), WordItem("Giraffe", "🦒"),
        WordItem("Monkey", "🐵"), WordItem("Rabbit", "🐰"), WordItem("Bear", "🐻"),
        WordItem("Panda", "🐼"), WordItem("Fox", "🦊"), WordItem("Frog", "🐸"),
        WordItem("Cow", "🐮"), WordItem("Pig", "🐷"), WordItem("Horse", "🐴"),
        WordItem("Sheep", "🐑"), WordItem("Chicken", "🐔"), WordItem("Duck", "🦆"),
        WordItem("Penguin", "🐧"), WordItem("Whale", "🐳")
    )

    private val fruits = listOf(
        WordItem("Apple", "🍎"), WordItem("Banana", "🍌"), WordItem("Orange", "🍊"),
        WordItem("Strawberry", "🍓"), WordItem("Watermelon", "🍉"), WordItem("Grapes", "🍇"),
        WordItem("Pineapple", "🍍"), WordItem("Peach", "🍑"), WordItem("Cherry", "🍒"),
        WordItem("Lemon", "🍋"), WordItem("Coconut", "🥥"), WordItem("Kiwi", "🥝")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        score = prefs.getInt("score", 0)
        level = prefs.getInt("level", 1)
        difficulty = prefs.getInt("difficulty", 0)
        tts = TextToSpeech(this, this)
        showHome()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            tts.setSpeechRate(0.78f)
        }
    }

    private fun saveProgress() {
        prefs.edit()
            .putInt("score", score)
            .putInt("level", level)
            .putInt("difficulty", difficulty)
            .apply()
    }

    private fun speak(text: String) {
        if (::tts.isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "lesson")
        }
    }

    private fun updateProgress() {
        scoreView.text = "⭐ $score"
        levelView.text = "Level $level"
    }

    private fun reward() {
        score++
        if (score % 5 == 0) level = min(level + 1, 20)
        saveProgress()
        updateProgress()
    }

    private fun base(title: String): LinearLayout {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(18, 18, 18, 18)
            setBackgroundColor(Color.rgb(142, 231, 255))
        }

        val top = LinearLayout(this).apply {
            gravity = Gravity.CENTER_VERTICAL
        }

        scoreView = TextView(this).apply {
            textSize = 18f
            setTextColor(Color.DKGRAY)
            setBackgroundColor(Color.WHITE)
            setPadding(14, 8, 14, 8)
        }
        top.addView(scoreView)

        levelView = TextView(this).apply {
            textSize = 18f
            setTextColor(Color.DKGRAY)
            setBackgroundColor(Color.WHITE)
            setPadding(14, 8, 14, 8)
        }
        val levelLp = LinearLayout.LayoutParams(-2, -2)
        levelLp.setMargins(8, 0, 0, 0)
        top.addView(levelView, levelLp)

        val titleView = TextView(this).apply {
            text = title
            textSize = 30f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(255, 60, 120))
            setPadding(5, 0, 5, 0)
        }
        top.addView(titleView, LinearLayout.LayoutParams(0, -2, 1f))
        root.addView(top)

        updateProgress()
        return root
    }

    private fun addButton(text: String, bg: Int = Color.WHITE, onClick: () -> Unit) {
        val b = Button(this).apply {
            this.text = text
            textSize = 21f
            isAllCaps = false
            setTextColor(Color.DKGRAY)
            setBackgroundColor(bg)
            minHeight = 66
            setOnClickListener { onClick() }
        }
        val lp = LinearLayout.LayoutParams(-1, -2)
        lp.setMargins(0, 6, 0, 6)
        root.addView(b, lp)
    }

    private fun addBack() {
        addButton("← Home", Color.rgb(255, 140, 105)) { showHome() }
    }

    private fun question(text: String): TextView =
        TextView(this).apply {
            this.text = text
            textSize = 25f
            gravity = Gravity.CENTER
            setTextColor(Color.DKGRAY)
            setBackgroundColor(Color.WHITE)
            setPadding(12, 18, 12, 18)
        }

    private fun showHome() {
        val v = base("Little Learners 🌈")

        val sub = TextView(this).apply {
            text = "Play • Learn • Grow"
            textSize = 20f
            gravity = Gravity.CENTER
            setPadding(0, 8, 0, 12)
        }
        v.addView(sub)

        addButton("🎨  Colors", Color.rgb(255, 204, 69)) { showColors() }
        addButton("🔤  A–Z Letters", Color.rgb(101, 216, 107)) { showLetters() }
        addButton("🔢  Numbers 1–20", Color.rgb(85, 170, 255)) { showNumbers() }
        addButton("🐶  Animals", Color.rgb(255, 180, 210)) { showWords("Animals", animals) }
        addButton("🍎  Fruits", Color.rgb(255, 210, 130)) { showWords("Fruits", fruits) }
        addButton("⚙️  Difficulty: ${difficultyName()}", Color.LTGRAY) { cycleDifficulty() }
        addButton("⭐  My Progress", Color.rgb(255, 245, 150)) { showProgress() }

        val hint = TextView(this).apply {
            text = "Your progress is saved automatically."
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 16, 0, 0)
        }
        v.addView(hint)
        setContentView(v)
    }

    private fun difficultyName(): String = when (difficulty) {
        0 -> "Easy"
        1 -> "Medium"
        else -> "Hard"
    }

    private fun cycleDifficulty() {
        difficulty = (difficulty + 1) % 3
        saveProgress()
        showHome()
        speak("Difficulty is now ${difficultyName()}")
    }

    private fun showColors() {
        val v = base("Colors 🎨")
        addBack()

        val target = colors.random()
        v.addView(question("Find the ${target.name} color!"))

        val count = when (difficulty) { 0 -> 4; 1 -> 6; else -> 8 }
        val options = mutableListOf(target)
        options.addAll(colors.filter { it != target }.shuffled().take(count - 1))
        options.shuffle()

        options.forEach { item ->
            val c = Color.parseColor(item.hex)
            val textColor = if (item.name == "White" || item.name == "Yellow" || item.name == "Lime" || item.name == "Gold") Color.DKGRAY else Color.WHITE
            val b = Button(this).apply {
                text = item.name
                textSize = 20f
                isAllCaps = false
                setTextColor(textColor)
                setBackgroundColor(c)
                minHeight = 62
                setOnClickListener {
                    if (item == target) {
                        reward()
                        showResult("🎉 Well Done!", "That is ${target.name}.")
                    } else {
                        speak("Try again")
                        showResult("😊 Try Again!", "Look for ${target.name}.")
                    }
                }
            }
            root.addView(b, LinearLayout.LayoutParams(-1, -2).apply {
                setMargins(0, 4, 0, 4)
            })
        }

        setContentView(v)
        speak("Find the ${target.name} color")
    }

    private fun showLetters() {
        val v = base("Letters A–Z 🔤")
        addBack()

        val target = letters.random()
        val example = letterWords[target] ?: "Letter $target"
        v.addView(question("Which letter is $target?"))
        val ex = TextView(this).apply {
            text = "🔊 $target is for $example"
            textSize = 22f
            gravity = Gravity.CENTER
            setPadding(5, 12, 5, 12)
        }
        v.addView(ex)

        val count = when (difficulty) { 0 -> 3; 1 -> 4; else -> 6 }
        val options = mutableListOf(target)
        options.addAll(letters.filter { it != target }.shuffled().take(count - 1))
        options.shuffle()

        options.forEach { letter ->
            addButton(letter.toString(), Color.WHITE) {
                if (letter == target) {
                    reward()
                    showResult("🌟 Great Job!", "$target is for $example")
                } else {
                    speak("Try again")
                    showResult("😊 Try Again!", "Find the letter $target.")
                }
            }
        }

        setContentView(v)
        speak("Which letter is $target?")
    }

    private fun showNumbers() {
        val v = base("Numbers 1–20 🔢")
        addBack()

        val target = Random.nextInt(1, 21)
        v.addView(question("How many objects are there?"))

        val emoji = listOf("🍎", "⭐", "🐟", "🍓", "⚽").random()
        val visual = TextView(this).apply {
            text = (1..target).joinToString(" ") { emoji }
            textSize = if (target > 12) 28f else 38f
            gravity = Gravity.CENTER
            setPadding(4, 12, 4, 12)
        }
        v.addView(visual)

        val count = when (difficulty) { 0 -> 3; 1 -> 5; else -> 7 }
        val options = mutableListOf(target)
        while (options.size < count) {
            val n = Random.nextInt(1, 21)
            if (!options.contains(n)) options.add(n)
        }
        options.shuffle()

        options.forEach { n ->
            addButton(n.toString(), Color.rgb(235, 230, 255)) {
                if (n == target) {
                    reward()
                    showResult("🎉 Excellent!", "The answer is $target.")
                } else {
                    speak("Try again")
                    showResult("😊 Try Again!", "Count the objects again.")
                }
            }
        }

        setContentView(v)
        speak("How many objects are there?")
    }

    private fun showWords(title: String, items: List<WordItem>) {
        val v = base("$title 📚")
        addBack()

        val target = items.random()
        v.addView(question("Which picture is ${target.word}?"))

        val count = when (difficulty) { 0 -> 4; 1 -> 6; else -> 8 }
        val options = mutableListOf(target)
        options.addAll(items.filter { it != target }.shuffled().take(count - 1))
        options.shuffle()

        options.forEach { item ->
            addButton("${item.icon}\n${item.word}", Color.WHITE) {
                if (item == target) {
                    reward()
                    showResult("🌟 Great Job!", "This is a ${target.word}.")
                } else {
                    speak("Try again")
                    showResult("😊 Try Again!", "Look for ${target.word}.")
                }
            }
        }

        setContentView(v)
        speak("Which picture is ${target.word}?")
    }

    private fun showResult(big: String, spoken: String) {
        val msg = TextView(this).apply {
            text = big
            textSize = 28f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(30, 130, 70))
            setPadding(5, 18, 5, 18)
        }
        root.addView(msg)
        speak(spoken)
        msg.postDelayed({
            if (msg.parent != null) root.removeView(msg)
        }, 1300)
    }

    private fun showProgress() {
        val v = base("My Progress ⭐")
        addBack()

        val text = TextView(this).apply {
            text = """
                ⭐ Stars: $score
                🏆 Level: $level
                🎯 Difficulty: ${difficultyName()}

                Keep learning and have fun!
            """.trimIndent()
            textSize = 24f
            gravity = Gravity.CENTER
            setTextColor(Color.DKGRAY)
            setBackgroundColor(Color.WHITE)
            setPadding(20, 35, 20, 35)
        }
        v.addView(text, LinearLayout.LayoutParams(-1, -2))

        addButton("🔊 Hear my progress", Color.rgb(190, 240, 255)) {
            speak("You have $score stars. You are on level $level. Keep learning and have fun!")
        }

        addButton("🏠 Back to Home", Color.rgb(120, 220, 130)) { showHome() }
        setContentView(v)
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
