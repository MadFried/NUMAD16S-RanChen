package edu.neu.madcourse.ranchen.communication;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.ranchen.R;

public class CommunicationTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication_test);

        Button registerButton = (Button)findViewById(R.id.registration_button);
        Button registedButton = (Button)findViewById(R.id.registed_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CommunicationTest.this, UserRegistration.class);
                startActivity(intent);
            }
        });

        registedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CommunicationTest.this, RegistedList.class);
                startActivity(intent);
            }
        });

    }

}
