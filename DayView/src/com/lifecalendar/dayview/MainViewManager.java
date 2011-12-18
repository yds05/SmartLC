package com.lifecalendar.dayview;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainViewManager {
    private static final int FLING_MIN_DISTANCE = 120;

    private static final int FLING_MIN_VELOCITY = 200;
    
    private final static int DISTENCE_X = 240, DISTENCE_Y = 0;

    private final static int ROTATE_ANIMATION_DURATION = 300;
    
    private final static int DEFAULT_FLING_ANGLE = 30;
    
    public final static Class<?>[] sActivityClasses = new Class[]{
            AllViewActivity.class, EventViewActivity.class, CameraViewActivity.class, MessageViewActivity.class, CallViewActivity.class, MyLifeViewActivity.class
    };

    public final static int[] sResIds = new int[]{
            R.id.btnAllView, R.id.btnEventView, R.id.btnCameraView, R.id.btnMessageView, R.id.btnCallView, R.id.btnMyLifeView
    };

    public final static String[] sActivityIds = new String[]{
            "AllViewActivity", "EventViewActivity", "CameraViewActivity", "MessageViewActivity", "CallViewActivity", "MyLifeViewActivity"
    };

    private int mPreBtnPos = 0, mCurBtnPos = 0;

    private RelativeLayout mViewContainer;

    private View mPreView;

    private View[] mCurView = new View[sResIds.length];

    private LinearLayout[] mBtns = new LinearLayout[sResIds.length];
    
    private ImageButton mBtnLastDay, mBtnNextDay;
    
    private static View mainView, bottomView, topView;

    private static MainViewManager mInstance = new MainViewManager();

    private MainViewManager() {
    }

    public static MainViewManager getInstance() {
        return mInstance;
    }
   
    public int getCurBtnId(int index) {
        return sResIds[index];
    }
    
    public void setCurBtnPos(int rid) {
        mCurBtnPos = getCurBtnIndex(rid);
    }
    
    public View getCurView(int index) {
        return mCurView[index];
    }
    
    public void setupViews(Context context) {
    	LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(context); 
    	//LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE); 
    	mainView = inflater.inflate(R.layout.main, null);
        mViewContainer = (RelativeLayout) mainView.findViewById(R.id.body);
    	//mViewContainer = (RelativeLayout) ((Activity) context).findViewById(R.id.body);
        
        bottomView = inflater.inflate(R.layout.bottom_buttons, (RelativeLayout)mainView, true);
        final LinearLayout[] btns = mBtns;
        for (int i = 0; i < btns.length; i++) {
            btns[i] = (LinearLayout) bottomView.findViewById(sResIds[i]);
            btns[i].setOnClickListener((OnClickListener) context);
        }
        ((Activity)context).setContentView(bottomView);
        
        // 第一次启动时，默认跳转到第一个activity
        mCurView[0] = ((ActivityGroup) context).getLocalActivityManager().startActivity(
                sActivityIds[0],
                new Intent(context, sActivityClasses[0]).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView();
        mViewContainer.addView(mCurView[0]);
        mPreView = mCurView[0];
        mPreBtnPos = 0;
        btns[0].setBackgroundResource(R.drawable.btnclickedbackground);
        
        //Temp begin, set listener for lastday and nextday button
        //Will use XML to realize the function later
        topView = inflater.inflate(R.layout.top_navigator, (RelativeLayout)mainView, true);
        mBtnLastDay = (ImageButton)topView.findViewById(R.id.btnLastDay);
        mBtnNextDay = (ImageButton)topView.findViewById(R.id.btnNextDay);
        
        final TextView textview1 = (TextView)topView.findViewById(R.id.txtSetdate);
        ((Activity)context).setContentView(topView);
        
        mBtnLastDay.setOnTouchListener(new ImageButton.OnTouchListener(){    
            @Override   
            public boolean onTouch(View v, MotionEvent event) {    
                    if(event.getAction() == MotionEvent.ACTION_DOWN){    
                            //更改为按下时的背景图片    
                    	mBtnLastDay.setBackgroundResource(R.drawable.btnclickedlastday44);               	
                    	textview1.setText("Sun, October 20, 2011");
                    }else if(event.getAction() == MotionEvent.ACTION_UP){    
                            //改为抬起时的图片    
                    	mBtnLastDay.setBackgroundResource(R.drawable.btnlastday44);   
                    }    
                    return false;    
            }    
        });  
        
        mBtnNextDay.setOnTouchListener(new ImageButton.OnTouchListener(){    
            @Override   
            public boolean onTouch(View v, MotionEvent event) {    
                    if(event.getAction() == MotionEvent.ACTION_DOWN){    
                            //更改为按下时的背景图片    
                    	mBtnNextDay.setBackgroundResource(R.drawable.btnclickednextday44);  
                    	textview1.setText("Tue, October 22, 2011");
                    }else if(event.getAction() == MotionEvent.ACTION_UP){    
                            //改为抬起时的图片    
                    	mBtnNextDay.setBackgroundResource(R.drawable.btnnextday44);   
                    }    
                    return false;    
            }    
        }); 
        
        //Temp end
    }

    public int getCurBtnIndex(int rid) {
        final int length = sResIds.length;
        for (int i = 0; i < length; i++) {
            if (rid == sResIds[i]) {
                return i;
            }
        }
        return 0;
    }
    
    public int getCurBtnResid(){
        return sResIds[mCurBtnPos];
    }

    public boolean getMotionState(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
        return e1.getX() - e2.getX() > FLING_MIN_DISTANCE
        && Math.abs(velocityX) > FLING_MIN_VELOCITY
        && Math.abs(Math.toDegrees(Math.atan((e1.getY() - e2.getY())
                / (e1.getX() - e2.getX())))) < DEFAULT_FLING_ANGLE;
    }
    
    public void processViews(Context context) {
        mViewContainer.removeAllViews();
        final Intent intent = new Intent(context, sActivityClasses[mCurBtnPos]);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mCurView[mCurBtnPos] = ((ActivityGroup) context).getLocalActivityManager().startActivity(
                sActivityIds[mCurBtnPos], intent).getDecorView();
    }

    public void onRotateAnimation(int index) {
        if (mPreBtnPos > mCurBtnPos) {
            Rotate3d.rightRotate(mPreView, mCurView[index], DISTENCE_X, DISTENCE_Y,
                    ROTATE_ANIMATION_DURATION, new AnimListener());
        } else {
            Rotate3d.leftRotate(mPreView, mCurView[index], DISTENCE_X, DISTENCE_Y,
                    ROTATE_ANIMATION_DURATION, new AnimListener());
        }

        //Set the background when click bottom button
        mBtns[mCurBtnPos].setBackgroundResource(R.drawable.btnclickedbackground);
        mBtns[mPreBtnPos].setBackgroundResource(R.drawable.btnnoprogressbackground);
        
        mPreView = mCurView[index];
        mViewContainer.removeAllViews();
        mViewContainer.addView(mCurView[index]);
        mPreBtnPos = mCurBtnPos;
    }

    private final static class AnimListener implements Animation.AnimationListener {

        public void onAnimationEnd(Animation animation) {
            // 可以设置buttons的背景或者状态(是否可点击等)
        }

        public void onAnimationRepeat(Animation animation) {

        }

        public void onAnimationStart(Animation animation) {
            // 可以设置buttons的背景或者状态(是否可点击等)
        }
    }
}
