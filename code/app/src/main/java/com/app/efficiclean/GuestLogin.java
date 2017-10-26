package com.app.efficiclean;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;

public class GuestLogin extends AppCompatActivity {

    private EditText forename;
    private EditText surname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_login);
    }

    public void loginButtonClick(View view) {
        this.forename = (EditText) findViewById(R.id.etForename);
        this.surname = (EditText) findViewById(R.id.etSurname);
        if (this.forename.getText().toString().equals("Joe") && this.surname.getText().toString().equals("Bloggs")) {
            Intent guest = new Intent(getApplicationContext(), GuestHome.class);
            startActivity(guest);
        } else if (this.forename.getText().toString().equals("morans") && this.surname.getText().toString().equals("staff")) {
            Intent staff = new Intent (getApplicationContext(), StaffHome.class);
            startActivity(staff);
        }
    }

}
