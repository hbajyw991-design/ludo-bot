package com.example.ludobot

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val btnStart = Button(this).apply {
            text = "تفعيل بوت اللودو العائم"
            setOnClickListener {
                checkOverlayPermission()
            }
        }
        setContentView(btnStart)
    }

    private fun checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1234)
        } else {
            startBotService()
        }
    }

    private fun startBotService() {
        val intent = Intent(this, LudoBotService::class.java)
        startService(intent)
        Toast.makeText(this, "تم تشغيل القائمة العائمة، افتح اللعبة الآن", Toast.LENGTH_LONG).show()
        finish()
    }
}
