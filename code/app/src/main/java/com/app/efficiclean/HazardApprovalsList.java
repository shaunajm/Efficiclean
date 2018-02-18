package com.app.efficiclean;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.app.efficiclean.classes.Approval;
import com.app.efficiclean.classes.Supervisor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class HazardApprovalsList extends AppCompatActivity {

    private String supervisorKey;
    private String hotelID;
    private Bundle extras;
    private Supervisor supervisor;
    private DatabaseReference mSuperRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_list_hazards);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        if (extras != null) {
            hotelID = extras.getString("hotelID");
            supervisorKey = extras.getString("staffKey");
        }

        mSuperRef = FirebaseDatabase.getInstance().getReference(hotelID).child("supervisor").child(supervisorKey);
        mSuperRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supervisor = dataSnapshot.getValue(Supervisor.class);
                setRoomApprovals();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mAuth = FirebaseAuth.getInstance();

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

    public void setRoomApprovals(){
        TableLayout table = (TableLayout) findViewById(R.id.tbHazardsToBeApproved);
        TextView template = (TextView) findViewById(R.id.tvRow1);

        table.removeViews(1, table.getChildCount() - 1);
        for(final Approval approval : supervisor.approvals.values()) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView roomNumber = new TextView(this);
            roomNumber.setText(approval.getJob().getRoomNumber());
            roomNumber.setTextSize(template.getTextSize() / 2);
            roomNumber.setWidth(template.getWidth());
            roomNumber.setHeight(template.getHeight());
            roomNumber.setPadding(
                    template.getPaddingLeft(),
                    template.getPaddingTop() - 5,
                    template.getPaddingRight(),
                    template.getPaddingBottom());
            roomNumber.setGravity(template.getGravity());

            tr.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(HazardApprovalsList.this, HazardApprovalPage.class);
                    extras.putString("roomNumber", approval.getJob().getRoomNumber());
                    i.putExtras(extras);
                    startActivity(i);
                }
            });

            tr.addView(roomNumber);
            table.addView(tr);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}