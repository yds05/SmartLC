/*
 * Copyright (C) 2011 Life Calendar 
 * Author Yu Deshui <yudeshui2007@gmail.com>
*/

package com.yud.Clock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MonthView extends View {
	private static int CELL_WIDTH = 58;
	private static int CELL_HEIGH = 53;
	private static int CELL_WEEK_HEIGH = 35;
	private static int NAVIGATION_HEIGH = 100;
	private static int NAVIGATION_WIDTH = 100;	
	private static int CELL_MARGIN_TOP = 10;
	private static int CELL_MARGIN_LEFT = 39;
	private static float CELL_TEXT_SIZE;

	private static final String TAG = "CalendarView";
	
	private static final int dayPerWeekCount = 7;
	private static final int weekPerMonthCount = 6;
	
	private Calendar mRightNow = null;
	
	private int currentMonth = 0;
	private int currentYear = 0;
	
	private DayCell mToday = null;
	private DayCell[][] mCells = new DayCell[weekPerMonthCount][dayPerWeekCount];
	private WeekCell[] mWeekCells = new WeekCell[dayPerWeekCount];
	private NavHeader mNavHeader = null;
	private OnCellTouchListener mOnCellTouchListener = null;
	
	//Initialize week string ids
	private int[] mWeekStringIds = new int[] {R.string.Sunday, R.string.Monday, 
			R.string.Tuesday, R.string.Wednesday, 
			R.string.Thursday, R.string.Friday, R.string.Saturday};
	
	//Initialize month string ids
	private int[] mMonthStringIds = new int[] {R.string.January, R.string.February,
			R.string.March, R.string.April, R.string.May, R.string.June, 
			R.string.July, R.string.August, R.string.September, R.string.October,
			R.string.November, R.string.December};
	
	private Paint mLinePaint;

	private HashMap<Integer, ArrayList<String>> mImageFileMap = new HashMap<Integer, ArrayList<String>>();
	private HashMap<Integer, ArrayList<ImagePair>> mImageIdMap = new HashMap<Integer, ArrayList<ImagePair>>();
	private HashMap<Integer, Rect> mDayRectMap = new HashMap<Integer, Rect>();
	
	private ContentResolver mConResolver;
	
	MonthDisplayHelper mHelper;
	Drawable mDecoration = null;
	
	private volatile boolean isImageLoaded = false;
	
	private final Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
		   switch(msg.what)
		   {
		   	case DayCell.MsgCode:
			    //Re-draw monthview 
		   		MonthView.this.invalidate();
			    break;
			}
		   super.handleMessage(msg);
		}
	};
	
	public interface OnCellTouchListener {
		public void onTouch(DayCell cell);
	}

	public MonthView(Context context) {
		this(context, null);		
	}

	public MonthView(Context context, AttributeSet attrs)
	{
		this(context, Calendar.getInstance(), attrs);	
	}
	
	public MonthView(Context context, Calendar rightNow, AttributeSet attrs) {
		this(context, rightNow, attrs, 0);		
	}

	public MonthView(Context context, Calendar rightNow, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDecoration = context.getResources().getDrawable(
				R.drawable.typeb_calendar_today);
		
		mConResolver = context.getContentResolver();		
		initCalendarView(context, rightNow);
	}

	private void initCalendarView(Context context, Calendar rightNow) {
		mRightNow = rightNow;
		// prepare static vars
		Resources res = getResources();
		
		CELL_WIDTH = (int) res.getDimension(R.dimen.cell_width);
		CELL_HEIGH = (int) res.getDimension(R.dimen.cell_heigh);
		CELL_WEEK_HEIGH = (int)res.getDimension(R.dimen.cell_week_heigh);
		
		CELL_MARGIN_TOP = (int) res.getDimension(R.dimen.cell_margin_top);
		CELL_MARGIN_LEFT = (int) res.getDimension(R.dimen.cell_margin_left);
		
		NAVIGATION_HEIGH = (int)res.getDimension(R.dimen.navigation_heigh);
		NAVIGATION_WIDTH = (int)res.getDimension(R.dimen.navigation_width);
		
		CELL_TEXT_SIZE = res.getDimension(R.dimen.cell_text_size);
		
		//TODO set background
		
		//Set line paint and color
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setColor(Color.GRAY);
		
		int year = currentYear = mRightNow.get(Calendar.YEAR);
		int month = currentMonth = mRightNow.get(Calendar.MONTH);
		int maxDayNumberOfMonth = mRightNow.getMaximum(Calendar.DAY_OF_MONTH);
		mHelper = new MonthDisplayHelper(mRightNow.get(Calendar.YEAR),
				mRightNow.get(Calendar.MONTH));

		// First day of current month, 00:00:00
		Date firstDayOfMonth = new Date(year - 1900, month, 1, 0, 0, 0);

		// Last day of current month, 23:59:59
		Date lastDayOfMonth = new Date(year - 1900, month, maxDayNumberOfMonth,
				23, 59, 59);

		// Read image from system content provider, filtered in current month
		String projection = "date_added > " + firstDayOfMonth.getTime() / 1000
				+ " AND date_added < " + lastDayOfMonth.getTime() / 1000;
		
		Cursor cursor = context.getContentResolver().query(
				Media.EXTERNAL_CONTENT_URI,
				//MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, 
				new String[] { 
						//Media.DISPLAY_NAME, Media.DESCRIPTION,
						Media.DATE_ADDED, Media.DATA, Media._ID}, 
						projection,
						null, // selectionArgs
						null); // sortOrder	
		
		 final String albumDir = Environment.getExternalStorageDirectory() + "/DCIM";

		 //Query external storage state
		 String status=Environment.getExternalStorageState();
		 
		 //Is SD card available?
         if(!status.equals(Environment.MEDIA_MOUNTED) && Calendar.getInstance().get(Calendar.MONTH) == month){  
            Toast.makeText(this.getContext(), "No SD Card available", Toast.LENGTH_SHORT).show();
        	return;
         }
		
		while (cursor != null && cursor.moveToNext()) {
//			String name = cursor.getString(cursor
//					.getColumnIndex(Media.DISPLAY_NAME));
//			String description = cursor.getString(cursor
//					.getColumnIndex(Media.DESCRIPTION));
			Long date = cursor.getLong(cursor.getColumnIndex(Media.DATE_ADDED));
			
			Integer mediaId = cursor.getInt(cursor.getColumnIndex(Media._ID));
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date * 1000);

			byte[] data = cursor.getBlob(cursor.getColumnIndex(Media.DATA));
			String imgFilePath = new String(data, 0, data.length - 1);
			
			
			int day = cal.get(Calendar.DAY_OF_MONTH);
			if (this.mImageFileMap.containsKey(day)) {
				(mImageFileMap.get(day)).add(imgFilePath);
			} else {
				ArrayList<String> val = new ArrayList<String>();
				val.add(imgFilePath);
				this.mImageFileMap.put(day, val);
			}
			
			//Only filter album images took by camera
			if(imgFilePath.contains(albumDir))
			{
				if (this.mImageIdMap.containsKey(day)) {
					(mImageIdMap.get(day)).add(new ImagePair(mediaId, day));
				} else {
					ArrayList<ImagePair> val = new ArrayList<ImagePair>();
					val.add(new ImagePair(mediaId, day));
					this.mImageIdMap.put(day, val);
				}
			}
		}
	}

	private void initCells() {
		class _calendar {
			public int day;
			public boolean thisMonth;

			public _calendar(int d, boolean b) {
				day = d;
				thisMonth = b;
			}

			public _calendar(int d) {
				this(d, false);
			}
		};
		
		_calendar tmp[][] = new _calendar[MonthView.weekPerMonthCount][MonthView.dayPerWeekCount];

		for (int i = 0; i < tmp.length; i++) {
			int n[] = mHelper.getDigitsForRow(i);
			for (int d = 0; d < n.length; d++) {
				if (mHelper.isWithinCurrentMonth(i, d))
					tmp[i][d] = new _calendar(n[d], true);
				else
					tmp[i][d] = new _calendar(n[d]);

			}
		}

		Calendar today = Calendar.getInstance();
		int thisDay = 0;
		mToday = null;
		if (mHelper.getYear() == today.get(Calendar.YEAR)
				&& mHelper.getMonth() == today.get(Calendar.MONTH)) {
			thisDay = today.get(Calendar.DAY_OF_MONTH);
		}
		
		// build navigation header		
		String title = String.format("%s %d", getResources().getString(mMonthStringIds[mHelper.getMonth()]), mHelper.getYear());
		
		Rect navBound = new Rect(CELL_MARGIN_LEFT, CELL_MARGIN_TOP, NAVIGATION_WIDTH
				+ CELL_MARGIN_LEFT, NAVIGATION_HEIGH + CELL_MARGIN_TOP);
		
		mNavHeader = new NavHeader(title, navBound, 30, false);
		
		int topMargin = CELL_MARGIN_TOP + NAVIGATION_HEIGH;
		
		// build week cells
		Rect weekBound = new Rect(CELL_MARGIN_LEFT, topMargin, CELL_WIDTH
				+ CELL_MARGIN_LEFT, CELL_WEEK_HEIGH + topMargin);
		for(int dayOfWeek = 0; dayOfWeek < MonthView.dayPerWeekCount;dayOfWeek++)
		{
			String content = getResources().getString(mWeekStringIds[dayOfWeek]);
			
			mWeekCells[dayOfWeek] = new WeekCell(content, new Rect(weekBound), CELL_TEXT_SIZE, false);
			if(dayOfWeek == 0 || dayOfWeek == MonthView.dayPerWeekCount - 1)
			{
				mWeekCells[dayOfWeek].SetWeekend();
			}
			weekBound.offset(CELL_WIDTH, 0);
		}
		
		topMargin += CELL_WEEK_HEIGH;
		
		// build day cells		
		Rect dayBound = new Rect(CELL_MARGIN_LEFT, topMargin, CELL_WIDTH
				+ CELL_MARGIN_LEFT, CELL_HEIGH + topMargin);
		for (int week = 0; week < mCells.length; week++) {
			for (int day = 0; day < mCells[week].length; day++) {
				
				int dayOfMonth = mHelper.getDayAt(week, day);
				
				if (tmp[week][day].thisMonth) {
					if (day == 0 || day == 6) {
						mCells[week][day] = new RedDayCell(tmp[week][day].day,
								new Rect(dayBound), CELL_TEXT_SIZE);
					} else {
						mCells[week][day] = new DayCell(tmp[week][day].day,
								new Rect(dayBound), CELL_TEXT_SIZE);
					}
					if (this.mImageFileMap.containsKey(dayOfMonth)) {
						mCells[week][day].setImageFileNames(this.mImageFileMap
								.get(dayOfMonth));
					}					
				
					//Cache day rectangle
					if(this.mDayRectMap.get(dayOfMonth) == null)
					{
						this.mDayRectMap.put(dayOfMonth, new Rect(dayBound));
					}					
				} else {
					mCells[week][day] = new GrayDayCell(tmp[week][day].day,
							new Rect(dayBound), CELL_TEXT_SIZE);
				}			
								
				
				dayBound.offset(CELL_WIDTH, 0); // move to next column

				// get today
				if (tmp[week][day].day == thisDay && tmp[week][day].thisMonth) {
					mCells[week][day].setToday();
				}
			}
			dayBound.offset(0, CELL_HEIGH); // move to next row and first column
			dayBound.left = CELL_MARGIN_LEFT;
			dayBound.right = CELL_MARGIN_LEFT + CELL_WIDTH;
		}
		
		
		ImageLoader thread = new ImageLoader();
		thread.start();	
	}

	@Override
	public void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		android.util.Log.d(TAG, "left=" + left);

		if(changed)
		{
			initCells();
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	public void setTimeInMillis(long milliseconds) {
		mRightNow.setTimeInMillis(milliseconds);
		initCells();
		this.invalidate();
	}

	public int getYear() {
		return mHelper.getYear();
	}

	public int getMonth() {
		return mHelper.getMonth();
	}

	public void nextMonth() {
		mHelper.nextMonth();
		initCells();
		invalidate();
	}

	public void previousMonth() {
		mHelper.previousMonth();
		initCells();
		invalidate();
	}

	public boolean firstDay(int day) {
		return day == 1;
	}

	public boolean lastDay(int day) {
		return mHelper.getNumberOfDaysInMonth() == day;
	}

	public void goToday() {
		Calendar cal = Calendar.getInstance();
		mHelper = new MonthDisplayHelper(cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH));
		initCells();
		invalidate();
	}

	public Calendar getDate() {
		return mRightNow;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
//		if (mOnCellTouchListener != null) {
//			for (DayCell[] week : mCells) {
//				for (DayCell day : week) {
//					if (day.hitTest((int) event.getX(), (int) event.getY())) {
//						mOnCellTouchListener.onTouch(day);
//						day.onTouch();
//					}
//				}
//			}
//		}
		
		NavTouch navTouch = this.mNavHeader.touchHit((int)event.getX(), (int)event.getY());
		if(navTouch != NavTouch.NONE)
		{
			if(navTouch == NavTouch.LEFT_HIT)
			{
				if(this.currentMonth == 0) 
				{
					currentMonth = 11;
					--currentYear;
				}
				else
				{
					--currentMonth;
				}
			}
			else if(navTouch == NavTouch.RIGHT_HIT)
			{
				if(this.currentMonth == 11) 
				{
					currentMonth = 0;
					++currentYear;
				}
				else
				{
					++currentMonth;
				}			
			}
			
			mHelper = new MonthDisplayHelper(currentYear,
					currentMonth);
			initCells();
			this.invalidate();
		}
		
		return super.onTouchEvent(event);
	}

	public void setOnCellTouchListener(OnCellTouchListener p) {
		mOnCellTouchListener = p;		
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		//set background
		canvas.drawColor(Color.WHITE);
		
		//calculate margins
		int leftMargin = CELL_MARGIN_LEFT;
		int topMargin = CELL_MARGIN_TOP + NAVIGATION_HEIGH;
		int horizontalLength = CELL_WIDTH * MonthView.dayPerWeekCount;
		int verticalLength = CELL_HEIGH * MonthView.weekPerMonthCount + CELL_WEEK_HEIGH;
		
		canvas.drawLine(leftMargin, topMargin, leftMargin + horizontalLength, topMargin, mLinePaint);
		
		//Draw day lines
		for(int week = 0; week <= MonthView.weekPerMonthCount; week++)
		{
			canvas.drawLine(leftMargin, topMargin + CELL_WEEK_HEIGH + week * CELL_HEIGH, leftMargin + horizontalLength, topMargin + CELL_WEEK_HEIGH + week * CELL_HEIGH, mLinePaint);
		}		
		for(int day = 0; day <= MonthView.dayPerWeekCount; day++)
		{
		    canvas.drawLine(leftMargin + day * CELL_WIDTH, topMargin, leftMargin + day * CELL_WIDTH, topMargin + verticalLength, mLinePaint);
		}
		
		// draw week cells
		for (WeekCell dayOfWeek : mWeekCells)
		{
			dayOfWeek.draw(canvas);
		}
		// draw day cells
		for (DayCell[] week : mCells) {
			for (DayCell day : week) {
				day.draw(canvas);				
			}
		}

		// draw header
		mNavHeader.draw(canvas);
		
		// draw today
		if (mDecoration != null && mToday != null) {
			mDecoration.draw(canvas);
		}
		
		if(isImageLoaded)
		{
			Iterator iter = mImageIdMap.entrySet().iterator();
			
			while(iter.hasNext())
			{
				Entry entry = (Entry)iter.next();
				
				Integer day = (Integer)entry.getKey();
				ArrayList<ImagePair> value = (ArrayList<ImagePair>)entry.getValue();
				
				ImagePair pair = value.get(0);
				
				int mediaId = (int)pair.GetMediaId();
				Bitmap bitmap = (Bitmap)pair.GetImage();
				
				Integer dayOfMonth = (Integer)pair.GetDayOfMonth();
				Rect rect = (Rect)(this.mDayRectMap.get(dayOfMonth));
				
				
				Paint pt = new Paint();
				pt.setAlpha(150);						
					
				canvas.drawBitmap(bitmap, rect.left, rect.top, pt);	                    		
			}
		}
	}	
    
	//Image info helper class
	private class ImagePair
	{
		private Integer mMediaId;
		private Bitmap image;
		private Integer mDayOfMonth;
	
		public ImagePair(Integer mediaId, Integer dayOfMonth)
		{
			mMediaId = mediaId;
			mDayOfMonth = dayOfMonth;
		}
		
		public Integer GetMediaId()
		{
			return mMediaId;
		}
		
		public Integer GetDayOfMonth()
		{
			return mDayOfMonth;
		}
		
		public Bitmap GetImage()
		{
			return image;
		}
		
		public void SetImage(Bitmap pImage)
		{
			image = pImage;
		}
	}
	
	private class ImageLoader extends Thread
	{	
		@Override
		public void run()
		{
			Iterator iter = mImageIdMap.entrySet().iterator();
			
			while(iter.hasNext())
			{
				Entry entry = (Entry)iter.next();
				
				Integer day = (Integer)entry.getKey();
				ArrayList<ImagePair> value = (ArrayList<ImagePair>)entry.getValue();
				
				ImagePair pair = value.get(0);				
				int mediaId = (int)pair.GetMediaId();				
				pair.SetImage(GenerateBitmap(mediaId));
			}
			
			isImageLoaded = true;
			
			//Send message to handler
			Message msg = mHandler.obtainMessage();
		    msg.what = DayCell.MsgCode;		    
		    mHandler.sendMessage(msg);
		}
		
		//Scale bitmap to fit cell size
		private Bitmap GenerateBitmap(int mediaId)
	    {
	    	int targetHeight = CELL_HEIGH;
		    int targetWidth = CELL_WIDTH;
		    
	    	BitmapFactory.Options opts = new BitmapFactory.Options();
			Bitmap bitmap = null;
			
		    opts.inJustDecodeBounds = true;
		    bitmap = MediaStore.Images.Thumbnails.getThumbnail(mConResolver, 
					mediaId, 
					MediaStore.Images.Thumbnails.MINI_KIND,// MICRO_KIND, 
					null);
					
		    int height = opts.outHeight;
		    int width = opts.outWidth;
		
		    opts.inJustDecodeBounds = false;
		    opts.inSampleSize = Math.max(height / targetHeight, width / targetWidth);		    		    
		    
			return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);			
	    }
	}
	

	private class GrayDayCell extends DayCell {
		public GrayDayCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			mPaint.setColor(Color.LTGRAY);
		}
	}

	
	private class RedDayCell extends DayCell {
		public RedDayCell(int dayOfMon, Rect rect, float s) {
			super(dayOfMon, rect, s);
			mPaint.setColor(0xdddd0000);
		}
	}

}
