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
        super.onCreate(savedInstanceState: Bundle?)
        
        val textView = TextView(this)
        textView.text = "請將 NFC 卡片貼近手機"
        textView.textSize = 24f
        setContentView(textView)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        
        // 建立 Intent
        val intent = Intent(this, javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        
        // 關鍵修正：顯式定義 flag 避免編譯器類型推斷錯誤
        val pendingIntentFlags: Int = PendingIntent.FLAG_MUTABLE
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, pendingIntentFlags)
        
        // 啟動前台調度
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // 偵測到 NFC 標籤後的行為
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            val resultTextView = TextView(this).apply {
                text = "偵測成功：已讀取 NFC 卡片"
                textSize = 24f
            }
            setContentView(resultTextView)
        }
    }
}