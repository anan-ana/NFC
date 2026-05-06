package com.lin.nfcwallet

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val textView = TextView(this)
        textView.text = "請將 NFC 卡片貼近手機"
        textView.textSize = 24f
        setContentView(textView)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        
        // 使用原始整數值 33554432 代表 PendingIntent.FLAG_MUTABLE
        // 這可以避開編譯器對該常數類型的解析錯誤
        val flags = 33554432 
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, flags)
        
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val resultTextView = TextView(this).apply {
                text = "偵測成功：已讀取 NFC 卡片"
                textSize = 24f
            }
            setContentView(resultTextView)
        }
    }
}