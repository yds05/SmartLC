/*
 * Copyright (C) 2011 Life Calendar 
 * Author Yu Deshui <yudeshui2007@gmail.com>
*/

package com.yud.Clock;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

public class MonthActivity extends Activity{
	private MonthView mMonthView = null;
	private int currentMonth = 0;
	private int currentYear = 0;	
	private Calendar mCalendar;
	private Calendar mPrevCalendar;
	private Calendar mNextCalendar;
	
	private ScrollLayout mScrollLayout;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	try
    	{
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        
	        mScrollLayout = (ScrollLayout)findViewById(R.id.scrollLayout);
	        mPrevCalendar = Calendar.getInstance();
	        mPrevCalendar.add(Calendar.MONTH, -1);
	        
	        mNextCalendar = Calendar.getInstance();
	        mNextCalendar.add(Calendar.MONTH, 1);
	        
	        //Add event listener, use anonimous listener to handle event callback
	        mScrollLayout.addEventListener(new ScrollLayoutListener()
	        {
	        	public void OnBoundary(ScrollLayoutEvent e)
	        	{
	        	    ScrollLayout sl = (ScrollLayout)e.getSource();
	        	    int curScreen = sl.getCurScreen();
	        	    int childCount = sl.getChildCount();
	        	    if(curScreen == 0)
	        	    {
	        	    	mPrevCalendar.add(Calendar.MONTH, -1);
	        	    	MonthView curMonth = new MonthView(MonthActivity.this, mPrevCalendar, null);
	        	    	
	        	    	mScrollLayout.DecreScreenOffset();
	        	    	
	        	    	mScrollLayout.addView(curMonth, 0);
	        	    	
	        	    	mScrollLayout.invalidate();
	        	    }
	        	    if((curScreen - mScrollLayout.getScreenOffset()) == (childCount - 1))
	        	    {
	        	    	mNextCalendar.add(Calendar.MONTH, 1);
	        	    	MonthView curMonth = new MonthView(MonthActivity.this, mNextCalendar, null);
	    				mScrollLayout.addView(curMonth);	        	    	
	        	    }
	        	}
	        });
	        
	        mCalendar = Calendar.getInstance();
	        currentYear = mCalendar.get(Calendar.YEAR);
	        currentMonth = mCalendar.get(Calendar.MONTH);
	        
	        LayoutParams p = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
	        
	        //Add previous month in the scroll layout view
	        moveCalendar(Prev);	        
			for (int i = 0; i <= 2; i++) {
				if (i > 0) {
					moveCalendar(Next);
				}

				MonthView curMonth = new MonthView(this, mCalendar, null);
				mScrollLayout.addView(curMonth, i);
			}
	        
			mScrollLayout.setToScreen(1);

	        //startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(null, CalendarActivity.MIME_TYPE));
	        // 1) start calendar view
	        //startActivityForResult(new Intent(Intent.ACTION_PICK).setDataAndType(null, CalendarActivity.MIME_TYPE), 100);	
	        // 2) implement your own onActivityResult method to handle returned date
    	}
    	catch(Exception e)
    	{
    		Log.i("Exception", "Exception in MonthActivity = " + e);
    	}    	
    }
    
    private final int Prev = 0;
    private final int Next = 1;
    
    private void moveCalendar(int direction)
    {
    	switch(direction)
    	{
    		case Next:
		    	if(currentMonth == 11) 
				{
					currentMonth = 0;
					++currentYear;
				}
				else
				{
					++currentMonth;
				}
		    	//mCalendar.add(Calendar.MONTH, 1);
		    	break;
    		case Prev:
    			if(currentMonth == 0)
    	    	{
    	    		currentMonth = 11;
    	    		--currentYear;
    	    	}
    	    	else
    	    	{
    	    		--currentMonth;
    	    	}
    			//mCalendar.add(Calendar.MONTH, -1);
    			break;
    		default:
    			//error
    			break;
    	}
    	
    	mCalendar.set(Calendar.YEAR, currentYear);
		mCalendar.set(Calendar.MONTH, currentMonth);
    }    
    
}