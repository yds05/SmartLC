package com.lifecalendar.dayview;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends ActivityGroup implements View.OnClickListener{
    private int mCurId = R.id.btnAllView;
    private MainViewManager mViewManager;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.TYPE_STATUS_BAR, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.main);
        mViewManager = MainViewManager.getInstance();
        mViewManager.setupViews(this);
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (mCurId == id) {
            return;
        }
        System.out.println("------" + id);
        mCurId = id;
        mViewManager.setCurBtnPos(id);
        mViewManager.processViews(this);
        mViewManager.onRotateAnimation(mViewManager.getCurBtnIndex(id));
    }
    
}
