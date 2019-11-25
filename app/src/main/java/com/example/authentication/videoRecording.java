package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class videoRecording extends Activity {

    private Button mRecordView,mplayView;
    private VideoView mVideoView;
    private int requestCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_recording);

        mRecordView = (Button) findViewById(R.id.rec_but);
        mplayView = (Button) findViewById(R.id.play_but);
        mVideoView = (VideoView) findViewById(R.id.vid_view);

        mRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callVideoAppIntent = new Intent();
                callVideoAppIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(callVideoAppIntent, requestCode);

            }
        });

        mplayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.start();
            }
        });
    }
    protected void onActivityResult(int requestCodee, int resultCode, Intent data)
    {
        if(requestCodee == requestCode && resultCode == RESULT_OK)
        {
            Uri videoUri = data.getData();
            mVideoView.setVideoURI(videoUri);
        }
    }
}
