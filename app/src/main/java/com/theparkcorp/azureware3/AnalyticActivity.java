package com.theparkcorp.azureware3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AnalyticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void toButton0(View view){

        Intent intent = new Intent(this, Analytic0.class);
        startActivity(intent);

    }

    public void toButton1(View view){

        Intent intent = new Intent(this, Activity1.class);
        startActivity(intent);

    }

    public void toButton2(View view){

        Intent intent = new Intent(this, Analytic2.class);
        startActivity(intent);

    }

    public void toButton3(View view){

        Intent intent = new Intent(this, Activity3.class);
        startActivity(intent);

    }

    public void toButton4(View view){

        Intent intent = new Intent(this, Analytic4.class);
        startActivity(intent);

    }

}
