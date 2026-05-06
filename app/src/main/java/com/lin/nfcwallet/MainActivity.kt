package com.nfc.wallet;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private TextView logText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logText = findViewById(R.id.logText);
        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // 修正 Android 12+ 必備的 FLAG_MUTABLE，避免閃退
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 
                PendingIntent.FLAG_MUTABLE);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String id = bytesToHex(tag.getId());
            logText.setText("讀取到卡片 UID: " + id + "\n(注意：非 Root 手機難以模擬此 ID)");
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02X", b));
        return sb.toString();
    }

    @Override
    protected void onResume() { super.onResume(); if(nfcAdapter != null) nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null); }
    @Override
    protected void onPause() { super.onPause(); if(nfcAdapter != null) nfcAdapter.disableForegroundDispatch(this); }
}