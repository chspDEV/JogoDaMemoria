package com.example.jogodamemoria

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {
    private var cardCount = 8
    private var pairsCount = 4
    private var moves = 0
    private var foundPairs = 0
    private lateinit var txtMoves: TextView
    private lateinit var txtPairs: TextView
    private lateinit var txtTime: TextView
    private lateinit var gridLayout: GridLayout
    
    private lateinit var soundPool: android.media.SoundPool
    private var soundFlip = 0
    private var soundMatch = 0
    private var soundError = 0
    
    private var timeInSeconds = 0
    private var timerHandler = Handler(Looper.getMainLooper())
    private var timerRunnable = object : Runnable {
        override fun run() {
            timeInSeconds++
            txtTime.text = getString(R.string.txt_time, timeInSeconds)
            timerHandler.postDelayed(this, 1000)
        }
    }

    private val images = listOf(
        R.drawable.carta_3_sushi,
        R.drawable.carta_hossomaki,
        R.drawable.carta_niguiri_camarao,
        R.drawable.carta_niguiri_cebolinha,
        R.drawable.carta_niguiri_salmao,
        R.drawable.carta_oniguiri,
        R.drawable.carta_roll_caviar,
        R.drawable.carta_temaki
    )
    private val backImage = R.drawable.carta_verso

    private var firstSelected: ImageView? = null
    private var secondSelected: ImageView? = null
    private var isProcessing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        cardCount = intent.getIntExtra("CARD_COUNT", 8)
        pairsCount = cardCount / 2

        txtMoves = findViewById(R.id.txtMoves)
        txtPairs = findViewById(R.id.txtPairs)
        txtTime = findViewById(R.id.txtTime)
        gridLayout = findViewById(R.id.gridLayout)

        updateStats()
        setupBoard()
        
        val audioAttributes = android.media.AudioAttributes.Builder()
            .setUsage(android.media.AudioAttributes.USAGE_GAME)
            .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = android.media.SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()
            
        soundFlip = soundPool.load(this, R.raw.sfx_virar_carta, 1)
        soundMatch = soundPool.load(this, R.raw.sfx_acertar, 1)
        soundError = soundPool.load(this, R.raw.sfx_errar, 1)

        timerHandler.postDelayed(timerRunnable, 1000)
    }

    private fun updateStats() {
        txtMoves.text = getString(R.string.txt_moves, moves)
        txtPairs.text = getString(R.string.txt_pairs, foundPairs, pairsCount)
    }

    private fun setupBoard() {
        val selectedImages = images.take(pairsCount)
        val deck = (selectedImages + selectedImages).shuffled()
        
        gridLayout.columnCount = 4
        
        val displayMetrics = resources.displayMetrics
        val density = displayMetrics.density
        val paddingPx = (16 * density).toInt() // activity padding
        val marginPx = (8 * density).toInt()   // card margins
        val screenWidth = displayMetrics.widthPixels
        
        // 4 columns, total margin per column is 2 * marginPx
        val availableWidth = screenWidth - (paddingPx * 2) - (marginPx * 2 * 4)
        val cardSize = availableWidth / 4
        
        for (i in 0 until cardCount) {
            val imageView = ImageView(this)
            imageView.setImageResource(backImage)
            imageView.setTag(R.id.tag_image_res, deck[i])
            imageView.setTag(R.id.tag_is_matched, false)
            
            val params = GridLayout.LayoutParams()
            params.rowSpec = GridLayout.spec(i / 4)
            params.columnSpec = GridLayout.spec(i % 4)
            params.width = cardSize
            params.height = (cardSize * 1.2).toInt() // slightly taller than wide for a card look
            params.setGravity(Gravity.CENTER)
            params.setMargins(marginPx, marginPx, marginPx, marginPx)
            imageView.layoutParams = params
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            imageView.setPadding(8, 8, 8, 8)

            imageView.setOnClickListener { onCardClicked(imageView) }
            gridLayout.addView(imageView)
        }
    }

    private fun onCardClicked(view: ImageView) {
        if (isProcessing) return
        val isMatched = view.getTag(R.id.tag_is_matched) as? Boolean ?: false
        if (isMatched) return
        if (view == firstSelected) return

        val imgRes = view.getTag(R.id.tag_image_res) as Int
        view.setImageResource(imgRes)
        soundPool.play(soundFlip, 1f, 1f, 0, 0, 1f)

        if (firstSelected == null) {
            firstSelected = view
        } else {
            secondSelected = view
            moves++
            updateStats()
            isProcessing = true
            
            val firstImgRes = firstSelected?.getTag(R.id.tag_image_res) as Int
            if (firstImgRes == imgRes) {
                // Match
                soundPool.play(soundMatch, 1f, 1f, 0, 0, 1f)
                firstSelected?.setTag(R.id.tag_is_matched, true)
                view.setTag(R.id.tag_is_matched, true)
                foundPairs++
                updateStats()
                firstSelected = null
                secondSelected = null
                isProcessing = false
                
                if (foundPairs == pairsCount) {
                    endGame()
                }
            } else {
                // No match
                soundPool.play(soundError, 1f, 1f, 0, 0, 1f)
                Handler(Looper.getMainLooper()).postDelayed({
                    firstSelected?.setImageResource(backImage)
                    view.setImageResource(backImage)
                    firstSelected = null
                    secondSelected = null
                    isProcessing = false
                }, 1000)
            }
        }
    }

    private fun endGame() {
        timerHandler.removeCallbacks(timerRunnable)
        
        val score = 100000 / (moves * maxOf(1, timeInSeconds))
        
        val prefs = getSharedPreferences("MemoryGame", android.content.Context.MODE_PRIVATE)
        val highScore = prefs.getInt("HIGH_SCORE", 0)
        if (score > highScore) {
            prefs.edit().putInt("HIGH_SCORE", score).apply()
        }

        val difficultyStr = when (cardCount) {
            8 -> getString(R.string.diff_easy)
            12 -> getString(R.string.diff_medium)
            else -> getString(R.string.diff_hard)
        }
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("MOVES", moves)
            putExtra("DIFFICULTY", difficultyStr)
            putExtra("TIME", timeInSeconds)
            putExtra("SCORE", score)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerHandler.removeCallbacks(timerRunnable)
        soundPool.release()
    }
}
