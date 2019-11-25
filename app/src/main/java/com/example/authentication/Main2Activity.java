package com.example.authentication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {

    MediaRecorder mediaRecorder;
    final int REQUEST_PERMISSION_CODE = 1000;
    private String mFileName = null;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Button audio = (Button) findViewById(R.id.audio_but);
        final Button video = (Button) findViewById(R.id.video_but);
        final Button location = (Button) findViewById(R.id.gps_but);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.3gp";

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Main2Activity.this, MapsActivity.class);
                startActivity(intent);

            }
        });


        /*
        Button mybuton2 = (Button) findViewById(R.id.loc_but);

        mybuton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textView.append("\n" + location.getLatitude()+" "+location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                }, 10);
                return;
            }
        } else {
            configureButton();
        }


    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureButton();
                    return;
                }
        }
    }

    private void configureButton() {
        mybuton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
             //   locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

            }
        });
        */



        audio.setOnTouchListener(new View.OnTouchListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startRecording();


                }else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stopRecording();

                }

                return false;
            }

        });

        video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent vid_intent = new Intent(Main2Activity.this, videoRecording.class);
                startActivity(vid_intent);


            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording()
    {


            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);




            mediaRecorder.setOutputFile(mFileName);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try
        {
            mediaRecorder.prepare();

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        mediaRecorder.start();

    }
    private void stopRecording()
    {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

    }


}
