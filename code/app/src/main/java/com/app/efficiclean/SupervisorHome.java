package com.app.efficiclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SupervisorHome extends AppCompatActivity {

    private Button hazardApproval;
    private Button cleansApproval;
    private Button breakApproval;
    private Button approveSevereMess;
    private Button reportAbsences;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_home);

        hazardApproval = (Button) findViewById(R.id.btHazardApproval);
        hazardApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, HazardApproval.class);
                startActivity(i);
            }
        });

        cleansApproval = (Button) findViewById(R.id.btCleansApproval);
        cleansApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, CleanApproval.class);
                startActivity(i);
            }
        });

        breakApproval = (Button) findViewById(R.id.btApproveBreak);
        breakApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, BreakApproval.class);
                startActivity(i);
            }
        });

        approveSevereMess = (Button) findViewById(R.id.btSevereMessApproval);
        approveSevereMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, ApproveSevereMess.class);
                startActivity(i);
            }
        });

        reportAbsences = (Button) findViewById(R.id.btReportAbsence);
        reportAbsences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, ReportAbsences.class);
                startActivity(i);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth fbAuth) {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Add authentication listener
        mAuth.addAuthStateListener(mAuthListener);
    }
}
