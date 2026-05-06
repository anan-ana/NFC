package com.nfc.wallet

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHCEService : HostApduService() {

    // 當讀卡機發送指令 (APDU) 時觸發
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        Log.d("NFCWallet", "收到 NFC 讀卡機指令")
        
        // 回傳 90 00 代表指令執行成功 (模擬卡片回應)
        return byteArrayOf(0x90.toByte(), 0x00.toByte())
    }

    override fun onDeactivated(reason: Int) {
        Log.d("NFCWallet", "NFC 連結已斷開，原因代碼: $reason")
    }
}