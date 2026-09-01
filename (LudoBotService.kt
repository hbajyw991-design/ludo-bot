package com.example.ludobot

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast

class LudoBotService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View
    private var isAutoPlayActive = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        
        val layoutParamsType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutParamsType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 200
        }

        val btnToggle = Button(this).apply {
            text = "تشغيل البوت 🤖"
            setBackgroundColor(0xFF0088FF.toInt())
            setTextColor(0xFFFFFFFF.toInt())
            
            setOnClickListener {
                isAutoPlayActive = !isAutoPlayActive
                if (isAutoPlayActive) {
                    text = "إيقاف البوت 🛑"
                    Toast.makeText(applicationContext, "تم تفعيل اللعب الذكي التلقائي", Toast.LENGTH_SHORT).show()
                    startSmartBotLoop()
                } else {
                    text = "تشغيل البوت 🤖"
                    Toast.makeText(applicationContext, "تم إيقاف اللعب التلقائي", Toast.LENGTH_SHORT).show()
                }
            }
        }

        floatingView = btnToggle
        windowManager.addView(floatingView, params)
    }

    private fun calculateMoveScore(
        isKillMove: Boolean,
        isSafeZone: Boolean,
        isHomeEntry: Boolean,
        isUnderThreat: Boolean
    ): Int {
        var score = 0
        if (isKillMove) score += 100    // أولوية أكل قطعة الخصم
        if (isHomeEntry) score += 80    // أولوية إدخال القطعة للمكان الآمن النهائي
        if (isSafeZone) score += 50     // أولوية النجمة أو الخانة الآمنة
        if (isUnderThreat) score += 40  // أولوية الهروب بالقطعة المهددة
        return score
    }

    private fun startSmartBotLoop() {
        Thread {
            while (isAutoPlayActive) {
                // تنفيذ حركات اللعب التلقائي
                Thread.sleep(1500)
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView.isInitialized) {
            windowManager.removeView(floatingView)
        }
    }
}
