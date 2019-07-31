package com.hld.recordvideo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecordButtonView extends View {


    public RecordButtonView(Context context) {
        super(context);
        init(context);
    }

    public RecordButtonView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordButtonView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private OnTimeOverListener onTimeOverListener;


    private final int stepCount=10;



    private int startMinCirSize;
    private int startBigCirSize;

    private int endMinCirSize;
    private int endBigCirSize;

    private int nowMinCirSize;
    private int nowBigCirSize;


    private Paint minCirclePaint,bigCirclePaint,shanArcPaint;

    private int width,height;

    private float sweepAngle=0;


    private void init(Context context){
        Resources resources=context.getResources();

        startBigCirSize=(int)resources.getDimension(R.dimen.bigCirSize);
        startMinCirSize=(int)resources.getDimension(R.dimen.minCirSize);

        endMinCirSize=(int)resources.getDimension(R.dimen.endMinCirSize);
        endBigCirSize=(int)resources.getDimension(R.dimen.endBigCirSize);


//        startMinCirSize=80;
//        startBigCirSize=100;
//
//        endMinCirSize=40;
//        endBigCirSize=140;

        nowMinCirSize=startMinCirSize;
        nowBigCirSize=startBigCirSize;


        shanArcPaint=new Paint();
        bigCirclePaint=new Paint();
        minCirclePaint=new Paint();

        minCirclePaint.setColor(Color.WHITE);
        bigCirclePaint.setColor(Color.parseColor("#dee3dd"));

        shanArcPaint.setAntiAlias(true);
        shanArcPaint.setColor(Color.parseColor("#3ec781"));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(width==0||height==0){
            width=getWidth();
            height=getHeight();
        }

        canvas.drawCircle(width/2,height/2,nowBigCirSize/2,bigCirclePaint);

        //圆角矩形
        RectF rectF = new RectF(0,0,width,height);
        //从多少度开始,画多少度
        canvas.drawArc(rectF,-90,sweepAngle,true,shanArcPaint);



        canvas.drawCircle(width/2,height/2,(nowBigCirSize-15)/2,bigCirclePaint);

        canvas.drawCircle(width/2,height/2,nowMinCirSize/2,minCirclePaint);
    }

    private boolean cutStep(){
        if(nowMinCirSize>=startMinCirSize||nowBigCirSize<=startBigCirSize) {//动画结束
            return true;
        }

            float stepMin=(endMinCirSize-startMinCirSize)/stepCount;
        float stepBig=(endBigCirSize-startBigCirSize)/stepCount;

        nowMinCirSize= (int) (nowMinCirSize-stepMin);
        nowBigCirSize= (int) (nowBigCirSize-stepBig);

        if(nowMinCirSize>=startMinCirSize||nowBigCirSize<=startBigCirSize){//动画结束
            nowMinCirSize=startMinCirSize;
            nowBigCirSize=startBigCirSize;
            return true;
        }
        return false;
    }
    private boolean addStep(){
        if(nowMinCirSize<=endMinCirSize||nowBigCirSize>=endBigCirSize) {//动画结束
            return true;
        }

        float stepMin=(endMinCirSize-startMinCirSize)/stepCount;
        float stepBig=(endBigCirSize-startBigCirSize)/stepCount;

        nowMinCirSize= (int) (nowMinCirSize+stepMin);
        nowBigCirSize= (int) (nowBigCirSize+stepBig);

        if(nowMinCirSize<=endMinCirSize||nowBigCirSize>=endBigCirSize){//动画结束
            nowMinCirSize=endMinCirSize;
            nowBigCirSize=endBigCirSize;
            return true;
        }
        return false;
    }

    private boolean addTimeAnim(){
        int time=10000;//10秒
        int stepCount=time/10;
        float stepAngle=360f/stepCount;
        sweepAngle+=stepAngle;
        if(sweepAngle>359){
            sweepAngle=0;
            return true;
        }
        return false;
    }


    public void doStartAnim(){
        doAnim(true);
    }
    public void doStopAnim(){
        doAnim(false);
    }

    private boolean isAnimStart=false;

    private long lastTime=0;

    private Runnable runnable=new Runnable(){
        @Override
        public void run() {
            while(true){
                try {Thread.sleep(10);} catch (InterruptedException e) {}
                if(isAnimStart){
                    if(addStep()){//添加已经结束，执行倒计时动画
                        if(addTimeAnim()){
                            isAnimStart=false;//改为false，继续执行关闭动画
                        }
                    }
                }else{
                    sweepAngle=0;
                    if(cutStep()){
                        long nowTime=Calendar.getInstance().getTimeInMillis();
                        Log.d("dddd", "===============================1:"+this);

                        if((nowTime-lastTime)>1000){//超过1秒才能再次反馈,防止出现两次反馈
                            if(onTimeOverListener!=null){
                                onTimeOverListener.onTimeOver();
                            }
                            lastTime=nowTime;
                        }


                        break;
                    }
                }
                handler.sendEmptyMessage(1);
            }
        }
    };
    private ExecutorService pool=Executors.newSingleThreadExecutor();

    private void doAnim(final boolean isStart){
        this.isAnimStart=isStart;
        if(isStart){
            pool.execute(runnable);
        }
    }


    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };


    public void setOnTimeOverListener(OnTimeOverListener onTimeOverListener) {
        this.onTimeOverListener = onTimeOverListener;
    }

    public interface OnTimeOverListener{
        void onTimeOver();
    }

}
