package com.theparkcorp.azureware3;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.cognito.exceptions.DataAccessNotAuthorizedException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private static String TAG = "RESULTACTIVITY";
    private static int finalZ;
    private static int finalResult;
    private TextView tremRat;
    Button ShareButton;
    private static String TremString;
    private File file;
    public static BufferedWriter bw = null;
    private static Context context;
    File filelocation;

    AmazonDynamoDBClient ddbClient;
    DynamoDBMapper mapper;

    String date2;
    double Treminit;
    String trem2;

    private static int tremPlace;

    @TargetApi(24)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ResultActivity.context = getApplicationContext();

        tremRat = (TextView) findViewById(R.id.tremRat);
        tremRat.setText("---");
        disResult(finalZ);

        date2 = DateFormat.getDateTimeInstance().format(new Date());

        tremPlace = DataActivity.getResult(Treminit);

        switch (tremPlace){
            case 0:
                trem2 = "0";
                break;
            case 1:
                trem2 = "1";
                break;
            case 2:
                trem2 = "2";
                break;
            case 3:
                trem2 = "3";
                break;
            case 4:
                trem2 = "4";
                break;
            default:
                trem2 = "Invalid, needs to be fixed";
        }


        ShareButton = (Button) findViewById(R.id.share);
        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Attach CSV file in email

                File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "AzureWare.csv");
                if (!filelocation.exists() || !filelocation.canRead()) {
                    return;
                }

                Uri uri = Uri.fromFile(filelocation);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                String shareBody = ("This is my result:  " + TremString);
                String shareSub = ("Today's AzureWare Result");
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share Using"));
            }
        });

        Uri csv = Uri.fromFile(filelocation);
        if (csv == null) {
            Toast.makeText(this, "Error creating share thing", Toast.LENGTH_LONG).show();
        } else {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/csv");
            sharingIntent.setType("text/comma_separated_values");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, csv);
            startActivity(Intent.createChooser(sharingIntent, "Send To..."));
        }

        // Initialize the Amazon Cognito credentials provider
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:b56a27ab-1ccf-4602-a145-e3c2a7705a52", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );

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

                    final PatientData patientData = new PatientData();
                    patientData.setPatientID("123");
                    patientData.setDate(date2);
                    patientData.setTremorRating(trem2);
                    mapper.save(patientData);

                }
            };

            Thread mythread = new Thread(runnable);
            mythread.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    private void disResult(int TremorRating) {

        finalZ = DataActivity.getResult(TremorRating);

        switch (finalZ) {
            case 0:
                tremRat.setText("0, No Tremor Exists");
                TremString = "0, No Tremor Exists";

                // Pass the tremor data to write to a CSV file
                writetofile("0");
                break;
            case 1:
                tremRat.setText("1, A slight tremor is present.");
                TremString = "1, A slight tremor is present.";

                // Pass the tremor data to write to a CSV file
                writetofile("1");
                break;
            case 2:
                tremRat.setText("2, A mild tremor is present.");
                TremString = "2, A mild tremor is present.";
                // Pass the tremor data to write to a CSV file
                writetofile("2");
                break;
            case 3:
                tremRat.setText("3, A moderate tremor is present");
                TremString = "3, A moderate tremor is present";
                // Pass the tremor data to write to a CSV file
                writetofile("3");
                break;
            case 4:
                tremRat.setText("4, An acute tremor is present");
                TremString = "4, An acute tremor is present";
                // Pass the tremor data to write to a CSV file
                writetofile("4");
                break;
        }
    }

    // Create and write list to CSV file
    @TargetApi(24)
    private void writetofile(String tremor_data) {

        Log.i("Log", "In Write to File");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "AzureWare.csv");
        Log.e("File Location", filelocation.toString());

        Date cDate = new Date();
        String date = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        String data_to_file = date + "," + tremor_data;

        try {
            filelocation.createNewFile();
            try {
                FileOutputStream out = new FileOutputStream(filelocation, true);
                out.write(data_to_file.getBytes());
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void toNextScreen(View view) {


        Intent intent = new Intent(this, AnalyticActivity.class);
        startActivity(intent);

    }

    public void toHomeScreen(View view) {

        Intent intent = new Intent(this, Main2.class);
        startActivity(intent);

    }

    public void SeeGraph(View view) {

        Intent intent = new Intent(this, GraphViewActivity.class);
        startActivity(intent);
    }

}


