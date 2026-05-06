package com.nfc.wallet

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHCEService : HostApduService() {

    // 當手機靠近讀卡機，且讀卡機發送匹配的 AID 時，會觸發此處
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) return byteArrayOf(0x6F.toByte(), 0x00.toByte()) // 回傳失敗

        val hexCommand = commandApdu.joinToString("") { "%02X".format(it) }
        Log.d("HCE", "收到讀卡機 APDU 指令: $hexCommand")

        // 這裡回傳 90 00，讀卡機才會認為「靠卡成功」並讀取到內容
        // 0x90, 0x00 是 ISO 7816 標準的「成功」狀態碼
        return byteArrayOf(0x90.toByte(), 0x00.toByte())
    }

    override fun onDeactivated(reason: Int) {
        Log.d("HCE", "NFC 連結已中斷，原因代碼: $reason")
    }
}