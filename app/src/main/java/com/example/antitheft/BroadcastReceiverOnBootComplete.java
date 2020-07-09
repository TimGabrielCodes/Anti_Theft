package com.example.antitheft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import Services.StickyService;

public class BroadcastReceiverOnBootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, StickyService.class);
            context.startService(serviceIntent);
        }
    }
}