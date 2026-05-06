package com.nfc.wallet

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class MyHCEService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        // 當手機感應到讀卡機時，回傳成功狀態碼 90 00
        return byteArrayOf(0x90.toByte(), 0x00.toByte())
    }

    override fun onDeactivated(reason: Int) {
        Log.d("HCE", "NFC 連結已中斷")
    }
}