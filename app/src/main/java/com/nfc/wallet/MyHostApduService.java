package com.nfc.wallet;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

public class MyHostApduService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        // 回應「成功」指令 (90 00)，讓讀卡機認為這是一張有效的卡片
        return new byte[]{(byte) 0x90, (byte) 0x00};
    }

    @Override
    public void onDeactivated(int reason) {
        // 連線中斷時的處理邏輯
    }
}