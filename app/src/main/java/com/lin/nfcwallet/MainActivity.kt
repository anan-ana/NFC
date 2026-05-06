package com.nfc.wallet

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    private lateinit var statusText: TextView
    private lateinit var cardListLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 動態建立簡單介面
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            padding = 50
        }
        statusText = TextView(this).apply { 
            text = "狀態：等待讀取卡片..."
            textSize = 18f
            setPadding(0, 0, 0, 40)
        }
        cardListLayout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        
        val title = TextView(this).apply { text = "已儲存的卡片 (點擊設為模擬)："; textSize = 16f }
        
        root.addView(statusText)
        root.addView(title)
        root.addView(cardListLayout)
        setContentView(root)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        pendingIntent = PendingIntent.getActivity(this, 0, intent, flags)
        
        refreshCardList()
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, null)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        tag?.let {
            val uid = it.id.joinToString("") { byte -> "%02X".format(byte) }
            showNamingDialog(uid)
        }
    }

    private fun showNamingDialog(uid: String) {
        val input = EditText(this).apply { hint = "例如：宿舍門禁、公司卡" }
        AlertDialog.Builder(this)
            .setTitle("讀取成功！")
            .setMessage("卡片 UID: $uid\n請輸入自訂名稱：")
            .setView(input)
            .setPositiveButton("儲存") { _, _ ->
                saveCard(uid, input.text.toString())
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun saveCard(uid: String, name: String) {
        val prefs = getSharedPreferences("NFC_DATA", Context.MODE_PRIVATE)
        val cardSet = prefs.getStringSet("CARD_IDS", mutableSetOf()) ?: mutableSetOf()
        val newSet = cardSet.toMutableSet()
        newSet.add(uid)
        
        prefs.edit()
            .putStringSet("CARD_IDS", newSet)
            .putString("NAME_$uid", name)
            .apply()
        
        refreshCardList()
        Toast.makeText(this, "儲存成功", Toast.LENGTH_SHORT).show()
    }

    private fun refreshCardList() {
        cardListLayout.removeAllViews()
        val prefs = getSharedPreferences("NFC_DATA", Context.MODE_PRIVATE)
        val cardSet = prefs.getStringSet("CARD_IDS", null) ?: return
        val activeUid = prefs.getString("ACTIVE_UID", "")

        for (uid in cardSet) {
            val name = prefs.getString("NAME_$uid", "未命名")
            val btn = Button(this).apply {
                text = if (uid == activeUid) "★ $name ($uid) - 模擬中" else "$name ($uid)"
                setOnClickListener {
                    prefs.edit().putString("ACTIVE_UID", uid).apply()
                    statusText.text = "目前模擬中：$name"
                    refreshCardList()
                }
            }
            cardListLayout.addView(btn)
        }
    }
}