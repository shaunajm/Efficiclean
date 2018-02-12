package com.app.efficiclean;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StaffCurrentRoom extends AppCompatActivity {

    private Button btMarkClean;
    private Button btReportHazard;
    private Button btReportSevereMess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_current_room);

        btReportHazard = (Button) findViewById(R.id.btReportHazard);
        btReportHazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffCurrentRoom.this, ReportHazard.class);
                startActivity(i);
            }
        });

        btReportSevereMess = (Button) findViewById(R.id.btReportSevereMess);
        btReportSevereMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StaffCurrentRoom.this, ReportSevereMess.class);
                startActivity(i);
            }
        });


    }
}
