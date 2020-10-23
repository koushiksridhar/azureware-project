package com.theparkcorp.azureware3;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.util.Date;
import java.util.List;

public class SREnd extends AppCompatActivity {
    AmazonDynamoDBClient ddbClient;
    DynamoDBMapper mapper;

    String date;
    String rig = SR1.rig;
    String speech = SR2.speech;
    String emotion = SR3.emotion;
    String mov = SR4.mov;
    String balance = SR5.balance;
    String uaccel = SR7.sleep;



    @Override
    @TargetApi(24)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srend);

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:6ed26c7c-c891-4639-89d6-f5d6ad1e1a80", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        date = DateFormat.getDateTimeInstance().format(new Date());

        final String stringSurvey = rig + "," +speech + "," +emotion + "," +mov + "," +balance + "," +uaccel ;
        // Initialize the Cognito Sync client
        CognitoSyncManager syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);

        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("TestResult");
        dataset.put("result", "myValue");
        dataset.synchronize(new DefaultSyncCallback() {

            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //Your handler code here
                Log.e("onSuccess", "Uploaded");
            }
        });


        ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.US_EAST_1));

        mapper = new DynamoDBMapper(ddbClient);

        Runnable runnable = new Runnable() {
            public void run() {
                //DynamoDB calls go here

                final SurveyData patientData = new SurveyData();
                patientData.setPatientID("123");
                patientData.setDate(date);
                patientData.setStringSurvey(stringSurvey);
                mapper.save(patientData);

            }
        };

        Thread mythread = new Thread(runnable);
        mythread.start();

    }

    public void endSurvey (View view){
        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);
    }

}
