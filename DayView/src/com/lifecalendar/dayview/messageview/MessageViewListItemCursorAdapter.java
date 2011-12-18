package com.lifecalendar.dayview.messageview;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.lifecalendar.dayview.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.CallLog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MessageViewListItemCursorAdapter extends SimpleCursorAdapter {

	private final LayoutInflater mInflater;
	
	public MessageViewListItemCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = mInflater.inflate(R.layout.messageview_list, parent, false);
		return view;
	} 
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub	
		long startTime = System.nanoTime();
		
		TextView tvMessagePerson = (TextView)view.findViewById(R.id.message_person);
		TextView tvMessageDate = (TextView)view.findViewById(R.id.message_date);
		TextView tvMessageBody = (TextView)view.findViewById(R.id.message_body);
		
		String number = cursor.getString(cursor.getColumnIndex("address"));
		String name = cursor.getString(cursor.getColumnIndex("person"));
		String smsbody = cursor.getString(cursor.getColumnIndex("body"));
		String date = cursor.getString(cursor.getColumnIndex("date"));
		int type = cursor.getInt(cursor.getColumnIndex("type"));
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");  
		Date d = new Date(Long.parseLong(date));  
		date = dateFormat.format(d); 
	
		if(name != null){
			tvMessagePerson.setText(name);
		} else{
			tvMessagePerson.setText(number);
		}
		
		tvMessageDate.setText(date);
		
		if(smsbody == null) smsbody = "";
		tvMessageBody.setText(smsbody);
		
		if(type == 1){ //Inbox message
			tvMessageBody.setBackgroundResource(R.drawable.chatfrom_bg);
			LinearLayout messageline = (LinearLayout)view.findViewById(R.id.message_line);
			messageline.setGravity(Gravity.LEFT);
			LinearLayout messageheader = (LinearLayout)view.findViewById(R.id.message_header);
			messageheader.setGravity(Gravity.LEFT);
			
		} else if(type == 2){ //Outbox message
			tvMessageBody.setBackgroundResource(R.drawable.chatto_bg);
			LinearLayout messageline = (LinearLayout)view.findViewById(R.id.message_line);
			messageline.setGravity(Gravity.RIGHT);
			LinearLayout messageheader = (LinearLayout)view.findViewById(R.id.message_header);
			messageheader.setGravity(Gravity.RIGHT);
			
		} else { //others
		
			
		}
		
		System.out.println("Message View list item: number" + number + "-" + "Time:" + date + "Type:" + type);

		super.bindView(view, context, cursor);
		
		long endTime = System.nanoTime();
        long val = (endTime - startTime) / 1000L;
        //System.out.println("Call View list item: number" + number + "-" + "Time:" + value);
	}	
}
