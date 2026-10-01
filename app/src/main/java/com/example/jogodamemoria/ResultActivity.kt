package com.example.jogodamemoria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    private var mediaPlayer: android.media.MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        
        mediaPlayer = android.media.MediaPlayer.create(this, R.raw.sfx_vitoria)
        mediaPlayer?.start()

        val moves = intent.getIntExtra("MOVES", 0)
        val difficulty = intent.getStringExtra("DIFFICULTY") ?: ""

        val txtResultDifficulty = findViewById<TextView>(R.id.txtResultDifficulty)
        val txtResultMoves = findViewById<TextView>(R.id.txtResultMoves)
        val txtResultTime = findViewById<TextView>(R.id.txtResultTime)
        val txtResultScore = findViewById<TextView>(R.id.txtResultScore)
        val btnPlayAgain = findViewById<Button>(R.id.btnPlayAgain)
        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)

        val time = intent.getIntExtra("TIME", 0)
        val score = intent.getIntExtra("SCORE", 0)

        txtResultDifficulty.text = getString(R.string.txt_result_difficulty, difficulty)
        txtResultMoves.text = getString(R.string.txt_result_moves, moves)
        txtResultTime.text = getString(R.string.txt_result_time, time)
        txtResultScore.text = getString(R.string.txt_result_score, score)

        btnPlayAgain.setOnClickListener {
            playButtonSound()
            // Volta para a tela de configurações para escolher a dificuldade novamente
            val intent = Intent(this, ConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        btnMainMenu.setOnClickListener {
            playButtonSound()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun playButtonSound() {
        val mp = android.media.MediaPlayer.create(this, R.raw.sfx_botao)
        mp.setOnCompletionListener { it.release() }
        mp.start()
    }
}
