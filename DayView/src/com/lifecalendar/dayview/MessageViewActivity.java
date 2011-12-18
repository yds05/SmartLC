package com.lifecalendar.dayview;

import java.util.Calendar;

import com.lifecalendar.dayview.callview.CallViewListCursorAdapter;
import com.lifecalendar.dayview.callview.CallViewManager;
import com.lifecalendar.dayview.messageview.MessageViewListItemCursorAdapter;
import com.lifecalendar.dayview.messageview.MessageViewManager;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageViewActivity extends ListActivity implements OnTouchListener, OnGestureListener {
    private GestureDetector mGestureDetector;

    private MainViewManager mViewManager;
    private MessageViewManager messageViewManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messageview_layout);
        setupViews();
    }

    private void setupViews() {
        mGestureDetector = new GestureDetector(this);
        mViewManager = MainViewManager.getInstance();
        android.util.Log.d("TAG", "aaa");
        
        messageViewManager = new MessageViewManager(this);
        MessageViewListItemCursorAdapter cursoradapter;

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = year + "/" + month + "/" + day;
        System.out.println("date to get call log is " + date);
        //final ListView listInComingCall = (ListView) findViewById(R.id.incomingcalllist);
        //callViewManager.setListView(listInComingCall);  
        cursoradapter = messageViewManager.setupList("type=1", date);

        setListAdapter(cursoradapter);
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        android.util.Log.d("TAG", "mmmmmm");
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
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(4));
                if (null == mViewManager.getCurView(4)) {
                    mViewManager.processViews(getParent());
                    mViewManager.onRotateAnimation(4);
                    return true;
                }
                mViewManager.onRotateAnimation(4);
                return true;
            } else if (mViewManager.getMotionState(e2, e1, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(2));
                if (null == mViewManager.getCurView(2)) {
                    mViewManager.processViews(getParent());
                    mViewManager.onRotateAnimation(2);
                    return true;
                }
                mViewManager.onRotateAnimation(2);
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
