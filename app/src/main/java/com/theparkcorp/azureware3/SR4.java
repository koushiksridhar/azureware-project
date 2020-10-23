package com.theparkcorp.azureware3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SR4 extends AppCompatActivity {

    public static String mov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr4);

    }

    public void mov0 (View view){

        mov = "0";

        Intent intent = new Intent(this, SR5.class);
        startActivity(intent);

    }

    public void mov1 (View view){

        mov = "1";

        Intent intent = new Intent(this, SR5.class);
        startActivity(intent);

    }

    public void mov2 (View view){

        mov = "2";

        Intent intent = new Intent(this, SR5.class);
        startActivity(intent);

    }

    public void mov3 (View view){

        mov = "3";

        Intent intent = new Intent(this, SR5.class);
        startActivity(intent);

    }

    public void mov4 (View view){

        mov = "4";

        Intent intent = new Intent(this, SR5.class);
        startActivity(intent);

    }

}
