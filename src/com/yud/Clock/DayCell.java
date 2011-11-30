/*
 * Copyright (C) 2011 Life Calendar 
 * Author Yu Deshui <yudeshui2007@gmail.com>
*/


package com.yud.Clock;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

public class DayCell{
	private static final String TAG = "Cell";
	private static final int MAXROW = 5;
	private static final int MAXCOLUMN = 6;
	
	protected Rect mBound = null;
	protected int mDayOfMonth = 1;	// from 1 to 31
	protected Paint mPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);
	
	protected Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	protected Paint mRectPaint = new Paint();
	
	protected ArrayList<String> mImageFileNames;
	
	private Boolean isToday = false;
	private Boolean isFocused = false;
	private Boolean leftTopOverlapped = false;
	private Boolean leftBottomOverlapped = false;
	private Boolean rightTopOverlapped = false;
	private Boolean rightBottomOverlapped = false;
	
	protected static final int MsgCode = 0xA5CF;
		
	int dx, dy;
	public DayCell(int dayOfMon, Rect rect, float textSize, boolean bold) {
		mDayOfMonth = dayOfMon;
		mBound = rect;
		
		mPaint.setTextSize(textSize/*26f*/);
		mPaint.setColor(Color.BLACK);
		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
		mPaint.setTypeface(font);
		if(bold) mPaint.setFakeBoldText(true);
		
		mLinePaint.setColor(Color.GRAY);		
		
		dx = (int) mPaint.measureText(String.valueOf(mDayOfMonth)) / 2;
		dy = (int) (-mPaint.ascent() + mPaint.descent()) / 2;
	}
	
	public DayCell(int dayOfMon, Rect rect, float textSize) {
		this(dayOfMon, rect, textSize, false);
	}
	
	public Boolean getHasImage()
	{
	   return this.mImageFileNames != null && this.mImageFileNames.size() > 0;
	}
	
	protected void draw(Canvas canvas) {
		if(isFocused)
		{
		   mPaint.setColor(Color.BLUE);
		}
		
		if(isToday)
		{
			mRectPaint.setColor(0xffdcdcdc);//Color.GRAY);
			canvas.drawRect(mBound, mRectPaint);
		}		
		
		if(!this.isFocused)
		{
			canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY()-dy, mPaint);
		}
		else
		{
			canvas.drawText(String.valueOf(mDayOfMonth), mBound.centerX() - dx, mBound.centerY(), mPaint);	
		}
	}	
	
	public int getDayOfMonth() {
		return mDayOfMonth;
	}
	
	public boolean hitTest(int x, int y) {
		return mBound.contains(x, y); 
	}
	
	public Rect getBound() {
		return mBound;
	}
	
	public void setImageFileNames(ArrayList<String> imageFileNames)
	{
		mImageFileNames = imageFileNames;
	}
	
	public void setToday()
	{
		this.isToday = true;
	}
	
	public String toString() {
		return String.valueOf(mDayOfMonth)+"("+mBound.toString()+")";
	}
	
	public void onTouch()
	{
		mPaint.setColor(Color.BLUE);
	    isFocused = true;
	}
}

