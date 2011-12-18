package com.lifecalendar.dayview;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyLifeViewActivity extends Activity implements OnTouchListener, OnGestureListener {
    private GestureDetector mGestureDetector;

    private MainViewManager mViewManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylifeview_layout);
        setupViews();
    }
    
    private void setupViews() {
        mGestureDetector = new GestureDetector(this);
        mViewManager = MainViewManager.getInstance();
        android.util.Log.d("TAG", "aaa");
        final LinearLayout layout1 = (LinearLayout) findViewById(R.id.layoutMyLife);
        final TextView tv1 = (TextView) layout1.findViewById(R.id.tv6);
        tv1.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        android.util.Log.d("TAG", "nnnnnnnn");
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        android.util.Log.d("TAG", "000000");
        try {
            if (mViewManager.getMotionState(e1, e2, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(0));
                if (null == mViewManager.getCurView(0)) {
                    mViewManager.processViews(getParent());
                    mViewManager.onRotateAnimation(0);
                    return true;
                }
                mViewManager.onRotateAnimation(0);
                return true;
            } else if (mViewManager.getMotionState(e2, e1, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(3));
                if (null == mViewManager.getCurView(3)) {
                    mViewManager.processViews(getParent());
                    mViewManager.onRotateAnimation(3);
                    return true;
                }
                mViewManager.onRotateAnimation(3);
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
