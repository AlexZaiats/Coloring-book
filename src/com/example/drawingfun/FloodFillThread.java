package com.example.drawingfun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class FloodFillThread extends Thread 
{
    ProgressDialog mProgressDialog;    Bitmap            mBitmap;
    int               mTargetColor;
    int            mNewColor;
    Bitmap colorfullBitmap;
    View view;

    
    public FloodFillThread(
           Bitmap bitmap, Bitmap colorfullBitmap, int newColor , View view ) 
    {
        mBitmap          = bitmap;
        this.colorfullBitmap           = colorfullBitmap;
    
        mNewColor          = newColor;

        this.view = view;
    }

    @Override
    public void run() 
    {         
        QueueLinearFloodFiller filler = 
           new QueueLinearFloodFiller(mBitmap, mTargetColor, mNewColor );
        
        filler.setTolerance(0);
        filler.setBitmap(colorfullBitmap);
        filler.floodFill(0, 0);
        
        handler.sendEmptyMessage(0);
    }

    private Handler handler = new Handler() 
    {
        @Override
        public void handleMessage(Message msg) 
        {
        	view.invalidate();
        }
    };
}