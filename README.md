# 给个star支持一下
# wx_record_video
模仿微信录制小视频<br/>
使用系统MediaRecorder进行录制

效果预览:https://fir.im/sd36

# 使用方法:

## 引入
1.加入maven
<pre>
maven { url 'https://jitpack.io' }
</pre>
2.引入gradle
<pre>
implementation 'com.github.yuanfen7650:wx_record_video:1.0'//视频录制
</pre>
## 加入权限
<pre>
    < uses-permission android:name="android.permission.CAMERA" />
    < uses-feature android:name="android.hardware.camera" />
    < uses-permission android:name="android.permission.RECORD_AUDIO" />
    < uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    < uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
</pre>

## 启动
<pre>
Intent intent=new Intent(this,RecordVideoActivity.class);
intent.putExtra("outFilePath",outFilePath);//设置视频保存的位置
startActivityForResult(intent,CODE);
</pre>
## 返回
<pre>
 @Override
 protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     if(requestCode==code&&resultCode==RESULT_OK){//录制成功
          //outFilePath...
     }
 }
</pre>
