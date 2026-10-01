package com.example.jogodamemoria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val moves = intent.getIntExtra("MOVES", 0)
        val difficulty = intent.getStringExtra("DIFFICULTY") ?: ""

        val txtResultDifficulty = findViewById<TextView>(R.id.txtResultDifficulty)
        val txtResultMoves = findViewById<TextView>(R.id.txtResultMoves)
        val btnPlayAgain = findViewById<Button>(R.id.btnPlayAgain)
        val btnMainMenu = findViewById<Button>(R.id.btnMainMenu)

        txtResultDifficulty.text = getString(R.string.txt_result_difficulty, difficulty)
        txtResultMoves.text = getString(R.string.txt_result_moves, moves)

        btnPlayAgain.setOnClickListener {
            // Volta para a tela de configurações para escolher a dificuldade novamente
            val intent = Intent(this, ConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        btnMainMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
