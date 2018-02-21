package com.app.efficiclean.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.app.efficiclean.R;
import com.app.efficiclean.classes.Housekeeper;
import com.app.efficiclean.classes.QueueHandler;
import com.app.efficiclean.classes.QueueHandlerCreater;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class SupervisorHome extends AppCompatActivity {

    private String supervisorKey;
    private DataSnapshot staff;
    private DatabaseReference mStaffRef;
    private Button btViewMap;
    private Button hazardApproval;
    private Button cleansApproval;
    private Button breakApproval;
    private Button approveSevereMess;
    private Button reportAbsences;
    private String hotelID;
    private Bundle extras;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private QueueHandler qHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.app.efficiclean.R.layout.activity_supervisor_home);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
        }

        btViewMap = (Button) findViewById(com.app.efficiclean.R.id.btViewMap);
        btViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, MapView.class);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        hazardApproval = (Button) findViewById(com.app.efficiclean.R.id.btHazardApproval);
        hazardApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, HazardApprovalsList.class);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        cleansApproval = (Button) findViewById(com.app.efficiclean.R.id.btCleansApproval);
        cleansApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, SupervisorApprovals.class);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        breakApproval = (Button) findViewById(com.app.efficiclean.R.id.btApproveBreak);
        breakApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, BreakApproval.class);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        approveSevereMess = (Button) findViewById(com.app.efficiclean.R.id.btSevereMessApproval);
        approveSevereMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, SevereMessApprovalsList.class);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        reportAbsences = (Button) findViewById(com.app.efficiclean.R.id.btReportAbsence);
        reportAbsences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SupervisorHome.this, ReportAbsences.class);
                i.putExtras(extras);
                startActivity(i);
            }
        });

        qHandler = QueueHandlerCreater.createHandler(hotelID);

        mStaffRef = FirebaseDatabase.getInstance().getReference(hotelID).child("staff");
        mStaffRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                staff = dataSnapshot;
                setRoomApprovals();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }

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

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Are you sure you want to sign out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    public void setRoomApprovals(){
        TableLayout table = (TableLayout) findViewById(com.app.efficiclean.R.id.tbTeamProgress);
        TextView template = (TextView) findViewById(R.id.tvRow1);

        table.removeViews(1, table.getChildCount() - 1);
        for(DataSnapshot ds : staff.getChildren()) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView roomNumber = new TextView(this);
            roomNumber.setText(ds.getValue(Housekeeper.class).getUsername());
            roomNumber.setTextSize(template.getTextSize() / 2);
            roomNumber.setWidth(template.getWidth());
            roomNumber.setHeight(template.getHeight());
            roomNumber.setPadding(
                    template.getPaddingLeft(),
                    template.getPaddingTop() - 5,
                    template.getPaddingRight(),
                    template.getPaddingBottom());
            roomNumber.setBackground(template.getBackground());
            roomNumber.setGravity(template.getGravity());

            tr.addView(roomNumber);
            table.addView(tr);
        }
    }

}


