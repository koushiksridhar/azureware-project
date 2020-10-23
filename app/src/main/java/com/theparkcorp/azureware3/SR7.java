package com.theparkcorp.azureware3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SR7 extends AppCompatActivity {

    public static String sleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr7);

    }

    public void sleep0 (View view){

        sleep = "0";

        Intent intent = new Intent(this, SREnd.class);
        startActivity(intent);

    }

    public void sleep1 (View view){

        sleep = "1";

        Intent intent = new Intent(this, SREnd.class);
        startActivity(intent);

    }

    public void sleep2 (View view){

        sleep = "2";

        Intent intent = new Intent(this, SREnd.class);
        startActivity(intent);

    }

    public void sleep3 (View view){

        sleep = "3";

        Intent intent = new Intent(this, SREnd.class);
        startActivity(intent);

    }

    public void sleep4 (View view){

        sleep = "4";

        Intent intent = new Intent(this, SREnd.class);
        startActivity(intent);

    }

}
