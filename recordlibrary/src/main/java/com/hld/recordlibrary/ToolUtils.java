package com.hld.recordlibrary;

import android.content.Context;
import android.hardware.Camera;
import android.util.DisplayMetrics;

import java.util.List;

public class ToolUtils {

    public static int[] getVideoSize(Context context,Camera mCamera){
//        mCamera.getSupportedPreviewFrameRates
        List<Camera.Size> listSizes=mCamera.getParameters().getSupportedPreviewSizes();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int height = dm.widthPixels;
        int width= dm.heightPixels;

        int cw=100000;
        int ch=100000;

        int cWidth=width;
        int cHeight=height;

        for(Camera.Size item:listSizes){

//            Log.d("dddd","摄像头像素:"+item.width+"*"+item.height);

            int tempW=Math.abs(item.width-width);
            int tempH=Math.abs(item.height-height);
            if(tempW<cw){
                cWidth=item.width;
                cw=tempW;
            }
            if(tempH<ch){
                cHeight=item.height;
                ch=tempH;
            }
        }
        int size[]=new int[2];
        size[0]=cWidth;
        size[1]=cHeight;
        return size;

    }
}
