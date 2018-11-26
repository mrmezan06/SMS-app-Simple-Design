package com.mezan.sms_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
        SmsMessage[] messages=null;
        String str="";
        if(bundle != null){
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for(int i=0;i<messages.length;i++){
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += "Message From : "+messages[i].getOriginatingAddress()+"\n";
                str += "Body : "+messages[i].getMessageBody().toString()+"\n";
            }
            Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
            Intent broadcastIntent=new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("sms",str);
            context.sendBroadcast(broadcastIntent);
        }
    }
}
