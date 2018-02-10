package com.app.efficiclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SupervisorHome extends AppCompatActivity {

    private Button hazardApproval;
    private Button cleansApproval;
    private Button breakApproval;
    private Button reportSevereMess;
    private Button reportAbsences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home);

        reportAbsences = (Button) findViewById(R.id.btReportAbsence);
        reportAbsences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, ReportAbsences.class);
                startActivity(i);
            }
        });
    }
}
