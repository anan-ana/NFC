package com.nfc.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 建立簡單的介面顯示狀態
        val tv = TextView(this).apply {
            text = "NFC 智慧錢包\n\n服務已啟動\n請靠近讀卡機測試"
            textSize = 24f
            setTextColor(Color.parseColor("#2196F3"))
            gravity = Gravity.CENTER
            setPadding(50, 50, 50, 50)
        }
        setContentView(tv)
    }
}