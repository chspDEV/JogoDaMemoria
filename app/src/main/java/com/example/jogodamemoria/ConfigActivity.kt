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

        btnEasy.setOnClickListener { startGame(8) }
        btnMedium.setOnClickListener { startGame(12) }
        btnHard.setOnClickListener { startGame(16) }
    }

    private fun startGame(cardCount: Int) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("CARD_COUNT", cardCount)
        startActivity(intent)
    }
}
