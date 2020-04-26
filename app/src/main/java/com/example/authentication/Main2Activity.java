package com.example.authentication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {

    int N = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, N * 10);
    short[] buffer = new short[N];
    boolean stopped = false;

    Handler mHandler;
    Runnable mAction = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            audioRecord.startRecording();
            startRecording();
            mHandler.postDelayed(this, 100);
        }
    };
    MediaRecorder mediaRecorder;
    final int REQUEST_PERMISSION_CODE = 1000;
    private String mFileName = null;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    public static List temparray;
    public static String string_buffer ="";
    private StorageReference mStorage;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Button audio = (Button) findViewById(R.id.audio_but);
        final Button firebase = (Button) findViewById(R.id.firebase_but);
        final Button rms = (Button) findViewById(R.id.rms_button);
        final Button video = (Button) findViewById(R.id.video_but);
        final Button location = (Button) findViewById(R.id.gps_but);
        final Button database = (Button) findViewById(R.id.database_but);
        final Button temp_sensor = (Button) findViewById(R.id.tempp_but);
        mProgress = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference();

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
        firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firebase_int = new Intent(Main2Activity.this, firebase.class);
                startActivity(firebase_int);
            }
        });
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
                stopRecording();
               // Intent temperature = new Intent(Main2Activity.this, gettempdata.class);
                //startActivity(temperature);
            }
        });
        rms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent rms_int = new Intent(Main2Activity.this, rms.class);
                startActivity(rms_int);
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
                    //startRecording();
                    if(mHandler != null) return true;
                    mHandler = new Handler();
                    mHandler.postDelayed(mAction, 100);


                }else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    if(mHandler == null) return true;
                    mHandler.removeCallbacks(mAction);
                    mHandler = null;
                    audioRecord.stop();
                    //stopRecording();

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
        Toast.makeText(this, "girdim",Toast.LENGTH_LONG).show();

        try
        {
            Toast.makeText(this, "basliyom kayda",Toast.LENGTH_LONG).show();
            int bufferReadResult;

            bufferReadResult =  audioRecord.read(buffer, 0, N);

            //double[] values = new double[N];
            double rms = 0;
            for(int i = 0; i < buffer.length; i++)
            {
                rms += Math.pow((((double)buffer[i])/32768), 2);
            }
            rms = Math.sqrt(rms / buffer.length);

            string_buffer += rms;

            //Toast.makeText(this, Arrays.toString(buffer),Toast.LENGTH_LONG).show();
            Toast.makeText(this, string_buffer,Toast.LENGTH_LONG).show();

            /*
            while(!stopped)
            {
                //bufferReadResult = audioRecord.read(buffer, 0, 512);
                Toast.makeText(this, "short okudum",Toast.LENGTH_LONG).show();
            }
            */

        }
        catch(Exception e)
        {
            Toast.makeText(Main2Activity.this,
                    "olmadi", Toast.LENGTH_LONG).show();
            //wait(1000);
        }


        /*
        audioRecord.startRecording();

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
        */
    }
    private void stopRecording()
    {

        HashMap<String, String> map = new HashMap<>();

        map.put("rms", ""+string_buffer);

        Call<SendRmsResult> call = retrofitInterface.executeSendRms(map);
        call.enqueue(new Callback<SendRmsResult>() {
            @Override
            public void onResponse(Call<SendRmsResult> call, Response<SendRmsResult> response) {
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
            public void onFailure(Call<SendRmsResult> call, Throwable t) {
                Toast.makeText(Main2Activity.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        /*
        stopped = true;
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        */

    }
/*
    private void uploadAudio() {
        mProgress.setMessage("Uploading...");
        mProgress.show();
        StorageReference filepath = mStorage.child("Audio").child("new_audio.3gp");
        Uri uri = Uri.fromFile(new File(mFileName));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();

            }
        });

    }
*/
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
