package com.nfc.wallet

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHCEService : HostApduService() {

    /**
     * 當 NFC 讀卡機發送 SELECT AID 指令且符合我們設定的 AID 時，系統會呼叫此方法。
     */
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) {
            // 回傳 6A 82 代表找不到檔案 (File not found)
            return byteArrayOf(0x6A.toByte(), 0x82.toByte())
        }

        // 這裡可以將收到的 APDU 指令轉成 Hex 字串印出來檢查
        Log.d("HCE_Debug", "收到指令: ${bytesToHex(commandApdu)}")

        // 暫時模擬成功回應：回傳 90 00 (OK)
        return byteArrayOf(0x90.toByte(), 0x00.toByte())
    }

    override fun onDeactivated(reason: Int) {
        // 當 NFC 連結斷開時呼叫（例如手機移開讀卡機）
        Log.d("HCE_Debug", "NFC 連結已斷開，原因代碼: $reason")
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }
}