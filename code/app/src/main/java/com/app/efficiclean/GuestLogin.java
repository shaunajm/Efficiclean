package com.app.efficiclean;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GuestLogin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText forename;
    private EditText surname;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);

        forename = (EditText) findViewById(R.id.etForename);
        surname = (EditText) findViewById(R.id.etSurname);

        loginBtn = (Button) findViewById(R.id.btLogin);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButtonClick();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth fbAuth) {
                FirebaseUser user = fbAuth.getCurrentUser();
                if (user != null) {
                    Intent staffLoginPage = new Intent(GuestLogin.this, GuestHome.class);
                    startActivity(staffLoginPage);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    public void loginButtonClick() {
        forename = (EditText) findViewById(R.id.etForename);
        surname = (EditText) findViewById(R.id.etSurname);
        String fString = forename.getText().toString().trim();
        String sString = surname.getText().toString().trim();
        if (fString.equals("staff1")) {
            Intent staffPage = new Intent(getApplicationContext(), StaffLogin.class);
            startActivity(staffPage);
        } else {
            mAuth.signInWithEmailAndPassword(fString, sString)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(GuestLogin.this, "Authentication failed.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
