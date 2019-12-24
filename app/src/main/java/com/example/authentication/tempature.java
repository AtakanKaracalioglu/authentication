package com.example.authentication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.Random;

public class tempature extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series1;

    public String temp_str = "TEMPERATURE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tempature);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button update = (Button) findViewById(R.id.update_but);
        final GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle(temp_str);
        graph.setTitleTextSize(50);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double x, y;
                x = 0;

                series1 = new LineGraphSeries<>();


                for (int i = 0; i < gettempdata.lst.size(); i++) {
                    x = x + 1;
                    BigInteger intt = new BigInteger(gettempdata.lst.get(i));
                    y = intt.doubleValue();
                    series1.appendData(new DataPoint(x, y), true, 100);

                }

                GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
                gridLabel.setHorizontalAxisTitle("Time~[s]");
                gridLabel.setVerticalAxisTitle("[°C]");
                // set manual X bounds
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(-10);
                graph.getViewport().setMaxY(40);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(35);
                // enable scaling and scrolling
                graph.getViewport().setScalable(true);
                graph.getViewport().setScalableY(true);


                graph.setCursorMode(true);
                graph.setClickable(true);

                graph.addSeries(series1);
                series1.setAnimated(true);
                graph.setTitle(temp_str);
                graph.setTitleTextSize(50);
                //graph.setTitleColor(75);
                // graph.getViewport().setScalable(true);
                // Bitmap bitmap = graph.takeSnapshot();
            }
        });



        Button load = (Button) findViewById(R.id.upload_but);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = graph.takeSnapshot();
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/req_images");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "Image-" + n + ".jpg";
                File file = new File(myDir, fname);

                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }


        });

    }
}
