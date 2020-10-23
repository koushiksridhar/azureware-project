package com.theparkcorp.azureware3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SR2 extends AppCompatActivity {

    public static String speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr2);

    }

    public void speech0 (View view){

        speech = "0";

        Intent intent = new Intent(this, SR3.class);
        startActivity(intent);

    }

    public void speech1 (View view){

        speech = "1";

        Intent intent = new Intent(this, SR3.class);
        startActivity(intent);

    }

    public void speech2 (View view){

        speech = "2";

        Intent intent = new Intent(this, SR3.class);
        startActivity(intent);

    }

    public void speech3 (View view){

        speech = "3";

        Intent intent = new Intent(this, SR3.class);
        startActivity(intent);

    }

    public void speech4 (View view){

        speech = "4";

        Intent intent = new Intent(this, SR3.class);
        startActivity(intent);

    }

}
