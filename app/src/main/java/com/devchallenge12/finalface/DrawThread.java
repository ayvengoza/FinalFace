package com.devchallenge12.finalface;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class DrawThread extends Thread {
    private SurfaceHolder mSurfaceHolder;
    private Bitmap mPhoto;
    private Bitmap mArObject;
    private Canvas mCanvas;
    private long lastTime;

    private boolean isFirstTime = true;

    public DrawThread(SurfaceHolder surfaceHolder, Resources resources){
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void run() {
        lastTime = System.currentTimeMillis();
        while(true) {
            update();
            draw();
            delay();
        }
    }

    private void update() {

    }

    private void draw() {
        if(mSurfaceHolder.getSurface().isValid()) {
            if(isFirstTime){
                isFirstTime = false;
            }

            try {
                mCanvas = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    if(mCanvas != null) {
                        mCanvas.drawColor(Color.WHITE);
                    }
                }
            } finally {
                if (mCanvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    private void delay() {
        long currentTime = System.currentTimeMillis();
        long period = 35;
        long drawTime = (currentTime - lastTime);
        if(period > drawTime) {
            try {
                sleep(period - drawTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lastTime = currentTime;
    }
}
