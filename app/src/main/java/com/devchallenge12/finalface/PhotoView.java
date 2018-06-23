package com.devchallenge12.finalface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PhotoView extends SurfaceView implements SurfaceHolder.Callback {

    private float width;
    private float height;
    private SurfaceHolder mSurfaceHolder;
    DrawThread thread;
    private Canvas mCanvas;
    private Bitmap mPhoto;
    private Bitmap mArObject;
    private RectF mDebugRect;
    private PointF mOldPoint;
    private PointF mCurrentPoint;
    private PointF mArPosition;


    public PhotoView(Context context, Bitmap photo) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mPhoto = photo;
        mDebugRect = new RectF(0, 0, 0, 0);
        mOldPoint = new PointF(0, 0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        thread = new DrawThread(mSurfaceHolder, getResources());
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        draw();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF currentPoint = new PointF(x, y);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mOldPoint.x = x;
                mOldPoint.y = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mOldPoint.x;
                float dy = y - mOldPoint.y;
                mDebugRect.left += dx;
                mDebugRect.top += dy;
                mDebugRect.right += dx;
                mDebugRect.bottom += dy;
                mOldPoint.x = x;
                mOldPoint.y = y;
                draw();
                break;
        }
        return true;
    }

    private void draw() {
        if(mSurfaceHolder.getSurface().isValid()) {
            try {
                mCanvas = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    if(mCanvas != null) {
                        Paint paint = new Paint();
                        mCanvas.drawColor(Color.WHITE);
                        mCanvas.drawBitmap(mPhoto, 0, 0, paint);
                        paint.setColor(Color.GREEN);
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(10f);
                        mCanvas.drawRect(mDebugRect, paint);

                    }
                }
            } finally {
                if (mCanvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                }
            }
        }
    }

    public void setDebugRect(RectF rect){
        mDebugRect = rect;
    }

    public void update(){
        surfaceChanged(mSurfaceHolder, 0, 0, 0);
    }
}
