package com.hld.recordvideo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * 如何使用:
 * Intent intent=new Intent(this,RecordVideoActivity.class);
 * intent.putExtra("outFilePath",outFilePath);//设置视频保存的位置
 * startActivityForResult(intent,CODE);
 *
 * @Override
 * protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
 *     super.onActivityResult(requestCode, resultCode, data);
 *     if(requestCode==code&&resultCode==RESULT_OK){//录制成功
 *          //outFilePath...
 *     }
 * }
 * @author hld
 */
public class RecordVideoActivity extends AppCompatActivity {
    private final int REQUEST_PERMISSION_CODE=0;


    private Camera mCamera;

    private View chanageCamera;
    private LinearLayout layoutBack;
    private RecordButtonView recordVideo;
    private FrameLayout frameLayout;

    private int selectCameraId=0;

    private MediaRecorder mediarecorder;


    private CameraPreview cameraPreview;


    private String outFilePath="/sdcard/temp.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outFilePath=getIntent().getStringExtra("outFilePath");

        if(outFilePath==null){
            outFilePath="/sdcard/temp.mp4";
        }

        File file=new File(outFilePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        layoutBack=findViewById(R.id.layoutBack);
        chanageCamera=findViewById(R.id.chanageCamera);
        recordVideo=findViewById(R.id.recordVideo);

        frameLayout= findViewById(R.id.camera_preview);


        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
            if (!checkPermission()) { //没有或没有全部授权
                requestPermissions(); //请求权限
                return;
            }
        }

        init();

    }
    //检查权限
    private boolean checkPermission() {
        //是否有权限
        boolean haveCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean haveWritePermission = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean haveRecordPermission = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;


        return haveCameraPermission && haveWritePermission && haveRecordPermission;
    }

    // 请求所需权限
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissions() {
        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION_CODE);
    }


    private void init(){

        // 创建Camera实例
        mCamera = getCameraInstance();
        if(mCamera==null){
            Toast.makeText(this,"",Toast.LENGTH_SHORT).show();
            return;
        }
        // 创建预览视图，并作为Activity的内容

        isShowChanageButton();


        recordVideo.setOnTimeOverListener(new RecordButtonView.OnTimeOverListener() {
            @Override
            public void onTimeOver() {
                stopRecord();//停止录制
                Intent intent=new Intent(RecordVideoActivity.this,PreviewVideoActivity.class);
                intent.putExtra("filePath",outFilePath);
                startActivityForResult(intent,1);
            }
        });

        recordVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    startRecord();//开始录制
                    Log.d("dddd","开始录制");
                    recordVideo.doStartAnim();

                }else if(event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL){

                    Log.d("dddd","停止录制");

                    recordVideo.doStopAnim();
                    return false;
                }
                return true;
            }
        });


        chanageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectCameraId==0){
                    showBefPreview();
                }else{
                    showBacPreview();
                }
            }
        });
    }



    // 请求权限后会在这里回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE://授权返回
                boolean allowAllPermission = false;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//被拒绝授权
                        allowAllPermission = false;
                        break;
                    }
                    allowAllPermission = true;
                }
                if (allowAllPermission) {
                    init();
                    if(selectCameraId==0){
                        showBacPreview();
                    }else{
                        showBefPreview();
                    }
                } else {
                    DialogFractory.showSingleButtonDialog(this, "该功能需要授权方可使用", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
                break;
        }
    }



    private void initMediaRecorder(){
        mediarecorder=new MediaRecorder();


        mediarecorder.setCamera(mCamera);
        mediarecorder.reset();

        mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Set output file format
        mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        // 这两项需要放在setOutputFormat之后
        mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);



        int size[]=ToolUtils.getVideoSize(RecordVideoActivity.this,mCamera);
        mediarecorder.setVideoSize(size[0], size[1]);

        mediarecorder.setVideoFrameRate(30);
        mediarecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
        mediarecorder.setOrientationHint(90);
        //设置记录会话的最大持续时间（毫秒）
        mediarecorder.setMaxDuration(30 * 1000);
        mediarecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());


        mediarecorder.setOutputFile(outFilePath);


    }

    private void startRecord(){
        if(mediarecorder!=null){
            try {
                mCamera.unlock();
                // 准备录制
                mediarecorder.prepare();
                // 开始录制
                mediarecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecord(){
        if(mediarecorder!=null) {
            // 停止录制
            mediarecorder.stop();
            // 释放资源
            mediarecorder.release();
            mediarecorder = null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            data=new Intent();
            data.putExtra("path",outFilePath);
            setResult(RESULT_OK,data);
            finish();
        }else{
            if(selectCameraId==0){
                showBacPreview();
            }else{
                showBefPreview();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//大于Android 6.0
            if (checkPermission()) { //没有或没有全部授权
                if(selectCameraId==0){
                    showBacPreview();
                }else{
                    showBefPreview();
                }
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
        }
    }




    private void showBefPreview(){
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
        }
        mCamera = getBefCameraInstance();
        // 创建预览视图，并作为Activity的内容
        cameraPreview = new CameraPreview(this, mCamera,true);

        initMediaRecorder();
        frameLayout.removeAllViews();
        frameLayout.addView(cameraPreview);

        selectCameraId=1;
    }
    private void showBacPreview(){
        if(mCamera!=null){
            mCamera.stopPreview();
            mCamera.release();
        }

        mCamera = getCameraInstance();
        // 创建预览视图，并作为Activity的内容
        cameraPreview = new CameraPreview(this, mCamera,false);
        initMediaRecorder();
        frameLayout.removeAllViews();
        frameLayout.addView(cameraPreview);

        selectCameraId=0;
    }


    public Camera getBefCameraInstance(){//打开前置摄像头
        Camera c = null;
        try {
            c = Camera.open(1);
            c.setDisplayOrientation(90);
        } catch (Exception e){
            e.printStackTrace();
        }
        return c;  // returns null if camera is unavailable
    }

    public Camera getCameraInstance(){//打开后置摄像头
        Camera c = null;
        try {
            c = Camera.open(0);
            c.setDisplayOrientation(90);
        } catch (Exception e){
            e.printStackTrace();
        }
        return c;  // returns null if camera is unavailable
    }


    private void isShowChanageButton(){
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        if(numberOfCameras!=2){
            chanageCamera.setVisibility(View.GONE);
        }else{
            chanageCamera.setVisibility(View.VISIBLE);
        }
    }



}
