package com.nfc.wallet

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            statusText.text = "此裝置不支援 NFC"
        } else if (!nfcAdapter!!.isEnabled) {
            statusText.text = "請開啟手機 NFC 功能"
        }
    }

    override fun onResume() {
        super.onResume()
        // 核心邏輯：告訴系統，只要我在前台，所有的 NFC 感應都先丟給我
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        
        // 針對不同 Android 版本設定 PendingIntent 旗標
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, flags)
        
        // 開始攔截 (這會取消你提到的系統預設彈窗)
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        // 當 App 不在畫面時，把控制權還給系統
        nfcAdapter?.disableForegroundDispatch(this)
    }

    // 當系統攔截到卡片並傳回 App 時，會觸發這裡
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            
            // 讀取物理卡片的 UID
            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let {
                val id = it.id.joinToString("") { byte -> "%02X".format(byte) }
                
                // 更新 UI，不再只是那句「請靠近門禁卡」
                statusText.text = """
                    ✅ 讀取成功！
                    卡片 UID: $id
                    
                    訊號已鎖定，現在您可以嘗試靠向讀卡機。
                """.trimIndent()
            }
        }
    }
}