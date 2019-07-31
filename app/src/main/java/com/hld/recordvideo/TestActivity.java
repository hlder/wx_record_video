package com.hld.recordvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

//import org.bytedeco.javacpp.avcodec;
//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacv.FFmpegFrameRecorder;
//import org.bytedeco.javacv.FrameRecorder;
//import org.bytedeco.javacv.OpenCVFrameConverter;
//import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;

import java.io.File;


public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button=new Button(this);
        setContentView(button);

        button.setText("点击");
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    test();
//                } catch (FrameRecorder.Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });


    }



//    public void test() throws FrameRecorder.Exception {
//        String saveMp4name = "/sdcard/f1.flv"; //保存的视频名称
//        // 目录中所有的图片，都是jpg的，以1.jpg,2.jpg的方式，方便操作
//        String imagesPath = "/sdcard/temp/images"; // 图片集合的目录
//
//        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(saveMp4name,640,480);
////		recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // 28
//        recorder.setVideoCodec(avcodec.AV_CODEC_ID_FLV1); // 28
////		recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4); // 13
//        recorder.setFormat("flv");
//        //	recorder.setFormat("mov,mp4,m4a,3gp,3g2,mj2,h264,ogg,MPEG4");
//        recorder.setFrameRate(20);
//        recorder.setPixelFormat(0); // yuv420p
//        recorder.start();
//        //
//        OpenCVFrameConverter.ToIplImage conveter = new OpenCVFrameConverter.ToIplImage();
//        // 列出目录中所有的图片，都是jpg的，以1.jpg,2.jpg的方式，方便操作
//        File file = new File(imagesPath);
//        File[] flist = file.listFiles();
//        // 循环所有图片
//        for(int i = 0; i < flist.length; i++ ){
//            String fname = flist[i].getPath();
//            Log.d("dddd","fname:"+fname);
//            opencv_core.IplImage image = cvLoadImage(fname); // 非常吃内存！！
//            recorder.record(conveter.convert(image));
//            // 释放内存？ cvLoadImage(fname); // 非常吃内存！！
//            opencv_core.cvReleaseImage(image);
//        }
//        recorder.stop();
//        recorder.release();
//    }



}
