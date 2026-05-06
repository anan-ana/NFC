package com.nfc.wallet;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;

public class MyHostApduService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        // 基本回應：90 00 (代表指令執行成功)
        return new byte[]{(byte) 0x90, (byte) 0x00};
    }

    @Override
    public void onDeactivated(int reason) {}
}