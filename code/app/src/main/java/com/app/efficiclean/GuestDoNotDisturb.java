package com.app.efficiclean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GuestDoNotDisturb extends AppCompatActivity {

    private Button btHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_do_not_disturb);

        btHome = (Button) findViewById(R.id.btHome);
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GuestDoNotDisturb.this, GuestHome.class);
                startActivity(i);
            }
        });
    }
}
