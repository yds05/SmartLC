/*
 * Copyright (C) 2011 Life Calendar 
 * Author Yu Deshui <yudeshui2007@gmail.com>
*/

package com.yud.Clock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;

public class NavHeader {

	private Rect mBound = null;
	private String mContent = null;	 //Globalization week content 
	private Paint mTextPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);	
	private Paint mFillPaint = new Paint();
	
	int dx, dy;
	
	public NavHeader(String content, Rect rect, float textSize, boolean bold) {
		mContent = content;
		mBound = rect;
		mTextPaint.setTextSize(textSize/*26f*/);
		mTextPaint.setColor(Color.BLACK);
		if(bold) mTextPaint.setFakeBoldText(true);
		
		mFillPaint.setStyle(Style.FILL);
		mFillPaint.setColor(Color.GRAY);
		
		dx = (int) mTextPaint.measureText(String.valueOf(mContent)) / 2;
		dy = (int) (-mTextPaint.ascent() + mTextPaint.descent()) / 2;
	}
	
	protected void draw(Canvas canvas) 
	{		
		canvas.drawText(mContent, mBound.centerX() - dx, mBound.centerY() + dy / 2, mTextPaint);
		
		Path leftPath = new Path();
		leftPath.moveTo(108, 40);
		leftPath.lineTo(118, 30);
		leftPath.lineTo(118, 50);
		leftPath.close();
		
		Path rightPath = new Path();
		rightPath.moveTo(360, 40);
		rightPath.lineTo(350, 30);
		rightPath.lineTo(350, 50);
		rightPath.close();
		
		canvas.drawPath(leftPath, mFillPaint);
		canvas.drawPath(rightPath, mFillPaint);
	}
	
	public NavTouch touchHit(int x, int y) {
		//return mBound.contains(x, y);
		NavTouch result = NavTouch.NONE;
	
		//Left button hit
		if(x>108 && x < 118 && y>30 &&y<50)
		{
		   result = NavTouch.LEFT_HIT;
		}
		else if(x>350 && x<360 && y>30 && y<50)
		{
			result = NavTouch.RIGHT_HIT;
		}
		
		return result;
	}
}

