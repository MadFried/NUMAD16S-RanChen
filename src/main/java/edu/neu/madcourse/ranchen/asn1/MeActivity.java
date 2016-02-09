package edu.neu.madcourse.ranchen.asn1;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import edu.neu.madcourse.ranchen.R;

public class MeActivity extends Activity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        textView =(TextView)findViewById(R.id.phone_id);
        String imeistring = null;
        String imsistring = null;

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        imeistring = telephonyManager.getDeviceId();
        textView.append("IMEI No:" + imeistring + "\n");

        imsistring = telephonyManager.getSubscriberId();
        textView.append("IMSI No:" + imsistring + "\n");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
