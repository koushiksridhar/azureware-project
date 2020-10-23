package com.theparkcorp.azureware3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SR3 extends AppCompatActivity {

    public static String emotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr3);

    }

    public void emotion0 (View view){

        emotion = "0";

        Intent intent = new Intent(this, SR4.class);
        startActivity(intent);

    }

    public void emotion1 (View view){

        emotion = "1";

        Intent intent = new Intent(this, SR4.class);
        startActivity(intent);

    }

    public void emotion2 (View view){

        emotion = "2";

        Intent intent = new Intent(this, SR4.class);
        startActivity(intent);

    }

    public void emotion3 (View view){

        emotion = "3";

        Intent intent = new Intent(this, SR4.class);
        startActivity(intent);

    }

    public void emotion4 (View view){

        emotion = "4";

        Intent intent = new Intent(this, SR4.class);
        startActivity(intent);

    }

}
