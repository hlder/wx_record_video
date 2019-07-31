package com.hld.recordvideo;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

public class PreviewVideoActivity extends AppCompatActivity {

    private VideoView videoView;

    private ImageView imgBack,imgFinish;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String filePath=getIntent().getStringExtra("filePath");
        if(filePath==null||"".equals(filePath)){
            return;
        }

        setContentView(R.layout.activity_preview_video);

        imgFinish=findViewById(R.id.imgFinish);

        imgBack=findViewById(R.id.imgBack);

        videoView=findViewById(R.id.videoView);

        videoView.setVideoPath(filePath);

        videoView.start();

        //监听视频播放完的代码
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });

        imgFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
