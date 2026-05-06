package com.nfc.wallet; // 請確保與你的專案 package 名稱一致

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class MyHCEService extends HostApduService {

    // 當讀卡機送來指令時觸發
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        if (commandApdu == null) {
            return new byte[]{(byte)0x6A, (byte)0x82}; // 回傳「找不到檔案」狀態碼
        }
        
        Log.d("HCE", "收到指令: " + bytesToHex(commandApdu));

        // 這裡回傳 90 00 代表成功接收指令
        return new byte[]{(byte)0x90, (byte)0x00};
    }

    @Override
    public void onDeactivated(int reason) {
        Log.d("HCE", "連線中斷，原因代碼: " + reason);
    }

    // 輔助工具：將 Byte 轉換為 Hex 字串方便除錯
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}