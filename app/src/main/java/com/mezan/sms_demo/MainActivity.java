package com.mezan.sms_demo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText number,Msg;
    Button btnSend;
    IntentFilter intentFilter;
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView txt=(TextView)findViewById(R.id.receivetxt);
            txt.setText(intent.getExtras().getString("sms"));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sms received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS},1);

        number=(EditText)findViewById(R.id.numbertxt);
        Msg=(EditText)findViewById(R.id.msgtxt);
        btnSend=(Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num=number.getText().toString();
                String msg=Msg.getText().toString();
                sendMsg(num,msg);

            }
        });
    }

    private void sendMsg(String num, String msg) {
        String SENT="Message Sent";
        String DELIVERED="Message Delivered";

        PendingIntent sendPI=PendingIntent.getBroadcast(this,0,new Intent(SENT),0);
        PendingIntent deliverePI=PendingIntent.getBroadcast(this,0,new Intent(DELIVERED),0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS Sent",Toast.LENGTH_LONG).show();
                        /*AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this)
                                .setMessage("SMS Sent");
                        AlertDialog popup=ad.create();
                        popup.show();*/
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(MainActivity.this,"Generic Failure",Toast.LENGTH_LONG).show();
                        /*ad=new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Generic Failure");
                        popup=ad.create();
                        popup.show();*/
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(MainActivity.this,"No Service",Toast.LENGTH_LONG).show();
                        /*ad=new AlertDialog.Builder(MainActivity.this)
                                .setMessage("No Service");
                        popup=ad.create();
                        popup.show();*/
                        break;
                }
            }
        },new IntentFilter(SENT));
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        Toast.makeText(MainActivity.this,"SMS Delivered",Toast.LENGTH_LONG).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MainActivity.this,"SMS Pending",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        },new IntentFilter(DELIVERED));

        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(num,null,msg,sendPI,deliverePI);
    }

    @Override
    protected void onResume() {

        registerReceiver(intentReceiver,intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }
}
