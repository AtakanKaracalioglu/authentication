package com.example.authentication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {

    MediaRecorder mediaRecorder;
    final int REQUEST_PERMISSION_CODE = 1000;
    private String mFileName = null;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public static List temparray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Button audio = (Button) findViewById(R.id.audio_but);
        final Button rms = (Button) findViewById(R.id.rms_button);
        final Button video = (Button) findViewById(R.id.video_but);
        final Button location = (Button) findViewById(R.id.gps_but);
        final Button database = (Button) findViewById(R.id.database_but);
        final Button temp_sensor = (Button) findViewById(R.id.tempp_but);

        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        findViewById(R.id.mainButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handleMainDialog();
            }
        });
        Button temp = (Button) findViewById(R.id.temp_but);

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent temp_int = new Intent(Main2Activity.this, tempature.class);
                startActivity(temp_int);
            }
        });


        final Button gettempdata = (Button) findViewById(R.id.tempdata_but);
        gettempdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent temperature = new Intent(Main2Activity.this, gettempdata.class);
                startActivity(temperature);
            }
        });
        rms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent temperature = new Intent(Main2Activity.this, rms.class);
                startActivity(temperature);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(location == null)
                {
                    Toast.makeText(Main2Activity.this,
                            "null", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(Main2Activity.this,
                            "location:" + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
                }
                HashMap<String, String> map = new HashMap<>();

                map.put("longitude", ""+location.getLongitude());
                map.put("latitude", ""+location.getLatitude());
                map.put("owner", ""+MainActivity.mail);

                Call<SendGpsResult> call = retrofitInterface.executeSendGps(map);

                call.enqueue(new Callback<SendGpsResult>() {
                    @Override
                    public void onResponse(Call<SendGpsResult> call, Response<SendGpsResult> response) {
                        if(response.code()==200){
                            Toast.makeText(Main2Activity.this,
                                    "Sent Successfully", Toast.LENGTH_LONG).show();
                        }
                        else if(response.code()==400){
                            Toast.makeText(Main2Activity.this,
                                    "Sent Failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SendGpsResult> call, Throwable t) {
                        Toast.makeText(Main2Activity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

                Intent intent = new Intent(Main2Activity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recorded_audio.3gp";

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
      /*
        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Main2Activity.this, MapsActivity.class);
                startActivity(intent);

            }
        });
*/
        database.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(Main2Activity.this, database.class);
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

        Button acc = (Button) findViewById(R.id.acc_but);
        acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, accelometer.class);
                startActivity(intent);
            }
        });

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
            Toast.makeText(this, "başarılı",Toast.LENGTH_LONG).show();

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
    private void handleMainDialog() {
        View view = getLayoutInflater().inflate(R.layout.send_text_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        Button sendBtn = view.findViewById(R.id.sendMsg);
        final EditText textEdit = view.findViewById(R.id.textEdit);
        final EditText ownerEdit = view.findViewById(R.id.ownerEdit);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map = new HashMap<>();

                map.put("text", textEdit.getText().toString());
                map.put("owner", ownerEdit.getText().toString());

                Call<SendResult> call = retrofitInterface.executeSendMsg(map);

                call.enqueue(new Callback<SendResult>() {
                    @Override
                    public void onResponse(Call<SendResult> call, Response<SendResult> response) {
                        if(response.code()==200){
                            Toast.makeText(Main2Activity.this,
                                    "Sent Successfully", Toast.LENGTH_LONG).show();
                        }
                        else if(response.code()==400){
                            Toast.makeText(Main2Activity.this,
                                    "Sent Failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SendResult> call, Throwable t) {
                        Toast.makeText(Main2Activity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
public boolean onCreateOptionsMenu(Menu menu)
{
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_main,menu);
    return  true;

}


}
