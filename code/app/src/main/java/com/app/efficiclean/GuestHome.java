package com.app.efficiclean;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class GuestHome extends AppCompatActivity {

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home);
        this.alertDialog = new AlertDialog.Builder(GuestHome.this).create();
    }

    public void pleaseServiceButtonClick(View view) {
        this.alertDialog.setMessage("Thank you! Your room has been added to the queue and will be serviced shortly.");
        this.alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        this.alertDialog.show();
    }

    public void doNotDisturbButtonClick(View view) {
        this.alertDialog.setMessage("Your room has been marked 'Do not disturb'. If you would like your room to be cleaned, click 'Please service my room'.");
        this.alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        this.alertDialog.show();
    }

    public void checkingOutButtonClick(View view) {
        this.alertDialog.setMessage("Thank you for using EfficiClean! We hope you enjoyed your stay.");
        this.alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        this.alertDialog.show();
    }
}
