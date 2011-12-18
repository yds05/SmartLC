
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

public class AllViewActivity extends Activity implements OnTouchListener, OnGestureListener {
    private GestureDetector mGestureDetector;

    private MainViewManager mViewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allview_layout);
        setupViews();
    }

    private void setupViews() {
        mGestureDetector = new GestureDetector(this);
        mViewManager = MainViewManager.getInstance();
        android.util.Log.d("TAG", "aaa");
        final LinearLayout layout1 = (LinearLayout) findViewById(R.id.layoutAllView);
        final TextView tv1 = (TextView) layout1.findViewById(R.id.tv1);
        tv1.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        android.util.Log.d("TAG", "1111");
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (mViewManager.getMotionState(e1, e2, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(1));
                if (null == mViewManager.getCurView(1)) {
                    mViewManager.processViews(getParent());
                }
                mViewManager.onRotateAnimation(1);
                return true;
            } else if (mViewManager.getMotionState(e2, e1, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(4));
                if (null == mViewManager.getCurView(4)) {
                    mViewManager.processViews(getParent());
                }
                mViewManager.onRotateAnimation(4);
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
