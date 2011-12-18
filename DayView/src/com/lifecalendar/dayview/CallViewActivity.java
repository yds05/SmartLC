package com.lifecalendar.dayview;


import java.util.Calendar;

import com.lifecalendar.dayview.callview.CallViewManager;
import com.lifecalendar.dayview.callview.CallViewListCursorAdapter;
import com.lifecalendar.dayview.callview.SectionedAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CallViewActivity extends ListActivity implements OnItemLongClickListener, OnTouchListener, OnGestureListener {
    private GestureDetector mGestureDetector;

    private MainViewManager mViewManager;
    private CallViewManager callViewManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callview_layout);
        setupViews();
    }
    
    private void setupViews() {
        mGestureDetector = new GestureDetector(this);
        mViewManager = MainViewManager.getInstance();
        android.util.Log.d("TAG", "aaa");
        
        callViewManager = new CallViewManager(this);
        CallViewListCursorAdapter mycursoradapter;

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = year + "/" + month + "/" + day;
        System.out.println("date to get call log is " + date);
        //final ListView listInComingCall = (ListView) findViewById(R.id.incomingcalllist);
        //callViewManager.setListView(listInComingCall);  
        mycursoradapter = callViewManager.setupList("type=1", date);
        adapter.addSection(R.drawable.incoming_call, "IncomingCall", mycursoradapter);
        
        //final ListView listOutGoingCall = (ListView) findViewById(R.id.outgoingcalllist);
        //callViewManager.setListView(listOutGoingCall);  
        mycursoradapter = callViewManager.setupList("type=2", date);
        adapter.addSection(R.drawable.outgoing_call, "OutgoingCall", mycursoradapter);
        
        //final ListView listMissingCall = (ListView) findViewById(R.id.missingcalllist);
        //callViewManager.setListView(listMissingCall);  
        //mycursoradapter = callViewManager.setupList("type=3", "2011/11/06");
        mycursoradapter = callViewManager.setupList("type=3", date);
        adapter.addSection(R.drawable.missing_call, "MissingCall", mycursoradapter);
        
        getListView().setOnItemLongClickListener(this);
        setListAdapter(adapter);
        
    }
    
    SectionedAdapter adapter=new SectionedAdapter() {
    	@Override
		protected View getHeaderView(int image, String caption, int index,View convertView,ViewGroup parent) {
    		LinearLayout lineview = null;
    		ImageView imgCallLogType;
    		TextView txtCallLogType;
			//TextView result=(TextView)convertView;
			
			if (convertView == null) {
				lineview = (LinearLayout)getLayoutInflater().inflate(R.layout.callview_listheader, null);
			}else{
				lineview = (LinearLayout)convertView;
			}
			
			imgCallLogType = (ImageView)lineview.findViewById(R.id.callview_listheader_image);
			txtCallLogType = (TextView)lineview.findViewById(R.id.callview_listheader_text);
			
			imgCallLogType.setImageResource(image);
			txtCallLogType.setText(caption);
			
			return(lineview);
		}


	};
	
	public boolean onItemLongClick(AdapterView parent, View view, int position, long id){
		LinearLayout lineview = (LinearLayout)view;
		TextView number = (TextView)lineview.findViewById(R.id.Text1);
		
		String phoneNumber = number.getText().toString();
		
		if(phoneNumber.trim().length() != 0)
		{
			Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
			
			startActivity(phoneIntent);
		}else{
			Toast.makeText(this, "Number is invalid!", Toast.LENGTH_LONG).show();
		}
		
		return true; 
	}
	
	@Override
	protected void onListItemClick(ListView arg0, View arg1, int arg2, long arg3) {
		LinearLayout lineview = (LinearLayout)arg1;
		TextView number = (TextView)lineview.findViewById(R.id.Text1);
		
		Log.e("CallLogActivity", number.getText() + " is clicked");
		super.onListItemClick(arg0, arg1, arg2, arg3);
	} 

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        android.util.Log.d("TAG", "nnnnnnnn");
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        android.util.Log.d("TAG", "000000");
        try {
            if (mViewManager.getMotionState(e1, e2, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(0));
                if (null == mViewManager.getCurView(0)) {
                    mViewManager.processViews(getParent());
                    mViewManager.onRotateAnimation(0);
                    return true;
                }
                mViewManager.onRotateAnimation(0);
                return true;
            } else if (mViewManager.getMotionState(e2, e1, velocityX, velocityY)) {
                mViewManager.setCurBtnPos( mViewManager.getCurBtnId(3));
                if (null == mViewManager.getCurView(3)) {
                    mViewManager.processViews(getParent());
                    mViewManager.onRotateAnimation(3);
                    return true;
                }
                mViewManager.onRotateAnimation(3);
                return true;
            }
        } catch (Exception ex) {
            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
}
