package com.hld.recordlibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

public class DialogFractory {

    /**
     * 展示单按钮框
     */
    public static void showSingleButtonDialog(Context context, String message){
        showSingleButtonDialog(context,message,null);
    }
    public static void showSingleButtonDialog(Context context, String message, final View.OnClickListener onClickListener){
        showSingleButtonDialog(context,message,"确定",onClickListener);
    }
    public static void showSingleButtonDialog(Context context, String message,String btnStr, final View.OnClickListener onClickListener){
        final AlertDialog.Builder normalDialog =new AlertDialog.Builder(context);
        normalDialog.setCancelable(false);
        normalDialog.setMessage(""+message);
        normalDialog.setPositiveButton(""+btnStr,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        if(onClickListener!=null){
                            onClickListener.onClick(null);
                        }
                    }
                });
        normalDialog.show();
    }

}
