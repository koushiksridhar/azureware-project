package com.theparkcorp.azureware3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SR1 extends AppCompatActivity {

    public static String rig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr1);

    }

    public void rig0 (View view){

        rig = "0";

        Intent intent = new Intent(this, SR2.class);
        startActivity(intent);

    }

    public void rig1 (View view){

        rig = "1";

        Intent intent = new Intent(this, SR2.class);
        startActivity(intent);

    }

    public void rig2 (View view){

        rig = "2";

        Intent intent = new Intent(this, SR2.class);
        startActivity(intent);

    }

    public void rig3 (View view){

        rig = "3";

        Intent intent = new Intent(this, SR2.class);
        startActivity(intent);

    }

    public void rig4 (View view){

        rig = "4";

        Intent intent = new Intent(this, SR2.class);
        startActivity(intent);

    }

}
