# wx_record_video
模仿微信录制小视频<br/>
使用系统MediaRecorder进行录制
# 使用方法:
## 引入
<pre>

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
