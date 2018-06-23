package com.devchallenge12.finalface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.LinearLayout;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private LinearLayout mPhotoHolder;
    private PhotoView mPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoHolder = (LinearLayout)findViewById(R.id.photoHolder);

        InputStream stream = getResources().openRawResource(R.raw.face);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);

        mPhotoView = new PhotoView(getApplicationContext(), bitmap);
        mPhotoHolder.addView(mPhotoView);


        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> faces = detector.detect(frame);

        for(int i = 0; i < faces.size(); i++){
            Log.d(TAG, "Detect " + i);
            List<Landmark> landmarks = faces.get(i).getLandmarks();
//            PointF point = faces.get(i).getPosition();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int widthP = mPhotoView.getWidth();
            int heightP = mPhotoView.getHeight();
            int widthL = mPhotoHolder.getWidth();
            int heightL = mPhotoHolder.getHeight();
            for(Landmark landmark : landmarks){
                Log.d(TAG, "LandMark " + landmark.getPosition().x + ", " + landmark.getPosition().y + ", " + landmark.getType());
                PointF point = landmark.getPosition();
                RectF rect = new RectF(point.x - 10, point.y - 10 , point.x + 10, point.y + 10);
                mPhotoView.setDebugRect(rect);
                mPhotoView.update();
            }
        }

        detector.release();
    }
}
