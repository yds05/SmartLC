package com.lifecalendar.dayview.callview;

import java.sql.Date;

import com.lifecalendar.dayview.R;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import android.widget.ListView;

public class CallViewManager {
	
	private ListView listview;
	private Activity activity;
	
	public CallViewManager(Context context)
	{
		this.activity = (Activity)context;
	}
	
	public void setListView(ListView listview)
	{
		this.listview = listview;
	}
	
	public CallViewListCursorAdapter setupList(String where, String dateStr){
		/**��ȡϵͳ��ContentProvider:CallLog.Calls*/
		long dateminSec = Date.parse(dateStr+" 00:00:00");
		long datemaxSec = Date.parse(dateStr+" 23:59:59");
		String selection = "date" + ">=? and date<=? and " + where;
		String[] selectionArg = {"" + dateminSec, "" + datemaxSec};
		Cursor cursor = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, selection, selectionArg, "date ASC");
	//	Cursor cursor = activity.getContentResolver().query(CallLog.Calls.CONTENT_URI,
		//		null, where, null, "date ASC");
		String order = CallLog.Calls.DEFAULT_SORT_ORDER;
		Log.i("order",order);
		//��cursor�������ڽ���activity������		
		activity.startManagingCursor(cursor);
		CallViewListCursorAdapter adapter = new CallViewListCursorAdapter(activity,
				R.layout.callview_list, cursor,
				//new String[] { "name", "date"},
				//new int[] { R.id.Text1, R.id.Text3});
				new String[]{},
				new int[]{});
		
		return adapter;
		
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + listview == null);
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + adapter== null);
		
		//listview.setAdapter(adapter);
		/**����ѯ�������ݷ���ListActivity��ʾ*/
	}
	
}
