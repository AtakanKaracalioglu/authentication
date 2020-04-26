package com.example.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class rms extends AppCompatActivity {

    private LineGraphSeries<DataPoint> series1;
    private LineGraphSeries<DataPoint> series2;
    private LineGraphSeries<DataPoint> series3;

    public String rms_str = "RMS";
    public String original_str = "Audio Data";
    public String fft_str = "FFT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rms);
        Button update = (Button) findViewById(R.id.updat_but);
       // Button send = (Button) findViewById(R.id.send_but);
        final GraphView graph = (GraphView) findViewById(R.id.original);
        graph.setTitle(original_str);
        graph.setTitleTextSize(50);
        final GraphView rms_graph = (GraphView) findViewById(R.id.rms_graph);
        graph.setTitle(rms_str);
        graph.setTitleTextSize(50);
        final GraphView fft_graph = (GraphView) findViewById(R.id.fft);
        graph.setTitle(fft_str);
        graph.setTitleTextSize(50);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random();
                double[] input = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,14,13,12,11,10,9};
                double[] output = my_rms(input,3,1);
                Complex[] inp= new Complex[64];


                for(int l =0; l < 64; l++)
                {
                    inp[l] = new Complex(rand.nextInt(50),rand.nextInt(50));
                }

                double x, y,z,f;
                x = 0;

                series1 = new LineGraphSeries<>();
                series2= new LineGraphSeries<>();
                series3= new LineGraphSeries<>();


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
                FFT fftobj = new FFT();
                Complex[] out =fftobj.fft(inp);
                x=0;
                for (int i = 0; i < out.length; i++) {
                    x = x + 1;
                    f = out[i].abs();
                    //System.out.println(output[i]);

                    series3.appendData(new DataPoint(x, f), true, 100);
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

                //GridLabelRenderer gridLabel3 = fft_graph.getGridLabelRenderer();
                //gridLabel3.setHorizontalAxisTitle("Time~[s]");
                //gridLabel3.setVerticalAxisTitle("Magnitude");
                // set manual X bounds
               // fft_graph.getViewport().setYAxisBoundsManual(true);
                //fft_graph.getViewport().setMinY(-10);
                //fft_graph.getViewport().setMaxY(100);

                //fft_graph.getViewport().setXAxisBoundsManual(true);
                //fft_graph.getViewport().setMinX(0);
                //fft_graph.getViewport().setMaxX(inp.length);
                // enable scaling and scrolling
                //fft_graph.getViewport().setScalable(true);
                //fft_graph.getViewport().setScalableY(true);



                fft_graph.setClickable(true);
                fft_graph.addSeries(series3);
                series3.setAnimated(true);
                fft_graph.setTitle(fft_str);
                fft_graph.setTitleTextSize(35);
            }
        });
        /*
        send.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                //Do the communication
            }
        });
        */
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
    public static class FFT {

        // compute the FFT of x[], assuming its length n is a power of 2
        public static Complex[] fft(Complex[] x) {
            int n = x.length;

            // base case
            if (n == 1) return new Complex[]{x[0]};

            // radix 2 Cooley-Tukey FFT
            if (n % 2 != 0) {
                throw new IllegalArgumentException("n is not a power of 2");
            }

            // compute FFT of even terms
            Complex[] even = new Complex[n / 2];
            for (int k = 0; k < n / 2; k++) {
                even[k] = x[2 * k];
            }
            Complex[] evenFFT = fft(even);

            // compute FFT of odd terms
            Complex[] odd = even;  // reuse the array (to avoid n log n space)
            for (int k = 0; k < n / 2; k++) {
                odd[k] = x[2 * k + 1];
            }
            Complex[] oddFFT = fft(odd);

            // combine
            Complex[] y = new Complex[n];
            for (int k = 0; k < n / 2; k++) {
                double kth = -2 * k * Math.PI / n;
                Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
                y[k] = evenFFT[k].plus(wk.times(oddFFT[k]));
                y[k + n / 2] = evenFFT[k].minus(wk.times(oddFFT[k]));
            }
            return y;
        }


        // compute the inverse FFT of x[], assuming its length n is a power of 2
        public static Complex[] ifft(Complex[] x) {
            int n = x.length;
            Complex[] y = new Complex[n];

            // take conjugate
            for (int i = 0; i < n; i++) {
                y[i] = x[i].conjugate();
            }

            // compute forward FFT
            y = fft(y);

            // take conjugate again
            for (int i = 0; i < n; i++) {
                y[i] = y[i].conjugate();
            }

            // divide by n
            for (int i = 0; i < n; i++) {
                y[i] = y[i].scale(1.0 / n);
            }

            return y;

        }
    }
}
