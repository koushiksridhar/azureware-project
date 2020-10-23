package com.theparkcorp.azureware3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphViewActivity extends AppCompatActivity {

    LineGraphSeries<DataPoint> series;
    public double[] zArray = new double[2000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DataActivity myData = new DataActivity();
        zArray = myData.retArray();

        double y,x;

        x = 0;

        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        graph.setBackgroundColor(getResources().getColor(android.R.color.black));
        graph.setTitle("Tremor Values");

        for (int i = 0; i < 100; i++){
            x = x+1;
            y = zArray[i];
            series.appendData(new DataPoint(x,y),true, zArray.length);
        }
        graph.addSeries(series);
    }



}
