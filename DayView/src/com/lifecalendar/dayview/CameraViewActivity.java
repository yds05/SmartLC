package com.lifecalendar.dayview;

import java.util.Calendar;

import com.lifecalendar.dayview.cameraview.ImageViewManager;
import com.lifecalendar.dayview.cameraview.MyScrollView;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CameraViewActivity extends Activity implements OnTouchListener, OnGestureListener {
    private GestureDetector mGestureDetector;

    private MainViewManager mViewManager;
    private AbsoluteLayout mLayoutGroup = null;  
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.cameraview_layout);
        setupViews();
    }

    private void setupViews() {
        mGestureDetector = new GestureDetector(this);
        mViewManager = MainViewManager.getInstance();
        android.util.Log.d("TAG", "aaa");
        //final LinearLayout layout1 = (LinearLayout) findViewById(R.id.layoutCameraView);
        //final TextView tv1 = (TextView) layout1.findViewById(R.id.tv3);
        //tv1.setOnTouchListener(this);
        
        mLayoutGroup = new AbsoluteLayout(this);  
        //FrameLayout.LayoutParams layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(480, 700, 0, 0);  
        
    	MyScrollView scr = new MyScrollView(this);
    	scr.addView(mLayoutGroup);

  //      setContentView(mLayoutGroup, layoutParams);
        setContentView(scr);
        
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = year + "/" + month + "/" + day;
        System.out.println("date to get call log is " + date);
        
        ImageViewManager imageviewmanager = new ImageViewManager();
        imageviewmanager.InitLayout(this, mLayoutGroup, getContentResolver(), getWindowManager(), date);
        
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        android.util.Log.d("TAG", "JJJJJ");
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        android.util.Log.d("TAG", "444");
        try {
            if (mViewManager.getMotionState(e1, e2, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(3));
                if (null == mViewManager.getCurView(3)) {
                    mViewManager.processViews(getParent());
                    mViewManager.onRotateAnimation(3);
                    return true;
                }
                mViewManager.onRotateAnimation(3);
                return true;
            } else if (mViewManager.getMotionState(e2, e1, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(1));
                if (null == mViewManager.getCurView(1)) {
                    mViewManager.processViews(getParent());
                    mViewManager.onRotateAnimation(1);
                    return true;
                }
                mViewManager.onRotateAnimation(1);
                return true;
            }
        } catch (Exception ex) {
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
