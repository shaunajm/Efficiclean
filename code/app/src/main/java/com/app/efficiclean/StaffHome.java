package com.app.efficiclean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StaffHome extends AppCompatActivity {

    private Button btRequestBreak;
    private Button btCurrentRoom;
    private Button btTodaysTeams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);

        btRequestBreak = (Button) findViewById(R.id.btRequestBreak);
        btRequestBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, StaffRequestBreak.class);
                startActivity(i);
            }
        });

        btCurrentRoom = (Button) findViewById(R.id.btCurrentRoom);
        btCurrentRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, StaffCurrentRoom.class);
                startActivity(i);
            }
        });

        btTodaysTeams = (Button) findViewById(R.id.btTodaysTeams);
        btTodaysTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffHome.this, TodaysTeams.class);
                startActivity(i);
            }
        });

    }
}
