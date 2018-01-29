package lc_k.cadena;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import broadcastReceivers.smsReceivers.LocationSmsReceiver;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnFindCadena = (Button) findViewById(R.id.btnFindCadena);
        btnFindCadena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSmsBackground();
            }
        });

        try {
            BroadcastReceiver locationSmsReceiver = new LocationSmsReceiver();
            IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            this.registerReceiver(locationSmsReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver();
    }

    private void sendSmsBackground() {
        try {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                String strPhone = getString(R.string.CADENA_PHONE_NUMBER);
                String strMessage = "GPS_REQUEST";
                SmsManager smsManager = SmsManager.getDefault();
                PendingIntent sentPI;
                String SENT = "SMS_SENT";
                sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
                smsManager.sendTextMessage(strPhone, null, strMessage, sentPI, null);
                Toast.makeText(this, "Message sent. Waiting for the response...", Toast.LENGTH_SHORT).show();

                System.out.println(" Sms with Pending Intent sent...");
            } else {
                System.out.println(" **** permission has to be asked. asking for permission *** ");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
