package com.example.jogodamemoria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener {
            playButtonSound()
            val intent = Intent(this, ConfigActivity::class.java)
            startActivity(intent)
        }

        val btnLanguage = findViewById<Button>(R.id.btnLanguage)
        btnLanguage.setOnClickListener {
            playButtonSound()
            val currentLang = resources.configuration.locales.get(0).language
            val newLang = if (currentLang == "en") "pt" else "en"
            
            val locale = java.util.Locale(newLang)
            java.util.Locale.setDefault(locale)
            val config = android.content.res.Configuration()
            config.setLocale(locale)
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
            
            recreate()
        }
    }

    private fun playButtonSound() {
        val mp = android.media.MediaPlayer.create(this, R.raw.sfx_botao)
        mp.setOnCompletionListener { it.release() }
        mp.start()
    }
}