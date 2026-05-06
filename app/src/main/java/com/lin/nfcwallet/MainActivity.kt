package com.lin.nfcwallet

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.graphics.Color

class MainActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 動態建立簡單精美的 UI
        statusText = TextView(this).apply {
            text = "請將 NFC 卡片貼近手機背面"
            textSize = 20f
            setTextColor(Color.BLACK)
            gravity = Gravity.CENTER
        }
        setContentView(statusText)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        // 啟用前台調度，確保 App 開啟時優先攔截 NFC 信號
        nfcAdapter?.enableReaderMode(this, { tag ->
            runOnUiThread {
                handleTag(tag)
            }
        }, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
    }

    private fun handleTag(tag: Tag) {
        val id = tag.id.joinToString(":") { "%02X".format(it) }
        statusText.text = "感應成功！\n卡片編號：$id\n錢包餘額：$100.00"
        statusText.setTextColor(Color.parseColor("#4CAF50")) // 成功綠
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableReaderMode(this)
    }
}