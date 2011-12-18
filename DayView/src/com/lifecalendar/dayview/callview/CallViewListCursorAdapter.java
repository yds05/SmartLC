package com.lifecalendar.dayview.callview;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.lifecalendar.dayview.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CallViewListCursorAdapter extends SimpleCursorAdapter {

	//private LayoutInflater mInflater;
	private TextView textView1;
	private TextView textView3;
	private String value;
	public CallViewListCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub	
		long startTime = System.nanoTime();
	
        //得到每一个view的布局文件
        //LinearLayout callViewListItem = null;
        //mInflater = (LayoutInflater)LayoutInflater.from(context);  
        //callViewListItem = (LinearLayout) mInflater.inflate(R.layout.callview_list, null);
		
		//绑定cursor项对应view的值，其中view为cursor中定义格式R.id.list对应的view
		textView1 = (TextView)view.findViewById(R.id.Text1);
		//textView1.setBackgroundColor(Color.RED);
		//textView2 = (TextView)callViewListItem.findViewById(R.id.Text2);
		textView3 = (TextView)view.findViewById(R.id.Text3);
		//textView4 = (TextView)callViewListItem.findViewById(R.id.Text4);	
		//textView3.setBackgroundColor(Color.BLUE);
		
		
		String number = cursor.getString(cursor.getColumnIndex("number"));
		String name = cursor.getString(cursor.getColumnIndex("name"));
		
		if(name!=null){
			textView1.setText(name);
		} else {
			textView1.setText(number);
		}
		
		//System.out.println("current number is: " + number);
	
		//将格林尼治时间转化为本地时间并显示在date项中
		value = cursor.getString(cursor.getColumnIndex("date"));
		long callTime = Long.parseLong(value);
		Date date = new Date(callTime);
		TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
		TimeZone.setDefault(timeZone);
	    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		value = sdFormat.format(date);
		//System.out.println("current log time is: " + value);
		textView3.setText(value);
		
		
		super.bindView(view, context, cursor);
		
		long endTime = System.nanoTime();
        long val = (endTime - startTime) / 1000L;
        System.out.println("Call View list item: number" + number + "-" + "Time:" + value);
	}	
}
