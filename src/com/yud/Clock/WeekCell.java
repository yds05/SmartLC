package com.yud.Clock;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class WeekCell {
	
	private Rect mBound = null;
	private String mContent = null;	 //Globalization week content 
	private Paint mTextPaint = new Paint(Paint.SUBPIXEL_TEXT_FLAG
            |Paint.ANTI_ALIAS_FLAG);	
	
	private Boolean isWeekend;
	
	private int dx, dy;
	
	public WeekCell(String content, Rect rect, float textSize, boolean bold) {
		mContent = content;
		mBound = rect;
		mTextPaint.setTextSize(textSize/*26f*/);
		mTextPaint.setColor(Color.BLACK);
		if(bold) mTextPaint.setFakeBoldText(true);
		
		dx = (int) mTextPaint.measureText(String.valueOf(mContent)) / 2;
		dy = (int) (-mTextPaint.ascent() + mTextPaint.descent()) / 2;
	}
	
	public void SetWeekend()
	{
		isWeekend = true;
		mTextPaint.setColor(0xdddd0000);
	}
	
	protected void draw(Canvas canvas) 
	{		
		canvas.drawText(mContent, mBound.centerX() - dx, mBound.centerY() + dy / 2, mTextPaint);
	}
	
}
