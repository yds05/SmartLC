package com.lifecalendar.dayview.cameraview;

import java.util.Calendar;

import android.content.Context;  

import android.util.AttributeSet;  

import android.util.Log;  

import android.view.MotionEvent;  

import android.widget.ScrollView;  


public class MyScrollView extends ScrollView {  

	private final String TAG = "DayView";
  public MyScrollView(Context context) {  

   super(context);  

   init();  

 }  

 public MyScrollView(Context context, AttributeSet attrs) {  

   super(context, attrs);  

   init();  
 }  

 public MyScrollView(Context context, AttributeSet attrs, int defStyle) {  

  super(context, attrs, defStyle);  

   init();  

 }  

  /**  

21   * upper than api level 1.6, we should use setScrollbarFadingEnabled(false)  

22   * to show scrollbar  

23   */ 

  // protected void init() {  

  // super.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_INSET);  

  // super.setScrollbarFadingEnabled(false);  

  // mTouchScrollBar = false;  

  // }  

  protected void init() {  

 //  super.setVerticalFadingEdgeEnabled(true);  

   super.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_INSET);  
   super.setScrollbarFadingEnabled(false);

   mTouchScrollBar = false;  

  }  

  // a flag that we touched scrollbar and not release yet.  

  protected boolean mTouchScrollBar;  

  protected float mLastMoveY;  

  @Override 

  public boolean dispatchTouchEvent(MotionEvent ev) {  

   if (!isVerticalScrollBarEnabled()) {  

    return super.dispatchTouchEvent(ev);  

   }  
  
   
   System.out.println("scrollview touch called by time: " + Calendar.MINUTE);
   
   int verticalScrollbarWidth = getVerticalScrollbarWidth();  

   switch (ev.getAction()) {  

   case MotionEvent.ACTION_DOWN:  
	   System.out.println("scrollview ACTION_DOWN called by time: " + Calendar.MINUTE);
    if (ev.getX() > getWidth() - verticalScrollbarWidth) {  

     // intercept touch event, during a down event in scrollbar area  

     // and a up/cancel event later.  

     mTouchScrollBar = true;  

     mLastMoveY = (int) ev.getY();  

     return true;  

    }  

    break;  

   case MotionEvent.ACTION_MOVE:  
	   System.out.println("scrollview ACTION_MOVE called by time: " + Calendar.MINUTE);
    if (mTouchScrollBar) {  

     int scrollY = (int) (getScrollY() + ev.getY() - mLastMoveY);  

     scrollY = Math.min(scrollY, computeVerticalScrollRange()  

       - getHeight() + getPaddingBottom() + getPaddingTop());  

     Log.v(getClass().getName(), computeVerticalScrollRange() + "," 

       + scrollY);  

     scrollY = Math.max(scrollY, 0);  


     scrollTo(getScrollX(), scrollY);  

     mLastMoveY = ev.getY();  

     return true;  

    }  

    break;  

   case MotionEvent.ACTION_UP:  

   case MotionEvent.ACTION_CANCEL:  
	   System.out.println("scrollview ACTION_CANCEL called by time: " + Calendar.MINUTE);
    if (mTouchScrollBar) {  

     mTouchScrollBar = false;  

     return true;  

    }  

    break;  

   default:  

    break;  

   }  

   return super.dispatchTouchEvent(ev);  
  }  

  @Override 
  public boolean onInterceptTouchEvent(MotionEvent ev) { 

     int action = ev.getAction(); 

     switch (action) { 

     case MotionEvent.ACTION_DOWN: 

         Log.d(TAG, "onInterceptTouchEvent action:ACTION_DOWN"); 

         break; 

     case MotionEvent.ACTION_MOVE: 

         Log.d(TAG, "onInterceptTouchEvent action:ACTION_MOVE"); 

         break; 

     case MotionEvent.ACTION_UP: 

         Log.d(TAG, "onInterceptTouchEvent action:ACTION_UP"); 

         break; 

     case MotionEvent.ACTION_CANCEL: 

         Log.d(TAG, "onInterceptTouchEvent action:ACTION_CANCEL"); 

         break; 

     } 

     return false; 

 } 


 } 