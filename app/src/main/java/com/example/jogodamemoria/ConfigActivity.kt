package com.example.jogodamemoria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_config)

        val btnEasy = findViewById<Button>(R.id.btnEasy)
        val btnMedium = findViewById<Button>(R.id.btnMedium)
        val btnHard = findViewById<Button>(R.id.btnHard)

        btnEasy.setOnClickListener { playButtonSound(); startGame(8) }
        btnMedium.setOnClickListener { playButtonSound(); startGame(12) }
        btnHard.setOnClickListener { playButtonSound(); startGame(16) }

        val prefs = getSharedPreferences("MemoryGame", android.content.Context.MODE_PRIVATE)
        val highScore = prefs.getInt("HIGH_SCORE", 0)
        val txtHighScore = findViewById<android.widget.TextView>(R.id.txtHighScore)
        txtHighScore.text = getString(R.string.txt_high_score, highScore)
    }

    private fun startGame(cardCount: Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("CARD_COUNT", cardCount)
        startActivity(intent)
    }

    private fun playButtonSound() {
        val mp = android.media.MediaPlayer.create(this, R.raw.sfx_botao)
        mp.setOnCompletionListener { it.release() }
        mp.start()
    }
}
