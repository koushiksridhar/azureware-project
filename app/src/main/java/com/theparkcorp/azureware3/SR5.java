package com.theparkcorp.azureware3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SR5 extends AppCompatActivity {

    public static String balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr5);

    }

    public void balance0 (View view){

        balance = "0";

        Intent intent = new Intent(this, SR7.class);
        startActivity(intent);

    }

    public void balance1 (View view){

        balance = "1";

        Intent intent = new Intent(this, SR7.class);
        startActivity(intent);

    }

    public void balance2 (View view){

        balance = "2";

        Intent intent = new Intent(this, SR7.class);
        startActivity(intent);

    }

    public void balance3 (View view){

        balance = "3";

        Intent intent = new Intent(this, SR7.class);
        startActivity(intent);

    }

    public void balance4 (View view){

        balance = "4";

        Intent intent = new Intent(this, SR7.class);
        startActivity(intent);

    }

}
