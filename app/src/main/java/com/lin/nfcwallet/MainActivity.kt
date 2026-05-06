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
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        
        // 修正點：確保 PendingIntent 的 Flag 處理正確
        val flags = PendingIntent.FLAG_MUTABLE
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
            val textView = TextView(this)
            textView.text = "偵測到 NFC 卡片！"
            textView.textSize = 24f
            setContentView(textView)
        }
    }
}