package com.hld.recordvideo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hld.recordlibrary.RecordVideoActivity;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_RECORDVIDEO=1;

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView=findViewById(R.id.textView);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RecordVideoActivity.class);
                startActivityForResult(intent,REQUEST_RECORDVIDEO);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_RECORDVIDEO&&resultCode==RESULT_OK){
            String filePath=data.getStringExtra("path");
            textView.setText("视频位置:"+filePath);

        }
    }
}
