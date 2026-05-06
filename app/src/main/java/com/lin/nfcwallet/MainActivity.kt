package com.nfc.wallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.view.Gravity
import android.graphics.Color

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this).apply {
            text = "NFC 智慧錢包\n\n服務運作中\n請將手機靠近讀卡機"
            textSize = 22f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }
        setContentView(tv)
    }
}