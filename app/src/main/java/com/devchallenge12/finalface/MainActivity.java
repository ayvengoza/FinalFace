package com.devchallenge12.finalface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

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

    private RadioGroup mGlassGroup;
    private RadioGroup mMustacheGroup;
    private RadioGroup mLipsGroup;

    private RadioGroup.OnCheckedChangeListener mRadioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (group.getCheckedRadioButtonId()){
                case R.id.glassesGroup:
                    mMustacheGroup.check(0);
                    mLipsGroup.check(0);
                    break;
                case R.id.mustachesGroup:
                    mGlassGroup.check(0);
                    mLipsGroup.check(0);
                    break;
                case R.id.lipsGroup:
                    mGlassGroup.check(0);
                    mMustacheGroup.check(0);
                    break;
            }
        }
    };

    private Face mFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotoHolder = (LinearLayout)findViewById(R.id.photoHolder);

        mGlassGroup = (RadioGroup)findViewById(R.id.glassesGroup);
        mGlassGroup.setOnCheckedChangeListener(mRadioListener);
        mGlassGroup.check(0);
        mMustacheGroup = (RadioGroup)findViewById(R.id.mustachesGroup);
        mMustacheGroup.setOnCheckedChangeListener(mRadioListener);
        mMustacheGroup.check(0);
        mLipsGroup = (RadioGroup)findViewById(R.id.lipsGroup);
        mLipsGroup.setOnCheckedChangeListener(mRadioListener);
        mMustacheGroup.check(0);

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

    private Bitmap getArBitmap(ArObject ar){
        int resId = 0;
        switch (ar){
            case Glass1:
                resId = R.raw.glasses11;
                break;
            case Glass2:
                resId = R.raw.glasses4;
                break;
            case Mustache1:
                resId = R.raw.mustache1;
                break;
            case Mustache2:
                resId = R.raw.mustache2;
                break;
            case Lip1:
                resId = R.raw.lips1;
                break;
            case Lip2:
                resId = R.raw.lips2;
                break;
            case None:
                return null;
        }
        InputStream stream = getResources().openRawResource(resId);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        return bitmap;
    }

    private PointF getArPosition(Face face, ArObject ar){
        switch(ar){
            case Glass1:
            case Glass2:
                return getGlassPosition(face);
            case Mustache1:
            case Mustache2:
                return getMustagePosition(face);
            case Lip1:
            case Lip2:
                return getMustagePosition(face);
            default:
                return null;
        }

    }

    private PointF getGlassPosition(Face face){
        PointF left = new PointF();
        PointF right = new PointF();
        for(Landmark landMark : face.getLandmarks()){
            switch(landMark.getType()){
                case Landmark.LEFT_EYE:
                    left = landMark.getPosition();
                    break;
                case Landmark.RIGHT_EYE:
                    right = landMark.getPosition();
            }
        }
        return getMiddlePoint(left, right);
    }

    private PointF getMustagePosition(Face face){
        PointF left = new PointF();
        PointF right = new PointF();
        for(Landmark landMark : face.getLandmarks()){
            switch(landMark.getType()){
                case Landmark.LEFT_MOUTH:
                    left = landMark.getPosition();
                    break;
                case Landmark.RIGHT_MOUTH:
                    right = landMark.getPosition();
            }
        }
        return getMiddlePoint(left, right);
    }

    private PointF getMiddlePoint(PointF point1, PointF point2){
        float x = (point1.x + point2.x)/2;
        float y = (point1.y + point2.y)/2;
        return new PointF(x, y);
    }

}
