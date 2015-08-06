package com.android.appmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import util.SmsTool;

public class FakeSmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("appmonitor.sms")) {
            //send broadcast to monitored app
//            byte[] pdu = SmsTool.createFakeSms("13268225807","wocaonima");
            System.out.print("send broadcast");
        }
    }
}
