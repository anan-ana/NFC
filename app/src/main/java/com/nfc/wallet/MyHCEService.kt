package com.nfc.wallet

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHCEService : HostApduService() {

    companion object {
        private const val TAG = "NFC_Dev_Host"
        
        // 成功狀態碼
        val STATUS_SUCCESS = byteArrayOf(0x90.toByte(), 0x00.toByte())
        // 失敗/未知指令狀態碼
        val STATUS_FAILED = byteArrayOf(0x6F.toByte(), 0x00.toByte())
        
        // 模擬的卡片數據 (根據你之前讀取到的學生證 UID 進行修改)
        // 假設 UID 為 A1B2C3D4
        val MOCK_CARD_DATA = byteArrayOf(0xA1.toByte(), 0xB2.toByte(), 0xC3.toByte(), 0xD4.toByte())
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) return STATUS_FAILED

        val hexCommand = commandApdu.joinToString("") { "%02X".format(it) }
        Log.d(TAG, "收到指令: $hexCommand")

        // 1. 處理 SELECT AID 指令 (通常是 00A40400...)
        if (hexCommand.startsWith("00A40400")) {
            Log.d(TAG, "讀卡機正在進行應用選取 (Select AID)")
            return STATUS_SUCCESS
        }

        // 2. 處理讀取指令 (某些讀卡機會發送 00B0 開頭的 Read Binary)
        if (hexCommand.startsWith("00B0")) {
            Log.d(TAG, "讀卡機嘗試讀取數據，回傳卡片 UID")
            return MOCK_CARD_DATA + STATUS_SUCCESS
        }

        // 3. 預設回傳：如果讀卡機有反應但無權限，嘗試直接回傳數據 + 成功碼
        // 這是許多開發者採取的「暴力應答」法，確保讀卡機能拿到一串 ID
        Log.d(TAG, "未知指令，嘗試通用回傳")
        return MOCK_CARD_DATA + STATUS_SUCCESS
    }

    override fun onDeactivated(reason: Int) {
        Log.d(TAG, "連線中斷，原因: $reason")
    }
}