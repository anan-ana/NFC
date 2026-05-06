package com.nfc.wallet

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.content.Context

class MyHCEService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        val prefs = getSharedPreferences("NFC_DATA", Context.MODE_PRIVATE)
        val activeUid = prefs.getString("ACTIVE_UID", "") ?: ""

        if (activeUid.isEmpty()) return byteArrayOf(0x6A.toByte(), 0x82.toByte()) // 回傳錯誤

        // 將儲存的 UID 字串轉回 Byte 陣列發送給讀卡機
        return hexToBytes(activeUid)
    }

    override fun onDeactivated(reason: Int) {}

    private fun hexToBytes(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}