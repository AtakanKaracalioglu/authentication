package com.example.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class rms extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series1;
    private LineGraphSeries<DataPoint> series2;

    public String rms_str = "RMS";
    public String original_str = "Audio Data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rms);
        Button update = (Button) findViewById(R.id.updat_but);
        Button send = (Button) findViewById(R.id.send_but);
        final GraphView graph = (GraphView) findViewById(R.id.original);
        graph.setTitle(original_str);
        graph.setTitleTextSize(50);
        final GraphView rms_graph = (GraphView) findViewById(R.id.rms_graph);
        graph.setTitle(rms_str);
        graph.setTitleTextSize(50);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] input = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,14,13,12,11,10,9};
                double[] output = my_rms(input,3,1);
                double x, y,z;
                x = 0;

                series1 = new LineGraphSeries<>();
                series2= new LineGraphSeries<>();


                for (int i = 0; i < input.length; i++) {
                    x = x + 1;
                    y = input[i];
                    series1.appendData(new DataPoint(x, y), true, 100);
                }
                x=0;
                for (int i = 0; i < output.length; i++) {
                    x = x + 1;
                    z = output[i];
                    //System.out.println(output[i]);

                    series2.appendData(new DataPoint(x, z), true, 100);
                }

                GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle("Time~[s]");
                gridLabel.setVerticalAxisTitle("Raw Data");
                // set manual X bounds
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(-10);
                graph.getViewport().setMaxY(40);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(input.length);
                // enable scaling and scrolling
                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);


                graph.setCursorMode(true);
                graph.setClickable(true);

                graph.addSeries(series1);
                series1.setAnimated(true);
                graph.setTitle(original_str);
                graph.setTitleTextSize(50);

                GridLabelRenderer gridLabel2 = rms_graph.getGridLabelRenderer();
                gridLabel2.setHorizontalAxisTitle("Time~[s]");
                gridLabel2.setVerticalAxisTitle("Rms Data");
                // set manual X bounds
                rms_graph.getViewport().setYAxisBoundsManual(true);
                rms_graph.getViewport().setMinY(0);
                rms_graph.getViewport().setMaxY(20);

                rms_graph.getViewport().setXAxisBoundsManual(true);
                rms_graph.getViewport().setMinX(0);
                rms_graph.getViewport().setMaxX(output.length);
                // enable scaling and scrolling
                rms_graph.getViewport().setScalable(true);
                rms_graph.getViewport().setScalableY(true);


                rms_graph.setCursorMode(true);
                rms_graph.setClickable(true);

                rms_graph.addSeries(series2);
                series2.setAnimated(true);
                rms_graph.setTitle(rms_str);
                rms_graph.setTitleTextSize(50);
                //graph.setTitleColor(75);
                // graph.getViewport().setScalable(true);
                // Bitmap bitmap = graph.takeSnapshot();
            }
        });
        send.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                //Do the communication
            }
        });

    }
    //Calculate Rms
    public static  double[] my_rms(double[] input,int windowlength,int overlap)
    {
        int delta = windowlength - overlap;
        int index = 0;
        int counter =0;
        double[] dummy = new double[input.length];
        double[] output = new double[input.length];
        //Square the content of the integer array
        for(int i=0; i < input.length; i++)
        {
            dummy[i] = input[i]*input[i];
        }
        //System.out.print(dummy[2]);
        for(int m=0; m< input.length;m = m+delta)
        {

            if(m+windowlength-1 < input.length)
            {
                double dummy_mean = 0;
                for(int k=index; k< index+windowlength; k++)
                {
                    dummy_mean = dummy_mean + dummy[k]/windowlength;
                }
                output[counter] = Math.sqrt(dummy_mean);
            }
            index = index + delta;
            counter++;
        }

        return output;

    }
}
